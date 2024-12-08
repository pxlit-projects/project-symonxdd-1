package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.dto.CreateCategoryRequest;
import be.pxl.services.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CategoryServiceTests {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllCategories_ShouldReturnListOfCategories() {
    // Arrange
    Category category = new Category();
    category.setName("Test Category");
    when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

    // Act
    List<Category> categories = categoryService.getAllCategories();

    // Assert
    assertEquals(1, categories.size());
    assertEquals("Test Category", categories.get(0).getName());
  }

  @Test
  void createCategory_ShouldSaveAndReturnCategory() {
    // Arrange
    CreateCategoryRequest request = new CreateCategoryRequest();
    request.setName("New Category");
    Category category = new Category();
    category.setName("New Category");
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    // Act
    Category createdCategory = categoryService.createCategory(request);

    // Assert
    assertEquals("New Category", createdCategory.getName());
  }
}