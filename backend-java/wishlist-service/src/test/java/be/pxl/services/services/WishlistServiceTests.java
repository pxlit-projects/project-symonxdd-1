package be.pxl.services.services;

import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repository.WishlistItemRepository;
import be.pxl.services.client.ProductCatalogClient;
import be.pxl.services.domain.Wishlist;
import be.pxl.services.domain.WishlistItem;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WishlistServiceTests {

  @Mock
  private WishlistRepository wishlistRepository;

  @Mock
  private WishlistItemRepository wishlistItemRepository;

  @Mock
  private ProductCatalogClient productCatalogClient;

  @InjectMocks
  private WishlistServiceImpl wishlistServiceImpl;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetAllItems_WishlistFound() {
    // Arrange
    String role = "user";
    Wishlist wishlist = new Wishlist();
    wishlist.addItem(new WishlistItem()); // Adding a mock item
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));

    // Act
    List<WishlistItem> items = wishlistServiceImpl.getAllItems(role);

    // Assert
    assertNotNull(items);
    assertEquals(1, items.size());
    verify(wishlistRepository, times(1)).findByRole(role);
  }

  @Test
  public void testGetAllItems_WishlistNotFound() {
    // Arrange
    String role = "user";
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> wishlistServiceImpl.getAllItems(role));
    assertEquals("Cart not found for role: " + role, thrown.getMessage());
  }

  @Test
  public void testAddToWishlist_ProductNotFound() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Integer quantity = 2;
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(new Wishlist()));
    when(productCatalogClient.getProductById(productId)).thenReturn(null);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> wishlistServiceImpl.addToWishlist(role, productId, quantity));
    assertEquals("Product not found", thrown.getMessage());
  }

  @Test
  public void testAddToWishlist_ProductExists() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Integer quantity = 2;
    ProductDTO productDTO = new ProductDTO(productId, "Product Name", 100);
    Wishlist wishlist = new Wishlist();
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));
    when(productCatalogClient.getProductById(productId)).thenReturn(productDTO);

    // Act
    wishlistServiceImpl.addToWishlist(role, productId, quantity);

    // Assert
    assertEquals(1, wishlist.getItems().size());
    // Convert Set to List to access elements by index
    List<WishlistItem> items = wishlist.getItems().stream().collect(Collectors.toList());
    WishlistItem item = items.get(0);
    assertEquals(productId, item.getProductId());
    assertEquals("Product Name", item.getProductName());
    assertEquals(100, item.getProductPrice());
    assertEquals(quantity, item.getQuantity());
    verify(wishlistRepository, times(1)).save(wishlist);
  }

  @Test
  public void testDeleteFromWishlist_ProductNotFound() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Wishlist wishlist = new Wishlist();
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class,
        () -> wishlistServiceImpl.deleteFromWishlist(role, productId));
    assertEquals("Product not found in wishlist", thrown.getMessage());
  }

  @Test
  public void testDeleteFromWishlist_Success() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Wishlist wishlist = new Wishlist();
    WishlistItem item = new WishlistItem();
    item.setProductId(productId);
    wishlist.addItem(item);
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));

    // Act
    wishlistServiceImpl.deleteFromWishlist(role, productId);

    // Assert
    assertTrue(wishlist.getItems().isEmpty());
    verify(wishlistRepository, times(1)).save(wishlist);
  }

  @Test
  public void testUpdateWishlistItemQuantity_ItemNotFound() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Integer quantity = 5;
    Wishlist wishlist = new Wishlist();
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));

    // Act & Assert
    ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
        () -> wishlistServiceImpl.updateWishlistItemQuantity(role, productId, quantity));
    assertEquals("Wishlist item not found with productId " + productId, thrown.getMessage());
  }

  @Test
  public void testUpdateWishlistItemQuantity_Success() {
    // Arrange
    String role = "user";
    Long productId = 1L;
    Integer quantity = 5;
    Wishlist wishlist = new Wishlist();
    WishlistItem item = new WishlistItem();
    item.setProductId(productId);
    wishlist.addItem(item);
    when(wishlistRepository.findByRole(role)).thenReturn(Optional.of(wishlist));

    // Act
    wishlistServiceImpl.updateWishlistItemQuantity(role, productId, quantity);

    // Assert
    assertEquals(quantity, item.getQuantity());
    verify(wishlistItemRepository, times(1)).save(item);
  }
}