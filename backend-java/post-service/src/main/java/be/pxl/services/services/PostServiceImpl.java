package be.pxl.services.services;

import be.pxl.services.client.CommentServiceClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.*;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repo.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentServiceClient commentServiceClient;

    @Override
    public PostDTO createPost(CreatePostRequest request) {
        Post post = request.toEntity();
        post.setStatus(PostStatus.DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        return new PostDTO(postRepository.save(post));
    }

    @Override
    public void submitPostForReview(SubmitForReviewRequest submitForReviewRequest) {
        Optional<Post> postOptional = postRepository.findById(submitForReviewRequest.getPostId());
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with id: " + submitForReviewRequest.getPostId());
        }
        Post post = postOptional.get();
        post.setStatus(PostStatus.PENDING_REVIEW);
        postRepository.save(post);
    }

    @Override
    public PostDTO getPostById(Long id) {
        // Fetch the post from the database
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        Post post = postOptional.get();

        // Fetch comments for the post from commentservice
        List<CommentDTO> comments = commentServiceClient.getCommentsByPostId(id);

        // Map Post entity and comments to PostDTO
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .comments(comments)
                .createdAt(post.getCreatedAt())
                .author(post.getAuthor())
                .status(post.getStatus())
                .build();
    }

    @Override
    public PostDTO updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        request.updateEntity(post);
        return new PostDTO(postRepository.save(post));
    }

    @Override
    public List<PostDTO> getUnpublishedPosts() {
        return postRepository.findAll().stream()
                .filter(post -> post.getStatus() == PostStatus.PENDING_REVIEW || post.getStatus() == PostStatus.REJECTED)
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getDraftPosts() {
        return postRepository.findByStatus(PostStatus.DRAFT).stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getPublishedPosts() {
        return postRepository.findByStatus(PostStatus.APPROVED).stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }
}


