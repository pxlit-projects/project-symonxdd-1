package be.pxl.services.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.AddProductRequest;
import be.pxl.services.domain.dto.ProductChangeDTO;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.domain.dto.ProductStockResponse;
import be.pxl.services.domain.dto.UpdateProductRequest;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.LabelRepository;
import be.pxl.services.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

  // With Lombok, we can use `@RequiredArgsConstructor` to inject the repository
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final LabelRepository labelRepository;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public List<ProductDTO> getAll() {
    List<Product> products = productRepository.findAll();

    // Fetch all labels and map them by ID to name
    Map<Long, String> labelIdToNameMap = labelRepository.findAll().stream()
        .collect(Collectors.toMap(Label::getId, Label::getName));

    // Fetch all categories and map them by ID to name
    Map<Long, String> categoryIdToNameMap = categoryRepository.findAll().stream()
        .collect(Collectors.toMap(Category::getId, Category::getName));

    logger.info("Fetching all products");

    return products.stream()
        .map(product -> {
          String categoryName = categoryIdToNameMap
              .get(product.getCategory() != null ? product.getCategory().getId() : null);

          Set<String> labelNames = product.getLabels() != null
              ? product.getLabels().stream()
                  .map(label -> labelIdToNameMap.get(label.getId()))
                  .collect(Collectors.toSet())
              : Set.of();

          return new ProductDTO(product, categoryName, labelNames);
        })
        .collect(Collectors.toList());
  }

  @Override
  public ProductDTO getProductById(Long id) {
    logger.info("Getting product by id {}", id);

    return productRepository.findById(id)
        .map(ProductDTO::new)
        .orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
  }

  @Transactional
  public ProductDTO addProduct(AddProductRequest addProductRequest) {
    logger.info("Adding new product");

    // Check if category exists, otherwise create it
    Category category = categoryRepository
        .findByName(addProductRequest.getCategoryName())
        .orElseGet(() -> categoryRepository.save(new Category(addProductRequest.getCategoryName())));

    // Check if labels exist, otherwise create them
    Set<Label> labels = new HashSet<>();
    for (String labelName : addProductRequest.getLabelNames()) {
      Label label = labelRepository
          .findByName(labelName)
          .orElseGet(() -> labelRepository.save(new Label(labelName)));
      labels.add(label);
    }

    // Create product entity
    Product product = Product.builder()
        .name(addProductRequest.getName())
        .description(addProductRequest.getDescription())
        .price(addProductRequest.getPrice())
        .stock(addProductRequest.getStock())
        .imageUrl(addProductRequest.getImageUrl())
        .category(category)
        .labels(labels)
        .build();

    Product newProduct = productRepository.save(product);

    logger.info("New product added: {}", newProduct);
    return new ProductDTO(newProduct);
  }

  public ProductDTO updateProduct(Long productId, UpdateProductRequest updateProductRequest, String role) {
    logger.info("Updating product with ID: {}", productId);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("No product with ID: " + productId));

    // Store old values for logging purposes
    String oldName = product.getName();
    String oldDescription = product.getDescription();
    Integer oldPrice = product.getPrice();
    Integer oldStock = product.getStock();
    String oldCategory = product.getCategory().getName();

    // Update fields only if new values are provided
    if (updateProductRequest.getName() != null) {
      product.setName(updateProductRequest.getName());
    }
    if (updateProductRequest.getDescription() != null) {
      product.setDescription(updateProductRequest.getDescription());
    }
    if (updateProductRequest.getPrice() != null) {
      product.setPrice(updateProductRequest.getPrice());
    }
    if (updateProductRequest.getStock() != null) {
      product.setStock(updateProductRequest.getStock());
    }

    if (updateProductRequest.getCategoryName() != null) {
      Category category = categoryRepository.findByName(updateProductRequest.getCategoryName())
          .orElseGet(() -> categoryRepository.save(new Category(updateProductRequest.getCategoryName())));
      product.setCategory(category);
    }
    if (updateProductRequest.getLabelNames() != null) {
      Set<Label> newLabels = convertStringsToLabels(updateProductRequest.getLabelNames());
      product.setLabels(newLabels);
    }

    Product updatedProduct = productRepository.save(product);

    logger.info("Product updated: {}", updatedProduct);

    ProductChangeDTO changeDTO = ProductChangeDTO.builder()
        .productId(productId)
        .oldName(oldName)
        .newName(updateProductRequest.getName())
        .oldDescription(oldDescription)
        .newDescription(updateProductRequest.getDescription())
        .oldPrice(oldPrice)
        .newPrice(updateProductRequest.getPrice())
        .oldStock(oldStock)
        .newStock(updateProductRequest.getStock())
        .oldCategory(oldCategory)
        .newCategory(updateProductRequest.getCategoryName())
        .role(role)
        .build();

    logger.info("Sending message (ProductChangeDTO) to RabbitMQ queue: {}", "myQueue");
    rabbitTemplate.convertAndSend("myQueue", changeDTO);
    logger.info("Message (ProductChangeDTO) sent to RabbitMQ queue: {}", "myQueue");

    return new ProductDTO(updatedProduct);
  }

  // Utility method to convert List<String> to Set<Label>
  private Set<Label> convertStringsToLabels(List<String> labelNames) {
    Set<Label> labels = new HashSet<>();
    for (String labelName : labelNames) {
      // Create or retrieve Label entity based on labelName
      Label label = labelRepository.findByName(labelName)
          .orElseGet(() -> labelRepository.save(new Label(labelName)));
      labels.add(label);
    }
    return labels;
  }

  public ProductDTO assignCategory(Long productId, Long categoryId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found"));
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new NotFoundException("Category not found"));

    product.setCategory(category);
    return toDTO(productRepository.save(product));
  }

  public ProductDTO assignLabels(Long productId, List<Long> labelIds) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found"));
    Set<Label> labels = new HashSet<>(labelRepository.findAllById(labelIds));

    product.setLabels(labels);
    return toDTO(productRepository.save(product));
  }

  public ProductStockResponse checkStock(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    boolean inStock = product.getStock() > 0;
    return ProductStockResponse.builder()
        .productId(productId)
        .inStock(inStock)
        .availableQuantity(product.getStock())
        .build();
  }

  private ProductDTO toDTO(Product product) {
    ProductDTO dto = new ProductDTO();
    dto.setId(product.getId());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    return dto;
  }

  @Override
  public List<String> getAllCategories() {
    logger.info("Fetching all categories");

    return categoryRepository.findAll().stream()
        .map(Category::getName) // Adjust this if your Category entity has a different field
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getAllLabels() {
    logger.info("Fetching all labels");

    return labelRepository.findAll().stream()
        .map(Label::getName) // Assuming Label entity has a getName method
        .distinct()
        .collect(Collectors.toList());
  }

  public ProductDTO findById(Long productId) {
    return productRepository.findById(productId).map(ProductDTO::new)
        .orElseThrow(() -> new NotFoundException("No product with ID: " + productId));
  }
}
