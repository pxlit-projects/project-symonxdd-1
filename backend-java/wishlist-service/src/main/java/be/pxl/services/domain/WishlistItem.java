package be.pxl.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wishlist_id")
  @JsonIgnore
  private Wishlist wishlist;

  private Long productId;
  private String productName;
  private Integer productPrice;
  private Integer quantity;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    WishlistItem wishlistItem = (WishlistItem) o;
    return id != null && id.equals(wishlistItem.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
