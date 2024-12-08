package be.pxl.services.services;

import be.pxl.services.client.ProductCatalogClient;
import be.pxl.services.domain.Cart;
import be.pxl.services.domain.CartItem;
import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.repository.CartRepository;
import be.pxl.services.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CartServiceTests {

  @Mock
  private CartRepository cartRepository;

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private ProductCatalogClient productCatalogClient;

  @InjectMocks
  private CartServiceImpl cartService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllItems_ShouldReturnCartItems() {
    // Arrange
    Cart cart = new Cart();
    CartItem item = new CartItem();
    cart.addItem(item);
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    // Act
    var items = cartService.getAllItems("user");

    // Assert
    assertNotNull(items);
    assertEquals(1, items.size());
  }

  @Test
  void getAllItems_ShouldThrowExceptionWhenCartNotFound() {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      cartService.getAllItems("user");
    });
    assertEquals("Cart not found for role: user", thrown.getMessage());
  }

  @Test
  void addToCart_ShouldAddProductToCart() {
    // Arrange
    Cart cart = new Cart();
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Product");
    productDTO.setPrice(100);
    when(productCatalogClient.getProductById(1L)).thenReturn(productDTO);

    // Act
    cartService.addToCart("user", 1L, 2);

    // Assert
    assertEquals(1, cart.getItems().size());
    CartItem item = cart.getItems().iterator().next();
    assertEquals(1L, item.getProductId());
    assertEquals("Product", item.getProductName());
    assertEquals(100, item.getProductPrice());
    assertEquals(2, item.getQuantity());
  }

  @Test
  void addToCart_ShouldThrowExceptionWhenCartNotFound() {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.empty());

    // Act & Assert
    ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
      cartService.addToCart("user", 1L, 2);
    });
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    assertEquals("Cart not found for role: user", thrown.getReason());
  }

  @Test
  void addToCart_ShouldThrowExceptionWhenProductNotFound() {
    // Arrange
    Cart cart = new Cart();
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));
    when(productCatalogClient.getProductById(1L)).thenReturn(null);

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      cartService.addToCart("user", 1L, 2);
    });
    assertEquals("Product not found", thrown.getMessage());
  }

  @Test
  void deleteFromCart_ShouldRemoveProductFromCart() {
    // Arrange
    Cart cart = new Cart();
    CartItem item = new CartItem();
    item.setProductId(1L);
    cart.addItem(item);
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    // Act
    cartService.deleteFromCart("user", 1L);

    // Assert
    assertTrue(cart.getItems().isEmpty());
  }

  @Test
  void deleteFromCart_ShouldThrowExceptionWhenCartNotFound() {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      cartService.deleteFromCart("user", 1L);
    });
    assertEquals("Cart not found for role: user", thrown.getMessage());
  }

  @Test
  void deleteFromCart_ShouldThrowExceptionWhenItemNotFound() {
    // Arrange
    Cart cart = new Cart();
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      cartService.deleteFromCart("user", 1L);
    });
    assertEquals("Product not found in cart", thrown.getMessage());
  }

  @Test
  void deleteAllFromCart_ShouldRemoveAllProductsFromCart() {
    // Arrange
    Cart cart = new Cart();
    cart.addItem(new CartItem());
    cart.addItem(new CartItem());
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    // Act
    cartService.deleteAllFromCart("user");

    // Assert
    assertTrue(cart.getItems().isEmpty());
  }

  @Test
  void updateCartItemQuantity_ShouldUpdateQuantity() {
    // Arrange
    Cart cart = new Cart();
    CartItem item = new CartItem();
    item.setId(1L);
    item.setQuantity(1);
    cart.addItem(item);
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));
    when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    cartService.updateCartItemQuantity("user", 1L, 5);

    // Assert
    assertEquals(5, item.getQuantity());
    verify(cartItemRepository).save(item);
  }

  @Test
  void updateCartItemQuantity_ShouldThrowExceptionWhenCartNotFound() {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.empty());

    // Act & Assert
    ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
      cartService.updateCartItemQuantity("user", 1L, 5);
    });
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    assertEquals("Cart not found for role: user", thrown.getReason());
  }

  @Test
  void updateCartItemQuantity_ShouldThrowExceptionWhenItemNotFound() {
    // Arrange
    Cart cart = new Cart();
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(cart));

    // Act & Assert
    ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
      cartService.updateCartItemQuantity("user", 1L, 5);
    });
    assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    assertEquals("CartItem not found", thrown.getReason());
  }
}