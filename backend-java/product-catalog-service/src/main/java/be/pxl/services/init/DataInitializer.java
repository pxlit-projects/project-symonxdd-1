package be.pxl.services.init;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.Product;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.repository.CategoryRepository;
import be.pxl.services.repository.LabelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Order(1) // Ensures this runs early
public class DataInitializer implements CommandLineRunner {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final LabelRepository labelRepository;

  public DataInitializer(ProductRepository productRepository, CategoryRepository categoryRepository, LabelRepository labelRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.labelRepository = labelRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    if (productRepository.count() == 0) { // Check if the database is empty
      // Create Categories
      Category ecoFriendly = categoryRepository.save(new Category("Eco-Friendly"));
      Category recycledMaterials = categoryRepository.save(new Category("Recycled Materials"));
      Category energyEfficient = categoryRepository.save(new Category("Energy Efficient"));

      // Create Labels
      Label organic = labelRepository.save(new Label("Organic"));
      Label biodegradable = labelRepository.save(new Label("Biodegradable"));
      Label fairTrade = labelRepository.save(new Label("Fair Trade"));

      // Save Products
      productRepository.saveAll(List.of(
              Product.builder()
                      .name("Recycled Water Bottle")
                      .description("An eco-friendly water bottle made from 100% recycled plastic.")
                      .price(15)
                      .stock(100)
                      .imageUrl("images/recycled_bottle.jpg")
                      .category(recycledMaterials)
                      .labels(Set.of(organic, biodegradable))
                      .build(),
              Product.builder()
                      .name("Solar Powered Lantern")
                      .description("A solar-powered lantern perfect for camping and outdoor activities.")
                      .price(35)
                      .stock(50)
                      .imageUrl("images/solar_lantern.jpg")
                      .category(energyEfficient)
                      .labels(Set.of(biodegradable))
                      .build(),
              Product.builder()
                      .name("Organic Cotton T-Shirt")
                      .description("A soft and comfortable t-shirt made from 100% organic cotton.")
                      .price(25)
                      .stock(200)
                      .imageUrl("images/organic_tshirt.jpg")
                      .category(ecoFriendly)
                      .labels(Set.of(organic, fairTrade))
                      .build(),
              Product.builder()
                      .name("Bamboo Toothbrush")
                      .description("A sustainable bamboo toothbrush with biodegradable bristles.")
                      .price(5)
                      .stock(300)
                      .imageUrl("images/bamboo_toothbrush.jpg")
                      .category(ecoFriendly)
                      .labels(Set.of(biodegradable))
                      .build(),
              Product.builder()
                      .name("Compostable Phone Case")
                      .description("A phone case made from compostable materials.")
                      .price(20)
                      .stock(75)
                      .imageUrl("images/compostable_case.jpg")
                      .category(recycledMaterials)
                      .labels(Set.of(biodegradable))
                      .build(),
              Product.builder()
                      .name("Eco-Friendly Yoga Mat")
                      .description("A yoga mat made from natural rubber, providing excellent grip and comfort.")
                      .price(45)
                      .stock(60)
                      .imageUrl("images/yoga_mat.jpg")
                      .category(ecoFriendly)
                      .build(),
              Product.builder()
                      .name("Energy Efficient LED Bulb")
                      .description("An LED bulb that uses 80% less energy than traditional bulbs.")
                      .price(10)
                      .stock(120)
                      .imageUrl("images/led_bulb.jpg")
                      .category(energyEfficient)
                      .build(),
              Product.builder()
                      .name("Reusable Shopping Bags")
                      .description("Set of 5 reusable shopping bags made from durable fabric.")
                      .price(12)
                      .stock(200)
                      .imageUrl("images/reusable_bags.jpg")
                      .category(ecoFriendly)
                      .build(),
              Product.builder()
                      .name("Biodegradable Trash Bags")
                      .description("Trash bags made from plant-based materials that break down naturally.")
                      .price(18)
                      .stock(150)
                      .imageUrl("images/biodegradable_bags.jpg")
                      .category(recycledMaterials)
                      .labels(Set.of(biodegradable))
                      .build()));
    }
  }
}
