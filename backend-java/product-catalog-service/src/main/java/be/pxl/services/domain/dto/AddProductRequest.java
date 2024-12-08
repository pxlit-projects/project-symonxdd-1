package be.pxl.services.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductRequest {

  private String name;
  private String description;
  private Integer price;
  private Integer stock;
  private String imageUrl;
  private String categoryName;
  private List<String> labelNames;
}
