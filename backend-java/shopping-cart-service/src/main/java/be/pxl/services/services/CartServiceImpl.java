package be.pxl.services.services;

import be.pxl.services.client.ProductCatalogClient;
import org.springframework.http.HttpStatus;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.repository.CartRepository;
import be.pxl.services.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductCatalogClient productCatalogClient;

  @Override
  public List<CartItem> getAllItems(String role) {
    logger.info("Fetching all items for role: {}", role);

    Cart cart = cartRepository.findByRole(role)
        .orElseThrow(() -> new RuntimeException("Cart not found for role: " + role));

    logger.info("All items fetched for role: {}", role);
    return cart.getItems().stream().collect(Collectors.toList());
  }

  @Override
  public void addToCart(String role, Long productId, Integer quantity) {
    logger.info("Adding product with id: {}", productId);

    // Fetch the cart for the provided role
    Cart cart = cartRepository.findByRole(role)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for role: " + role));

    // Fetch product details using Feign client
    ProductDTO product = productCatalogClient.getProductById(productId);
    logger.info("ProductCatalogClient response: {}", product);

    if (product == null) {
      throw new RuntimeException("Product not found");
    }

    // Create and add CartItem
    CartItem item = CartItem.builder()
        .cart(cart)
        .productId(productId)
        .productName(product.getName())
        .productPrice(product.getPrice())
        .quantity(quantity)
        .build();

    cart.addItem(item);
    cartRepository.save(cart); // Saves the cart and the item due to cascade

    logger.info("Product added to cart: {}", item);
  }

  @Override
  public void deleteFromCart(String role, Long productId) {
    logger.info("Deleting product with id: {}", productId);

    Cart cart = cartRepository.findByRole(role)
        .orElseThrow(() -> new RuntimeException("Cart not found for role: " + role));

    CartItem itemToRemove = cart.getItems().stream()
        .filter(item -> item.getProductId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Product not found in cart"));

    cart.removeItem(itemToRemove);
    cartRepository.save(cart); // Saves the updated cart

    logger.info("Product removed from cart: {}", itemToRemove);
  }

  @Override
  public void deleteAllFromCart(String role) {
    logger.info("Deleting all products from cart for role: {}", role);

    Cart cart = cartRepository.findByRole(role)
        .orElseThrow(() -> new RuntimeException("Cart not found for role: " + role));

    cart.getItems().clear(); // Remove all items from the cart
    cartRepository.save(cart); // Save the updated cart

    logger.info("All products removed from cart for role: {}", role);
  }

  @Override
  public void updateCartItemQuantity(String role, Long productId, Integer quantity) {
    logger.info("Updating quantity of product with id: {}", productId);

    Cart cart = cartRepository.findByRole(role)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for role: " + role));

    CartItem itemToUpdate = cart.getItems().stream()
        .filter(item -> item.getId().equals(productId)) // Use cartItemId here
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CartItem not found"));

    itemToUpdate.setQuantity(quantity);
    cartItemRepository.save(itemToUpdate); // Save the updated item

    logger.info("Quantity updated for product: {}", itemToUpdate);
  }
}
