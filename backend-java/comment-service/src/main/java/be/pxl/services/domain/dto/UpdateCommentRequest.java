package be.pxl.services.domain.dto;

import be.pxl.services.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
    private String content;

    public void updateEntity(Comment comment) {
        comment.setContent(this.content);
        comment.setUpdatedAt(LocalDateTime.now());
    }
}