package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.dto.CreateCategoryRequest;
import be.pxl.services.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category createCategory(CreateCategoryRequest createCategoryRequest) {
    Category category = new Category();
    category.setName(createCategoryRequest.getName());

    return categoryRepository.save(category);
  }
}