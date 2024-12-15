package be.pxl.services.domain.dto;

import be.pxl.services.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    private String title;
    private String content;

    public void updateEntity(Post post) {
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setUpdatedAt(LocalDateTime.now());
    }
}

