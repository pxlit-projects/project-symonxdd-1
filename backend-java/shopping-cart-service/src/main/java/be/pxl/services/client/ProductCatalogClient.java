package be.pxl.services.client;

import be.pxl.services.domain.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-catalog-service")
public interface ProductCatalogClient {

  @GetMapping("/api/products/{productId}")
  ProductDTO getProductById(@PathVariable Long productId);
}
