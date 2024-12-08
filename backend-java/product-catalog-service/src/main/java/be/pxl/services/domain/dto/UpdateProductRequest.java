package be.pxl.services.domain.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

  private String name;
  private String description;
  private Integer price;
  private Integer stock;
  private String imageUrl;
  private String categoryName;
  private List<String> labelNames;

  // New constructor
  public UpdateProductRequest(String name, String description, Integer price, Integer stock, String categoryName,
      List<String> labelNames) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.categoryName = categoryName;
    this.labelNames = labelNames;
  }
}
