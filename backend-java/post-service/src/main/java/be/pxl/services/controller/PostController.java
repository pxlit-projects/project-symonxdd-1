package be.pxl.services.controller;

import be.pxl.services.domain.dto.CreatePostRequest;
import be.pxl.services.domain.dto.PostDTO;
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

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest request) {
        postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest request) {
        postService.updatePost(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unpublished")
    public ResponseEntity<List<PostDTO>> getUnpublishedPosts() {
        return ResponseEntity.ok(postService.getUnpublishedPosts());
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPublishedPosts() {
        return ResponseEntity.ok(postService.getPublishedPosts());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostDTO>> filterPosts(@RequestParam String category, @RequestParam String author) {
        return ResponseEntity.ok(postService.filterPosts(category, author));
    }
}

