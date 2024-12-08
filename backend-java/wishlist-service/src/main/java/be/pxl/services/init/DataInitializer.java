package be.pxl.services.init;

import be.pxl.services.domain.Wishlist;
import be.pxl.services.repository.WishlistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  public CommandLineRunner loadData(WishlistRepository wishlistRepository) {
    return args -> {

      // Check and populate wishlists for 'user' and 'admin' roles if they don't exist
      if (!wishlistRepository.findByRole("user").isPresent()) {
        wishlistRepository.save(Wishlist.builder()
            .role("user")
            .build());
      }

      if (!wishlistRepository.findByRole("admin").isPresent()) {
        wishlistRepository.save(Wishlist.builder()
            .role("admin")
            .build());
      }
    };
  }
}
