package be.pxl.services.init;

import be.pxl.services.domain.Wishlist;
import be.pxl.services.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataInitializerTests {

  @Mock
  private WishlistRepository wishlistRepository;

  @InjectMocks
  private DataInitializer dataInitializer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testLoadData() throws Exception {
    // Arrange
    when(wishlistRepository.findByRole("user")).thenReturn(Optional.empty());
    when(wishlistRepository.findByRole("admin")).thenReturn(Optional.empty());

    // Act
    CommandLineRunner commandLineRunner = dataInitializer.loadData(wishlistRepository);
    commandLineRunner.run(); // Execute the CommandLineRunner

    // Assert
    verify(wishlistRepository, times(1)).save(argThat(wishlist -> "user".equals(wishlist.getRole())));
    verify(wishlistRepository, times(1)).save(argThat(wishlist -> "admin".equals(wishlist.getRole())));
  }

  @Test
  public void testLoadData_WithExistingUserWishlist() throws Exception {
    // Arrange
    when(wishlistRepository.findByRole("user")).thenReturn(Optional.of(new Wishlist()));
    when(wishlistRepository.findByRole("admin")).thenReturn(Optional.empty());

    // Act
    CommandLineRunner commandLineRunner = dataInitializer.loadData(wishlistRepository);
    commandLineRunner.run(); // Execute the CommandLineRunner

    // Assert
    verify(wishlistRepository, never()).save(argThat(wishlist -> "user".equals(wishlist.getRole())));
    verify(wishlistRepository, times(1)).save(argThat(wishlist -> "admin".equals(wishlist.getRole())));
  }

  @Test
  public void testLoadData_WithExistingAdminWishlist() throws Exception {
    // Arrange
    when(wishlistRepository.findByRole("user")).thenReturn(Optional.empty());
    when(wishlistRepository.findByRole("admin")).thenReturn(Optional.of(new Wishlist()));

    // Act
    CommandLineRunner commandLineRunner = dataInitializer.loadData(wishlistRepository);
    commandLineRunner.run(); // Execute the CommandLineRunner

    // Assert
    verify(wishlistRepository, times(1)).save(argThat(wishlist -> "user".equals(wishlist.getRole())));
    verify(wishlistRepository, never()).save(argThat(wishlist -> "admin".equals(wishlist.getRole())));
  }

  @Test
  public void testLoadData_WithExistingWishlists() throws Exception {
    // Arrange
    when(wishlistRepository.findByRole("user")).thenReturn(Optional.of(new Wishlist()));
    when(wishlistRepository.findByRole("admin")).thenReturn(Optional.of(new Wishlist()));

    // Act
    CommandLineRunner commandLineRunner = dataInitializer.loadData(wishlistRepository);
    commandLineRunner.run(); // Execute the CommandLineRunner

    // Assert
    verify(wishlistRepository, never()).save(any(Wishlist.class));
  }
}