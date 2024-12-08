package be.pxl.services.dto;

import lombok.Data;

@Data
public class ProductStockResponse {
  private Long productId;
  private boolean inStock;
  private Integer availableQuantity;
}
