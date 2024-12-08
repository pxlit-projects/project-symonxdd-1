package be.pxl.services.domain;

import be.pxl.services.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Product.class)
@Testcontainers
@AutoConfigureMockMvc
public class ProductTests {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ProductRepository productRepository;

  private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.7.37");

  @DynamicPropertySource
  static void registerMySQLProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @Test
  public void testAddProduct() throws Exception {
    Product product = Product.builder()
        .name("Kim Chaewon")
        .description(null)
        .price(1000)
        .build();

    String productString = objectMapper.writeValueAsString(product);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
        .contentType(MediaType.APPLICATION_JSON)
        .content(productString))
        .andExpect(status().isCreated());

    assertEquals(1, productRepository.findAll().size());
  }
}
