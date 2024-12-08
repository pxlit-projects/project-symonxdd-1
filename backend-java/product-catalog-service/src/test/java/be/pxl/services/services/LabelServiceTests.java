package be.pxl.services.services;

import be.pxl.services.domain.Label;
import be.pxl.services.domain.dto.CreateLabelRequest;
import be.pxl.services.repository.LabelRepository;
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

public class LabelServiceTests {

  @Mock
  private LabelRepository labelRepository;

  @InjectMocks
  private LabelService labelService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllLabels_ShouldReturnListOfLabels() {
    // Arrange
    Label label = new Label();
    label.setName("Test Label");
    when(labelRepository.findAll()).thenReturn(Collections.singletonList(label));

    // Act
    List<Label> labels = labelService.getAllLabels();

    // Assert
    assertEquals(1, labels.size());
    assertEquals("Test Label", labels.get(0).getName());
  }

  @Test
  void createLabel_ShouldSaveAndReturnLabel() {
    // Arrange
    CreateLabelRequest request = new CreateLabelRequest();
    request.setName("New Label");
    Label label = new Label();
    label.setName("New Label");
    when(labelRepository.save(any(Label.class))).thenReturn(label);

    // Act
    Label createdLabel = labelService.createLabel(request);

    // Assert
    assertEquals("New Label", createdLabel.getName());
  }
}
