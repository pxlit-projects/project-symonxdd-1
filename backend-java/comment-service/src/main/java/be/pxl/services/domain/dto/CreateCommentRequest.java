package be.pxl.services.domain.dto;

import be.pxl.services.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    private Long postId;
    private String content;
    private String author;

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setPostId(this.postId);
        comment.setContent(this.content);
        comment.setAuthor(this.author);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return comment;
    }
}

