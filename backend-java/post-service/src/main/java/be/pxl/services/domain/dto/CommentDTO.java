package be.pxl.services.domain.dto;

import lombok.Data;

@Data
public class CommentDTO {
  private String content;
  private String created;
  private String author;
}
