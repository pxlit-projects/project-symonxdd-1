package be.pxl.services.domain.dto;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private PostStatus status;
    private LocalDateTime createdAt;
    private List<CommentDTO> comments;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getAuthor();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
    }
}


