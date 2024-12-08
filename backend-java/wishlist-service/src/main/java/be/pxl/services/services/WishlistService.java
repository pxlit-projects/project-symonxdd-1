package be.pxl.services.services;

import java.util.List;

import be.pxl.services.domain.WishlistItem;

public interface WishlistService {

  void addToWishlist(String role, Long productId, Integer quantity);

  List<WishlistItem> getAllItems(String role);

  void deleteFromWishlist(String role, Long productId);

  void updateWishlistItemQuantity(String role, Long productId, Integer quantity);
}
