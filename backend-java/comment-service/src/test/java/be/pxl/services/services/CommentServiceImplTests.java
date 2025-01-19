package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.exception.UnauthorizedActionException;
import be.pxl.services.repo.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

// if using 'testcontainers'
// use both annots: @ActiveProfiles("testcontainers") and @Testcontainers

//@ActiveProfiles("h2")
@ActiveProfiles("testcontainers")
@Testcontainers
@SpringBootTest
class CommentServiceImplTests {

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
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll(); // Ensure a clean state for the database before each test
    }

    @Test
    void testAddComment() {
        CreateCommentRequest request = CreateCommentRequest.builder()
                .postId(1L)
                .content("This is a test comment.")
                .author("John Doe")
                .build();

        CommentDTO result = commentService.addComment(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(request.getContent());
        assertThat(result.getAuthor()).isEqualTo(request.getAuthor());

        List<Comment> savedComments = commentRepository.findAll();
        assertThat(savedComments).hasSize(1);
        assertThat(savedComments.get(0).getContent()).isEqualTo(request.getContent());
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange: Create and save a comment with an author
        Comment comment = Comment.builder()
                .postId(1L)
                .content("Sample Comment")
                .author("ADMIN") // Author must match the role passed to the service
                .build();

        comment = commentRepository.save(comment);

        // Act: Attempt to delete the comment with the matching role
        commentService.deleteComment(comment.getId(), "ADMIN");

        // Assert: Ensure the comment is deleted
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }


    @Test
    void testDeleteComment_CommentNotFound() {
        assertThatThrownBy(() -> commentService.deleteComment(1L, "ADMIN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Comment not found with id: 1");
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange: Create and save a comment with an author
        Comment comment = Comment.builder()
                .postId(1L)
                .content("Original Content")
                .author("John Doe") // Author must match the role passed to the service
                .build();

        comment = commentRepository.save(comment);

        // Create an update request
        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("Updated Content")
                .build();

        // Act: Attempt to update the comment with the matching role
        CommentDTO result = commentService.updateComment(comment.getId(), request, "John Doe");

        // Assert: Ensure the comment was updated
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Updated Content");

        // Verify the update in the repository
        Comment updatedComment = commentRepository.findById(comment.getId()).orElseThrow();
        assertThat(updatedComment.getContent()).isEqualTo("Updated Content");
    }


    @Test
    void testUpdateComment_CommentNotFound() {
        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("Updated Content")
                .build();

        assertThatThrownBy(() -> commentService.updateComment(1L, request, "ADMIN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Comment not found" + 1L);
    }

    @Test
    void testGetCommentsByPostId() {
        Comment comment1 = Comment.builder()
                .postId(1L)
                .content("Comment 1")
                .author("John Doe")
                .build();

        Comment comment2 = Comment.builder()
                .postId(1L)
                .content("Comment 2")
                .author("Jane Smith")
                .build();

        commentRepository.saveAll(List.of(comment1, comment2));

        List<CommentDTO> result = commentService.getCommentsByPostId(1L);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getContent()).isEqualTo("Comment 1");
        assertThat(result.get(1).getContent()).isEqualTo("Comment 2");
    }

    @Test
    void testUpdateComment_Unauthorized() {
        // Arrange: Create and save a comment with a different author
        Comment comment = Comment.builder()
                .postId(1L)
                .content("Original Content")
                .author("Jane Doe")
                .build();

        comment = commentRepository.save(comment);

        // Create an update request
        UpdateCommentRequest request = UpdateCommentRequest.builder()
                .content("Updated Content")
                .build();

        // Act & Assert: Attempt to update with a mismatched role
        Comment finalComment = comment;
        assertThrows(UnauthorizedActionException.class, () -> {
            commentService.updateComment(finalComment.getId(), request, "ADMIN");
        });
    }

    @Test
    void testDeleteComment_Unauthorized() {
        // Arrange: Create and save a comment with a different author
        Comment comment = Comment.builder()
                .postId(1L)
                .content("Sample Comment")
                .author("Jane Doe")
                .build();

        comment = commentRepository.save(comment);

        // Act & Assert: Attempt to delete with a mismatched role
        Comment finalComment = comment;
        assertThrows(UnauthorizedActionException.class, () -> {
            commentService.deleteComment(finalComment.getId(), "ADMIN");
        });
    }
}

