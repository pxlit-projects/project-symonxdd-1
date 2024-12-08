package be.pxl.services.controller;

import be.pxl.services.domain.CartItem;
import be.pxl.services.services.CartService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private static final Logger logger = LoggerFactory.getLogger(CartController.class);
  private final CartService cartService;

  @GetMapping("/items")
  public ResponseEntity<List<CartItem>> getAllItems(@RequestHeader("Role") String role) {
    logger.info("Fetching all cart items for role: {}", role);

    List<CartItem> items = cartService.getAllItems(role);
    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @PostMapping("/products/add/{productId}")
  public ResponseEntity<?> addToCart(
      @RequestHeader("Role") String role,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    logger.info("Adding product to cart for role: {}", role);

    if (!"user".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    try {
      cartService.addToCart(role, productId, quantity);
      return new ResponseEntity<>("Product added to cart!", HttpStatus.NO_CONTENT);
    } catch (RuntimeException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/products/delete/{productId}")
  public ResponseEntity<?> deleteFromCart(@RequestHeader("Role") String role, @PathVariable Long productId) {
    logger.info("Deleting product from cart for role: {}", role);

    try {
      cartService.deleteFromCart(role, productId);
      return new ResponseEntity<>("Product removed from cart!", HttpStatus.NO_CONTENT);
    } catch (RuntimeException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/items/{cartItemId}")
  public ResponseEntity<String> updateCartItemQuantity(@RequestHeader("Role") String role,
      @PathVariable Long cartItemId, @RequestParam Integer quantity) {
    cartService.updateCartItemQuantity(role, cartItemId, quantity);
    return new ResponseEntity<>("Cart item quantity updated!", HttpStatus.OK);
  }

  @DeleteMapping("/products/delete/all")
  public ResponseEntity<?> deleteAllFromCart(@RequestHeader("Role") String role) {
    try {
      cartService.deleteAllFromCart(role);
      return new ResponseEntity<>("All products removed from cart!", HttpStatus.NO_CONTENT);
    } catch (RuntimeException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
