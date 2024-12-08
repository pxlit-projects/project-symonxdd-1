package be.pxl.services.init;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.Product;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataInitializerTests {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private LabelRepository labelRepository;

  @InjectMocks
  private DataInitializer dataInitializer;

  @Captor
  private ArgumentCaptor<List<Product>> productCaptor;

  private Category ecoFriendly;
  private Category recycledMaterials;
  private Category energyEfficient;
  private Label organic;
  private Label biodegradable;
  private Label fairTrade;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ecoFriendly = new Category("Eco-Friendly");
    recycledMaterials = new Category("Recycled Materials");
    energyEfficient = new Category("Energy Efficient");
    organic = new Label("Organic");
    biodegradable = new Label("Biodegradable");
    fairTrade = new Label("Fair Trade");

    when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(labelRepository.save(any(Label.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  void testRun_whenNoProductsExist_initializesData() throws Exception {
    // Arrange
    when(productRepository.count()).thenReturn(0L);

    // Act
    dataInitializer.run();

    // Assert
    verify(categoryRepository, times(3)).save(any(Category.class));
    verify(labelRepository, times(3)).save(any(Label.class));
    verify(productRepository).saveAll(productCaptor.capture());

    List<Product> capturedProducts = productCaptor.getValue();
    assertEquals(9, capturedProducts.size());

    // Verifying individual products and their properties
    Product recycledBottle = capturedProducts.stream()
        .filter(p -> p.getName().equals("Recycled Water Bottle"))
        .findFirst()
        .orElse(null);

    assertNotNull(recycledBottle);
    assertEquals(15, recycledBottle.getPrice());
    assertEquals(100, recycledBottle.getStock());

    Product solarLantern = capturedProducts.stream()
        .filter(p -> p.getName().equals("Solar Powered Lantern"))
        .findFirst()
        .orElse(null);

    assertNotNull(solarLantern);
    assertEquals(35, solarLantern.getPrice());
    assertEquals(50, solarLantern.getStock());

    Product cottonTShirt = capturedProducts.stream()
        .filter(p -> p.getName().equals("Organic Cotton T-Shirt"))
        .findFirst()
        .orElse(null);

    assertNotNull(cottonTShirt);
    assertEquals(25, cottonTShirt.getPrice());
    assertEquals(200, cottonTShirt.getStock());
  }

  @Test
  void testRun_whenProductsExist_doesNotInitializeData() throws Exception {
    // Arrange
    when(productRepository.count()).thenReturn(10L);

    // Act
    dataInitializer.run();

    // Assert
    verify(productRepository, never()).saveAll(any());
    verify(categoryRepository, never()).save(any());
    verify(labelRepository, never()).save(any());
  }

  @Test
  void testRun_initializesCategoriesAndLabelsCorrectly() throws Exception {
    // Arrange
    when(productRepository.count()).thenReturn(0L);

    // Act
    dataInitializer.run();

    // Assert
    verify(categoryRepository, times(3)).save(any(Category.class));
    verify(labelRepository, times(3)).save(any(Label.class));
  }

  @Test
  void testRun_initializesProductsWithCorrectCategoriesAndLabels() throws Exception {
    // Arrange
    when(productRepository.count()).thenReturn(0L);

    // Act
    dataInitializer.run();

    // Assert
    verify(productRepository).saveAll(productCaptor.capture());
    List<Product> capturedProducts = productCaptor.getValue();

    Product recycledBottle = capturedProducts.stream()
        .filter(p -> p.getName().equals("Recycled Water Bottle"))
        .findFirst()
        .orElse(null);

    assertNotNull(recycledBottle);
    assertEquals(recycledMaterials, recycledBottle.getCategory());
    assertTrue(recycledBottle.getLabels().contains(organic));
    assertTrue(recycledBottle.getLabels().contains(biodegradable));

    Product solarLantern = capturedProducts.stream()
        .filter(p -> p.getName().equals("Solar Powered Lantern"))
        .findFirst()
        .orElse(null);

    assertNotNull(solarLantern);
    assertEquals(energyEfficient, solarLantern.getCategory());
    assertTrue(solarLantern.getLabels().contains(biodegradable));
  }
}