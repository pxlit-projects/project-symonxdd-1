package be.pxl.services.services;

import be.pxl.services.domain.Cart;
import be.pxl.services.repository.CartRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner loadData(CartRepository cartRepository) {
    return args -> {

      // Check and populate carts for 'user' and 'admin' roles if they don't exist
      if (!cartRepository.findByRole("user").isPresent()) {
        cartRepository.save(Cart.builder()
            .role("user")
            .build());
      }

      if (!cartRepository.findByRole("admin").isPresent()) {
        cartRepository.save(Cart.builder()
            .role("admin")
            .build());
      }
    };
  }
}
