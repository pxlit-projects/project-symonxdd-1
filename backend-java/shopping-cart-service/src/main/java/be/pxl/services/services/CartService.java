package be.pxl.services.services;

import java.util.List;

import be.pxl.services.domain.CartItem;

public interface CartService {

  void addToCart(String role, Long productId, Integer quantity);

  List<CartItem> getAllItems(String role);

  void deleteFromCart(String role, Long productId);

  void updateCartItemQuantity(String role, Long productId, Integer quantity);

  void deleteAllFromCart(String role);
}