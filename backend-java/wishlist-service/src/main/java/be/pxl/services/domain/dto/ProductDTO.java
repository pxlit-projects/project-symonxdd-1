package be.pxl.services.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
  private Long id;
  private String name;
  private Integer price;
}
