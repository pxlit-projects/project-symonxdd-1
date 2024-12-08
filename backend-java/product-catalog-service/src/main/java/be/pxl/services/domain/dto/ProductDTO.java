package be.pxl.services.domain.dto;

import java.util.Set;
import java.util.stream.Collectors;

import be.pxl.services.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import be.pxl.services.domain.Label;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

  private Long id;
  private String name;
  private String description;
  private Integer price;
  private Integer stock;
  private String imageUrl;

  private String categoryName;
  private Set<String> labelNames;

  public ProductDTO(Product product) {
    this.id = product.getId();  // Map the id
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.stock = product.getStock();
    this.imageUrl = product.getImageUrl();
    this.categoryName = categoryName != null ? categoryName : "";
    this.labelNames = product.getLabels() != null
            ? product.getLabels().stream().map(Label::getName).collect(Collectors.toSet())
            : Set.of(); // Default to empty set if labels are null
  }

  // Constructor to initialize with Product, categoryName, and labelNames
  public ProductDTO(Product product, String categoryName, Set<String> labelNames) {
    this.id = product.getId();
    this.name = product.getName();
    this.description = product.getDescription();
    this.price = product.getPrice();
    this.stock = product.getStock();
    this.imageUrl = product.getImageUrl();
    this.categoryName = categoryName != null ? categoryName : "";
    this.labelNames = labelNames != null ? labelNames : Set.of(); // Default to empty set if labelNames are null
  }
}
