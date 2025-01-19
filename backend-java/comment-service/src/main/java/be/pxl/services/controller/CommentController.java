package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CreateCommentRequest request) {
        logger.info("Adding a comment...");
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Role") String role, @PathVariable Long id) {
        logger.info("Deleting a comment...");
        commentService.deleteComment(id, role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long id,
            @RequestBody UpdateCommentRequest request,
            @RequestHeader("Role") String role) {
        logger.info("Updating a comment...");
        return ResponseEntity.ok(commentService.updateComment(id, request, role));
    }

    // used by the CommentServiceClient in PostService's PostServiceImpl
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        logger.info("Getting comments by post ID...(" + postId + ")");
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}