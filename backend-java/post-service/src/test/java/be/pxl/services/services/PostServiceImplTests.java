package be.pxl.services.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;

import be.pxl.services.client.CommentServiceClient;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.*;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repo.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// if using 'testcontainers'
// use both annots: @ActiveProfiles("testcontainers") and @Testcontainers

//@ActiveProfiles("h2")
@ActiveProfiles("testcontainers")
@Testcontainers
@SpringBootTest
class PostServiceImplTests {

    @Container
    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private PostService postService;

    @MockBean
    private CommentServiceClient commentServiceClient;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll(); // Ensure a clean state for the database before each test
    }

    @Test
    void testCreatePost() {
        // Arrange
        CreatePostRequest request = new CreatePostRequest("Title", "Content", "Author");

        // Act
        PostDTO result = postService.createPost(request);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertEquals("Author", result.getAuthor());
        assertEquals(PostStatus.DRAFT, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getId());
    }

    @Test
    void testSubmitPostForReview() {
        // Arrange
        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        post.setAuthor("Author");
        post.setStatus(PostStatus.DRAFT);
        post = postRepository.save(post);

        SubmitForReviewRequest request = new SubmitForReviewRequest(post.getId());

        // Act
        postService.submitPostForReview(request);

        // Assert
        Post updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(PostStatus.PENDING_REVIEW, updatedPost.getStatus());
    }

    @Test
    void testGetPostById() {
        // Arrange
        Post post = new Post();
        post.setTitle("Title");
        post.setContent("Content");
        post.setAuthor("Author");
        post.setStatus(PostStatus.DRAFT);
        post = postRepository.save(post);

        when(commentServiceClient.getCommentsByPostId(post.getId())).thenReturn(List.of());

        // Act
        PostDTO result = postService.getPostById(post.getId());

        // Assert
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertEquals("Author", result.getAuthor());
        assertEquals(PostStatus.DRAFT, result.getStatus());
    }

    @Test
    void testUpdatePost() {
        // Arrange
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old Content");
        post.setAuthor("Author");
        post.setStatus(PostStatus.DRAFT);
        post = postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("New Title", "New Content");

        // Act
        PostDTO result = postService.updatePost(post.getId(), request);

        // Assert
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals("New Content", result.getContent());
    }

    @Test
    void testGetUnpublishedPosts() {
        // Arrange
        Post pending = new Post();
        pending.setTitle("Pending Title");
        pending.setContent("Content");
        pending.setAuthor("Author");
        pending.setStatus(PostStatus.PENDING_REVIEW);
        postRepository.save(pending);

        Post rejected = new Post();
        rejected.setTitle("Rejected Title");
        rejected.setContent("Content");
        rejected.setAuthor("Author");
        rejected.setStatus(PostStatus.REJECTED);
        postRepository.save(rejected);

        Post draft = new Post();
        draft.setTitle("Draft Title");
        draft.setContent("Content");
        draft.setAuthor("Author");
        draft.setStatus(PostStatus.DRAFT);
        postRepository.save(draft);

        // Act
        List<PostDTO> result = postService.getUnpublishedPosts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(post -> post.getStatus() == PostStatus.PENDING_REVIEW));
        assertTrue(result.stream().anyMatch(post -> post.getStatus() == PostStatus.REJECTED));
    }

    @Test
    void testGetDraftPosts() {
        // Arrange
        Post draft = new Post();
        draft.setTitle("Draft Title");
        draft.setContent("Content");
        draft.setAuthor("Author");
        draft.setStatus(PostStatus.DRAFT);
        postRepository.save(draft);

        Post nonDraft = new Post();
        nonDraft.setTitle("Non-Draft Title");
        nonDraft.setContent("Content");
        nonDraft.setAuthor("Author");
        nonDraft.setStatus(PostStatus.APPROVED);
        postRepository.save(nonDraft);

        // Act
        List<PostDTO> result = postService.getDraftPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PostStatus.DRAFT, result.get(0).getStatus());
    }

    @Test
    void testGetPublishedPosts() {
        // Arrange
        Post published = new Post();
        published.setTitle("Published Title");
        published.setContent("Content");
        published.setAuthor("Author");
        published.setStatus(PostStatus.APPROVED);
        postRepository.save(published);

        Post nonPublished = new Post();
        nonPublished.setTitle("Non-Published Title");
        nonPublished.setContent("Content");
        nonPublished.setAuthor("Author");
        nonPublished.setStatus(PostStatus.DRAFT);
        postRepository.save(nonPublished);

        // Act
        List<PostDTO> result = postService.getPublishedPosts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(PostStatus.APPROVED, result.get(0).getStatus());
    }
}
