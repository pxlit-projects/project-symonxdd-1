package be.pxl.services.domain.dto;

import lombok.Data;

@Data
public class ProductDTO {
  private Long id;
  private String name;
  private Integer price;
}
