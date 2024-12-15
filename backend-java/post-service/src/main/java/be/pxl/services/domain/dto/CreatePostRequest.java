package be.pxl.services.domain.dto;

import be.pxl.services.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    private String title;
    private String content;
    private String author;

    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setAuthor(this.author);
        return post;
    }
}

