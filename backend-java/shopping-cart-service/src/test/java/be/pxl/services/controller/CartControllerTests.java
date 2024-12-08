package be.pxl.services.controller;

import be.pxl.services.domain.CartItem;
import be.pxl.services.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CartService cartService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void getAllItemsTest() throws Exception {
    List<CartItem> items = Arrays.asList(new CartItem(), new CartItem());
    given(cartService.getAllItems(anyString())).willReturn(items);

    mockMvc.perform(get("/api/carts/items")
        .header("Role", "user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(items.size()));
  }

  @Test
  public void addToCartTest() throws Exception {
    mockMvc.perform(post("/api/carts/products/add/1")
        .header("Role", "user")
        .param("quantity", "2")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isNoContent());

    Mockito.verify(cartService).addToCart("user", 1L, 2);
  }

  @Test
  public void addToCartForbiddenTest() throws Exception {
    mockMvc.perform(post("/api/carts/products/add/1")
        .header("Role", "admin")
        .param("quantity", "2")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isForbidden());
  }

  @Test
  public void addToCartBadRequestTest() throws Exception {
    // Simulate a RuntimeException when adding a product to the cart
    doThrow(new RuntimeException("Product not found"))
        .when(cartService).addToCart(anyString(), anyLong(), anyInt()); // Fix here: use anyInt()

    // Perform the POST request with the correct types
    mockMvc.perform(post("/api/carts/products/add/1")
        .header("Role", "user")
        .param("quantity", "2")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Product not found"));
  }

  @Test
  public void deleteFromCartTest() throws Exception {
    mockMvc.perform(delete("/api/carts/products/delete/1")
        .header("Role", "user"))
        .andExpect(status().isNoContent())
        .andExpect(content().string("Product removed from cart!"));

    Mockito.verify(cartService).deleteFromCart("user", 1L);
  }

  @Test
  public void deleteFromCartBadRequestTest() throws Exception {
    doThrow(new RuntimeException("Product not found"))
        .when(cartService).deleteFromCart(anyString(), anyLong());

    mockMvc.perform(delete("/api/carts/products/delete/1")
        .header("Role", "user"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Product not found"));
  }

  @Test
  public void updateCartItemQuantityTest() throws Exception {
    mockMvc.perform(put("/api/carts/items/1")
        .header("Role", "user")
        .param("quantity", "5"))
        .andExpect(status().isOk())
        .andExpect(content().string("Cart item quantity updated!"));

    Mockito.verify(cartService).updateCartItemQuantity("user", 1L, 5);
  }

  @Test
  public void deleteAllFromCartTest() throws Exception {
    mockMvc.perform(delete("/api/carts/products/delete/all")
        .header("Role", "user"))
        .andExpect(status().isNoContent())
        .andExpect(content().string("All products removed from cart!"));

    Mockito.verify(cartService).deleteAllFromCart("user");
  }

  @Test
  public void deleteAllFromCartBadRequestTest() throws Exception {
    doThrow(new RuntimeException("Failed to delete all products"))
        .when(cartService).deleteAllFromCart(anyString());

    mockMvc.perform(delete("/api/carts/products/delete/all")
        .header("Role", "user"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Failed to delete all products"));
  }
}