package be.pxl.services.controller;

import be.pxl.services.domain.WishlistItem;
import be.pxl.services.services.WishlistService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WishlistController.class)
public class WishlistControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WishlistService wishlistService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void getAllItemsTest() throws Exception {
    List<WishlistItem> items = Arrays.asList(new WishlistItem(), new WishlistItem());
    given(wishlistService.getAllItems(anyString())).willReturn(items);

    mockMvc.perform(get("/api/wishlist/items")
        .header("Role", "user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(items.size()));
  }

  @Test
  public void addToWishlistTest() throws Exception {
    mockMvc.perform(post("/api/wishlist/add/1")
        .header("Role", "user")
        .param("quantity", "2"))
        .andExpect(status().isOk());

    Mockito.verify(wishlistService).addToWishlist("user", 1L, 2);
  }

  @Test
  public void deleteFromWishlistTest() throws Exception {
    mockMvc.perform(delete("/api/wishlist/products/delete/1")
        .header("Role", "user"))
        .andExpect(status().isNoContent())
        .andExpect(content().string("Product removed from wishlist!"));

    Mockito.verify(wishlistService).deleteFromWishlist("user", 1L);
  }

  @Test
  public void deleteFromWishlistBadRequestTest() throws Exception {
    // Mock the behavior to throw an exception when deleteFromWishlist is called
    Mockito.doThrow(new RuntimeException("Product not found"))
        .when(wishlistService).deleteFromWishlist(anyString(), anyLong());

    mockMvc.perform(delete("/api/wishlist/products/delete/1")
        .header("Role", "user"))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Product not found"));
  }

  @Test
  public void updateWishlistItemQuantityTest() throws Exception {
    mockMvc.perform(put("/api/wishlist/1")
        .header("Role", "user")
        .param("quantity", "5"))
        .andExpect(status().isOk());

    Mockito.verify(wishlistService).updateWishlistItemQuantity("user", 1L, 5);
  }

  @Test
  public void updateWishlistItemQuantityForbiddenTest() throws Exception {
    mockMvc.perform(put("/api/wishlist/1")
        .header("Role", "admin")
        .param("quantity", "5"))
        .andExpect(status().isForbidden());
  }
}
