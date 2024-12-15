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
public class CommentDTO {
    private Long id;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPostId();
        this.content = comment.getContent();
        this.author = comment.getAuthor();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}

