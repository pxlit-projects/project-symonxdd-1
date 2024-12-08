package be.pxl.services.services;

import be.pxl.services.domain.dto.ProductDTO;
import be.pxl.services.domain.dto.ProductStockResponse;
import be.pxl.services.domain.dto.UpdateProductRequest;
import be.pxl.services.domain.dto.AddProductRequest;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

  List<ProductDTO> getAll();

  ProductDTO getProductById(Long productId);

  ProductDTO addProduct(AddProductRequest addProductRequest);

  ProductDTO updateProduct(Long productId, UpdateProductRequest updateProductRequest, String role);

  ProductStockResponse checkStock(Long productId);

  ProductDTO assignCategory(Long productId, Long categoryId);

  ProductDTO assignLabels(Long productId, List<Long> labelIds);

  ProductDTO findById(Long productId);

  List<String> getAllCategories();

  List<String> getAllLabels();
}
