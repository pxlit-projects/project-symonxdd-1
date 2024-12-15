package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    // used by the CommentServiceClient in PostService's PostServiceImpl
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}