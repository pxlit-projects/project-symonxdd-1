package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.*;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.LabelRepository;
import be.pxl.services.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTests {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private LabelRepository labelRepository;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private ProductServiceImpl productService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAll_ShouldReturnProductDTOList() {
    // Arrange
    Category category = new Category("Electronics");
    category.setId(1L);
    Label label = new Label();
    label.setId(1L);
    label.setName("New");
    Product product = new Product();
    product.setId(1L);
    product.setName("Product1");
    product.setDescription("Description1");
    product.setPrice(100);
    product.setStock(10);
    product.setImageUrl("url");
    product.setCategory(category);
    product.setLabels(Set.of(label));

    when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
    when(labelRepository.findAll()).thenReturn(Collections.singletonList(label));
    when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

    // Act
    List<ProductDTO> productDTOs = productService.getAll();

    // Assert
    assertEquals(1, productDTOs.size());
    assertEquals("Product1", productDTOs.get(0).getName());
    assertEquals("Electronics", productDTOs.get(0).getCategoryName());
    assertTrue(productDTOs.get(0).getLabelNames().contains("New"));
  }

  @Test
  void getProductById_ShouldReturnProductDTO() {
    // Arrange
    Product product = new Product();
    product.setId(1L);
    product.setName("Product1");
    product.setDescription("Description1");
    product.setPrice(100);
    product.setStock(10);
    product.setImageUrl("url");

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    // Act
    ProductDTO productDTO = productService.getProductById(1L);

    // Assert
    assertEquals("Product1", productDTO.getName());
  }

  @Test
  void addProduct_ShouldSaveAndReturnProductDTO() {
    // Arrange
    AddProductRequest request = new AddProductRequest("Product1", "Description1", 100, 10, "url", "Electronics",
        List.of("New"));
    Category category = new Category("Electronics");
    category.setId(1L);
    Label label = new Label();
    label.setName("New");
    label.setId(1L);
    Product product = new Product();
    product.setId(1L);
    product.setName("Product1");
    product.setDescription("Description1");
    product.setPrice(100);
    product.setStock(10);
    product.setImageUrl("url");
    product.setCategory(category);
    product.setLabels(Set.of(label));

    when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
    when(labelRepository.findByName("New")).thenReturn(Optional.of(label));
    when(productRepository.save(any(Product.class))).thenReturn(product);

    // Act
    ProductDTO productDTO = productService.addProduct(request);

    // Assert
    assertEquals("Product1", productDTO.getName());
  }

  @Test
  void updateProduct_ShouldUpdateAndReturnProductDTO() {
    // Arrange
    Product existingProduct = new Product();
    existingProduct.setId(1L);
    existingProduct.setName("Old Product");
    existingProduct.setDescription("Old Description");
    existingProduct.setPrice(50);
    existingProduct.setStock(5);
    existingProduct.setCategory(new Category("OldCategory"));
    existingProduct.getCategory().setId(1L);

    UpdateProductRequest updateRequest = new UpdateProductRequest("New Product", "New Description", 75, 10,
        "NewCategory", List.of("NewLabel"));
    Category newCategory = new Category("NewCategory");
    newCategory.setId(2L);
    Label newLabel = new Label();
    newLabel.setName("NewLabel");
    newLabel.setId(2L);

    when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
    when(categoryRepository.findByName("NewCategory")).thenReturn(Optional.of(newCategory));
    when(labelRepository.findByName("NewLabel")).thenReturn(Optional.of(newLabel));
    when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
    doNothing().when(rabbitTemplate).convertAndSend(eq("myQueue"), any(ProductChangeDTO.class));

    // Act
    ProductDTO productDTO = productService.updateProduct(1L, updateRequest, "admin");

    // Assert
    assertEquals("New Product", productDTO.getName());
  }

  @Test
  void assignCategory_ShouldAssignAndReturnProductDTO() {
    // Arrange
    Product product = new Product();
    product.setId(1L);
    product.setName("Product1");
    product.setDescription("Description1");
    product.setPrice(100);
    product.setStock(10);
    product.setImageUrl("url");

    // Create and set up the Category
    Category category = new Category("Electronics");
    category.setId(2L); // Assign an ID to the category

    // Mock the repository methods
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
      Product savedProduct = invocation.getArgument(0);
      savedProduct.setCategory(category); // Ensure the category is set
      return savedProduct;
    });

    // Act
    ProductDTO productDTO = productService.assignCategory(1L, 2L);

    // Assert
    assertNotNull(productDTO);
  }

  @Test
  void assignLabels_ShouldAssignAndReturnProductDTO() {
    // Arrange
    Product product = new Product();
    product.setId(1L);
    product.setName("Product1");
    product.setDescription("Description1");
    product.setPrice(100);
    product.setStock(10);
    product.setImageUrl("url");

    Label label = new Label();
    label.setId(1L);
    label.setName("New");

    List<Label> labelList = Collections.singletonList(label);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(labelRepository.findAllById(anyList())).thenReturn(labelList);
    when(productRepository.save(any(Product.class))).thenReturn(product);

    // Act
    ProductDTO productDTO = productService.assignLabels(1L, List.of(1L));

    // Assert
    assertNotNull(productDTO);
  }

  @Test
  void checkStock_ShouldReturnProductStockResponse() {
    // Arrange
    Product product = new Product();
    product.setId(1L);
    product.setStock(10);

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    // Act
    ProductStockResponse stockResponse = productService.checkStock(1L);

    // Assert
    assertTrue(stockResponse.isInStock());
    assertEquals(10, stockResponse.getAvailableQuantity());
  }

  @Test
  void getAllCategories_ShouldReturnCategoryNames() {
    // Arrange
    Category category = new Category("Electronics");
    category.setId(1L);
    when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

    // Act
    List<String> categories = productService.getAllCategories();

    // Assert
    assertTrue(categories.contains("Electronics"));
  }

  @Test
  void getAllLabels_ShouldReturnLabelNames() {
    // Arrange
    Label label = new Label();
    label.setId(1L);
    label.setName("New");
    when(labelRepository.findAll()).thenReturn(Collections.singletonList(label));

    // Act
    List<String> labels = productService.getAllLabels();

    // Assert
    assertTrue(labels.contains("New"));
  }
}
