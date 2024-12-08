package be.pxl.services.repository;

import java.util.List;

import be.pxl.services.domain.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.pxl.services.domain.Wishlist;

import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

  WishlistItem findByProductId(Long productId);
}
