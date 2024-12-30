package be.pxl.services.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDTO {
  private Long id;
  private String content;
  private LocalDateTime createdAt;
  private String author;
}
