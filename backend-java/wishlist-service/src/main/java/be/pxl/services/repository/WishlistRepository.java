package be.pxl.services.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import be.pxl.services.domain.Wishlist;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

  Optional<Wishlist> findByRole(String role);

  List<Wishlist> findAllByRole(String role);
}
