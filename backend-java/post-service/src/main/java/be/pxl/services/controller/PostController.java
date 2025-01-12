package be.pxl.services.controller;

import be.pxl.services.domain.UserRoles;
import be.pxl.services.domain.dto.CreatePostRequest;
import be.pxl.services.domain.dto.PostDTO;
import be.pxl.services.domain.dto.SubmitForReviewRequest;
import be.pxl.services.domain.dto.UpdatePostRequest;
import be.pxl.services.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/submit-for-review")
    public ResponseEntity<Void> submitPostForReview(
            @RequestHeader("Role") String role,
            @RequestBody SubmitForReviewRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.submitPostForReview(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestHeader("Role") String role, @RequestBody CreatePostRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@RequestHeader("Role") String role, @PathVariable Long id, @RequestBody UpdatePostRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.updatePost(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unpublished")
    public ResponseEntity<List<PostDTO>> getUnpublishedPosts(@RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(postService.getUnpublishedPosts());
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDTO>> getDraftPosts(@RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(postService.getDraftPosts());
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPublishedPosts() {
        return ResponseEntity.ok(postService.getPublishedPosts());
    }
}

