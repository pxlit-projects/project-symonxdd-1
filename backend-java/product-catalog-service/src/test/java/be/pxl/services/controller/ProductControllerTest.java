package be.pxl.services.controller;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.dto.*;
import be.pxl.services.exception.NotFoundException;
import be.pxl.services.services.CategoryService;
import be.pxl.services.services.LabelService;
import be.pxl.services.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @MockBean
  private CategoryService categoryService;

  @MockBean
  private LabelService labelService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void getAllProductsTest() throws Exception {
    List<ProductDTO> products = Arrays.asList(new ProductDTO(), new ProductDTO());
    given(productService.getAll()).willReturn(products);

    mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(products.size()));
  }

  @Test
  public void getProductByIdTest() throws Exception {
    ProductDTO product = new ProductDTO();
    given(productService.getProductById(1L)).willReturn(product);

    mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(product.getId()));
  }

  @Test
  public void getProductByIdNotFoundTest() throws Exception {
    given(productService.getProductById(1L)).willThrow(new NotFoundException("Product not found"));

    mockMvc.perform(get("/api/products/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Product not found"));
  }

  @Test
  public void addProductTest() throws Exception {
    AddProductRequest request = new AddProductRequest();
    ProductDTO product = new ProductDTO();
    given(productService.addProduct(any(AddProductRequest.class))).willReturn(product);

    mockMvc.perform(post("/api/products")
                    .header("Role", "admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
  }

  @Test
  public void updateProductTest() throws Exception {
    UpdateProductRequest request = new UpdateProductRequest();
    ProductDTO product = new ProductDTO();
    given(productService.updateProduct(eq(1L), any(UpdateProductRequest.class), eq("admin")))
            .willReturn(product);

    mockMvc.perform(put("/api/products/1")
                    .header("Role", "admin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
  }

  @Test
  public void checkProductStockTest() throws Exception {
    ProductStockResponse stockResponse = new ProductStockResponse(1L, true, 100);
    given(productService.checkStock(1L)).willReturn(stockResponse);

    mockMvc.perform(get("/api/products/1/stock"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId").value(stockResponse.getProductId()))
            .andExpect(jsonPath("$.inStock").value(stockResponse.isInStock()));
  }

  @Test
  public void createCategoryTest() throws Exception {
    CreateCategoryRequest request = new CreateCategoryRequest();
    Category category = new Category();
    given(categoryService.createCategory(any(CreateCategoryRequest.class))).willReturn(category);

    mockMvc.perform(post("/api/products/categories/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
  }
}
