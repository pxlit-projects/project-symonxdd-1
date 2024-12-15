package be.pxl.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import be.pxl.services.domain.dto.CommentDTO;

@FeignClient(name = "comment-service")
public interface CommentServiceClient {

    @GetMapping("/api/comments/{postId}")
    List<CommentDTO> getCommentsByPostId(@PathVariable Long postId);
}
