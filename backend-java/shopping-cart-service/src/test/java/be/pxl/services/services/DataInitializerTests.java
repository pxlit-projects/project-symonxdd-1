package be.pxl.services.services;

import be.pxl.services.domain.Cart;
import be.pxl.services.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataInitializerTests {

  @Mock
  private CartRepository cartRepository;

  @InjectMocks
  private DataInitializer dataInitializer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void loadData_ShouldInitializeCartsForUserAndAdmin() throws Exception {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.empty());
    when(cartRepository.findByRole("admin")).thenReturn(Optional.empty());

    // Act
    CommandLineRunner runner = dataInitializer.loadData(cartRepository);
    runner.run();

    // Assert
    verify(cartRepository).findByRole("user");
    verify(cartRepository).findByRole("admin");
    verify(cartRepository).save(argThat(cart -> "user".equals(cart.getRole())));
    verify(cartRepository).save(argThat(cart -> "admin".equals(cart.getRole())));
  }

  @Test
  void loadData_ShouldNotInitializeExistingCarts() throws Exception {
    // Arrange
    when(cartRepository.findByRole("user")).thenReturn(Optional.of(new Cart()));
    when(cartRepository.findByRole("admin")).thenReturn(Optional.of(new Cart()));

    // Act
    CommandLineRunner runner = dataInitializer.loadData(cartRepository);
    runner.run();

    // Assert
    verify(cartRepository, never()).save(any(Cart.class));
  }
}
