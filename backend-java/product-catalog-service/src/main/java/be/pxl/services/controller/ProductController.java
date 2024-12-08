package be.pxl.services.controller;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.dto.CreateCategoryRequest;
import be.pxl.services.domain.dto.CreateLabelRequest;
import be.pxl.services.domain.dto.AddProductRequest;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.domain.dto.ProductStockResponse;
import be.pxl.services.domain.dto.UpdateProductRequest;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.services.CategoryService;
import be.pxl.services.services.LabelService;
import be.pxl.services.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

  // With Lombok, we can use `@RequiredArgsConstructor` to inject the service
  private final ProductService productService;
  private final CategoryService categoryService;
  private final LabelService labelService;

  @GetMapping
  public ResponseEntity<List<ProductDTO>> getAll() {
    logger.info("Fetching all products");

    return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<?> getProductById(@PathVariable Long productId) {
    logger.info("Getting product by id {}", productId);

    try {
      return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping
  public ResponseEntity<ProductDTO> addProduct(
      @RequestHeader("Role") String role,
      @RequestBody @Valid AddProductRequest addProductRequest) {
    logger.info("Adding new product");

    if (!"admin".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(
        productService.addProduct(addProductRequest),
        HttpStatus.CREATED);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<?> updateProduct(
      @RequestHeader("Role") String role,
      @PathVariable Long productId,
      @RequestBody @Valid UpdateProductRequest updateProductRequest) {

    if (!"admin".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    try {
      ProductDTO updatedProduct = productService.updateProduct(productId, updateProductRequest, role);
      return new ResponseEntity<ProductDTO>(updatedProduct, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/{id}/stock")
  public ProductStockResponse checkProductStock(@PathVariable Long id) {
    return productService.checkStock(id);
  }

  @PostMapping("/categories/add")
  public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
    Category createdCategory = categoryService.createCategory(createCategoryRequest);
    return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
  }

  @PostMapping("/labels/add")
  public ResponseEntity<Label> createLabel(@RequestBody CreateLabelRequest createLabelRequest) {
    return new ResponseEntity<>(labelService.createLabel(createLabelRequest), HttpStatus.CREATED);
  }

  @PutMapping("/{productId}/category/{categoryId}")
  public ResponseEntity<ProductDTO> assignCategoryToProduct(
      @RequestHeader("Role") String role,
      @PathVariable Long productId,
      @PathVariable Long categoryId) {

    if (!"admin".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    try {
      ProductDTO updatedProduct = productService.assignCategory(productId, categoryId);
      return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/categories")
  public ResponseEntity<List<String>> getAllCategories() {
    List<String> categories = productService.getAllCategories();
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @PutMapping("/{productId}/labels")
  public ResponseEntity<ProductDTO> assignLabelsToProduct(
      @RequestHeader("Role") String role,
      @PathVariable Long productId,
      @RequestBody List<Long> labelIds) {

    if (!"admin".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    try {
      ProductDTO updatedProduct = productService.assignLabels(productId, labelIds);
      return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } catch (NotFoundException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/labels")
  public ResponseEntity<List<String>> getAllLabels() {
    List<String> labels = productService.getAllLabels();
    return new ResponseEntity<>(labels, HttpStatus.OK);
  }
}
