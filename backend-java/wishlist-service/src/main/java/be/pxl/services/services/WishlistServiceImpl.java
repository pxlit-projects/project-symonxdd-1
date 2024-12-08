package be.pxl.services.services;

import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repository.WishlistItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import be.pxl.services.client.ProductCatalogClient;
import be.pxl.services.domain.Wishlist;
import be.pxl.services.domain.WishlistItem;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

  private final WishlistRepository wishlistRepository;
  private final WishlistItemRepository wishlistItemRepository;
  private final ProductCatalogClient productCatalogClient;

  @Override
  public List<WishlistItem> getAllItems(String role) {
    logger.info("Fetching all items for role: {}", role);

    Wishlist wishlist = wishlistRepository.findByRole(role)
        .orElseThrow(() -> new RuntimeException("Cart not found for role: " + role));

    logger.info("All items fetched for role: {}", role);

    return wishlist.getItems().stream().collect(Collectors.toList());
  }

  @Override
  public void addToWishlist(String role, Long productId, Integer quantity) {
    logger.info("Adding product with id: {}", productId);

    // Fetch the wishlist for the provided role
    Wishlist wishlist = wishlistRepository.findByRole(role)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wishlist not found for role: " + role));

    // Check if the product is already in the wishlist
    boolean productExists = wishlist.getItems().stream()
        .anyMatch(item -> item.getProductId().equals(productId));

    if (!productExists) {
      // Fetch product details using Feign client
      ProductDTO product = productCatalogClient.getProductById(productId);
      System.out.println("ProductCatalogClient response: " + product);

      if (product == null) {
        throw new RuntimeException("Product not found");
      }

      // Create and add WishlistItem
      WishlistItem item = WishlistItem.builder()
          .wishlist(wishlist)
          .productId(productId)
          .productName(product.getName())
          .productPrice(product.getPrice())
          .quantity(quantity)
          .build();

      wishlist.addItem(item);
      wishlistRepository.save(wishlist); // Saves the wishlist and the item due to cascade

      logger.info("Product with id {} added to wishlist", productId);
    }
  }

  @Override
  public void deleteFromWishlist(String role, Long productId) {
    logger.info("Deleting product with id: {}", productId);

    Wishlist wishlist = wishlistRepository.findByRole(role)
        .orElseThrow(() -> new RuntimeException("Wishlist not found for role: " + role));

    WishlistItem itemToRemove = wishlist.getItems().stream()
        .filter(item -> item.getProductId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Product not found in wishlist"));

    wishlist.removeItem(itemToRemove);
    wishlistRepository.save(wishlist); // Saves the updated wishlist

    logger.info("Product with id {} removed from wishlist", productId);
  }

  @Override
  public void updateWishlistItemQuantity(String role, Long productId, Integer quantity) {
    // Find the Wishlist based on the user's role
    Optional<Wishlist> optionalWishlist = wishlistRepository.findByRole(role);

    Wishlist wishlist = optionalWishlist
        .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for role " + role));

    // Find the WishlistItem within the Wishlist
    WishlistItem wishlistItem = wishlist.getItems().stream()
        .filter(item -> item.getProductId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found with productId " + productId));

    // Update the quantity of the item
    wishlistItem.setQuantity(quantity);

    // Save the updated wishlist item
    wishlistItemRepository.save(wishlistItem);
  }
}
