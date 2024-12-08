package be.pxl.services.controller;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.pxl.services.domain.WishlistItem;
import be.pxl.services.services.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

  private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);
  private final WishlistService wishlistService;

  @GetMapping("/items")
  public ResponseEntity<List<WishlistItem>> getAllItems(@RequestHeader("Role") String role) {
    logger.info("Fetching all wishlist items for role: {}", role);

    List<WishlistItem> items = wishlistService.getAllItems(role);
    return ResponseEntity.ok(items);
  }

  @PostMapping("/add/{productId}")
  public ResponseEntity<Void> addToWishlist(@RequestHeader("Role") String role, @PathVariable Long productId,
      @RequestParam Integer quantity) {
    logger.info("Adding product with ID {} to wishlist for role: {}", productId, role);

    wishlistService.addToWishlist(role, productId, quantity);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/products/delete/{productId}")
  public ResponseEntity<?> deleteFromWishlist(@RequestHeader("Role") String role, @PathVariable Long productId) {
    logger.info("Deleting product with ID {} from wishlist for role: {}", productId, role);

    try {
      wishlistService.deleteFromWishlist(role, productId);
      return new ResponseEntity<>("Product removed from wishlist!", HttpStatus.NO_CONTENT);
    } catch (RuntimeException ex) {
      return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{productId}")
  public ResponseEntity<Void> updateWishlistItemQuantity(
      @RequestHeader("Role") String role,
      @PathVariable Long productId,
      @RequestParam Integer quantity) {
    logger.info("Updating wishlist item quantity with product ID {} from wishlist for role: {}", productId, role);

    if (!"user".equalsIgnoreCase(role)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    wishlistService.updateWishlistItemQuantity(role, productId, quantity);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
