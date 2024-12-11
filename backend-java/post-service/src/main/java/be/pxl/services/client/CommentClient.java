package be.pxl.services.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import be.pxl.services.domain.dto.CommentDTO;

@FeignClient(name = "comment-service")
public interface CommentClient {

  @GetMapping("/api/comments/{postId}")
  List<CommentDTO> getCommentsByPostId(@PathVariable Long postId);
}
