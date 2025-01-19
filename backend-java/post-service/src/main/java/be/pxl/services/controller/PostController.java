package be.pxl.services.controller;

import be.pxl.services.domain.UserRoles;
import be.pxl.services.domain.dto.CreatePostRequest;
import be.pxl.services.domain.dto.PostDTO;
import be.pxl.services.domain.dto.SubmitForReviewRequest;
import be.pxl.services.domain.dto.UpdatePostRequest;
import be.pxl.services.services.PostService;
import be.pxl.services.services.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postService.getPostById(postId);
        logger.info("Getting post by id: {}", postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/submit-for-review")
    public ResponseEntity<Void> submitPostForReview(
            @RequestHeader("Role") String role,
            @RequestBody SubmitForReviewRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't submit for review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.submitPostForReview(request);
        logger.info("Submitted post {} for review", request.getPostId());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestHeader("Role") String role, @RequestBody CreatePostRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't create post, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        PostDTO createdPost = postService.createPost(request);
        logger.info("Created new post");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@RequestHeader("Role") String role, @PathVariable Long id, @RequestBody UpdatePostRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't submit for review, FORBIDDEN MokaXMeUwu");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.updatePost(id, request);
        logger.info("Updated post {}", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unpublished")
    public ResponseEntity<List<PostDTO>> getUnpublishedPosts(@RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't submit for review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        logger.info("Getting unpublished posts");
        return ResponseEntity.ok(postService.getUnpublishedPosts());
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDTO>> getDraftPosts(@RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't submit for review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        logger.info("Getting draft posts");
        return ResponseEntity.ok(postService.getDraftPosts());
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPublishedPosts() {
        logger.info("Getting published posts");
        return ResponseEntity.ok(postService.getPublishedPosts());
    }
}

