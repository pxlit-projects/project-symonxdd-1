package be.pxl.services.services;

import be.pxl.services.domain.Category;
import be.pxl.services.domain.Label;
import be.pxl.services.domain.dto.CreateCategoryRequest;
import be.pxl.services.domain.dto.CreateLabelRequest;
import be.pxl.services.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {
  private final LabelRepository labelRepository;

  public List<Label> getAllLabels() {
    return labelRepository.findAll();
  }

  public Label createLabel(CreateLabelRequest createLabelRequest) {
    Label label = new Label();
    label.setName(createLabelRequest.getName());

    return labelRepository.save(label);
  }
}
