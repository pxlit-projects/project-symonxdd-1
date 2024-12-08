package be.pxl.services.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ProductChangeDTO {

  @JsonProperty("productId")
  private Long productId;

  @JsonProperty("oldName")
  private String oldName;

  @JsonProperty("newName")
  private String newName;

  @JsonProperty("oldDescription")
  private String oldDescription;

  @JsonProperty("newDescription")
  private String newDescription;

  @JsonProperty("oldPrice")
  private Integer oldPrice;

  @JsonProperty("newPrice")
  private Integer newPrice;

  @JsonProperty("oldStock")
  private Integer oldStock;

  @JsonProperty("newStock")
  private Integer newStock;

  @JsonProperty("oldCategory")
  private String oldCategory;

  @JsonProperty("newCategory")
  private String newCategory;

  @JsonProperty("role")
  private String role;

  @JsonCreator
  public ProductChangeDTO(
      @JsonProperty("productId") Long productId,
      @JsonProperty("oldName") String oldName,
      @JsonProperty("newName") String newName,
      @JsonProperty("oldDescription") String oldDescription,
      @JsonProperty("newDescription") String newDescription,
      @JsonProperty("oldPrice") Integer oldPrice,
      @JsonProperty("newPrice") Integer newPrice,
      @JsonProperty("oldStock") Integer oldStock,
      @JsonProperty("newStock") Integer newStock,
      @JsonProperty("oldCategory") String oldCategory,
      @JsonProperty("newCategory") String newCategory,
      @JsonProperty("role") String role) {
    this.productId = productId;
    this.oldName = oldName;
    this.newName = newName;
    this.oldDescription = oldDescription;
    this.newDescription = newDescription;
    this.oldPrice = oldPrice;
    this.newPrice = newPrice;
    this.oldStock = oldStock;
    this.newStock = newStock;
    this.oldCategory = oldCategory;
    this.newCategory = newCategory;
    this.role = role;
  }
}
