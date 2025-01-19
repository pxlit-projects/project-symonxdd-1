package be.pxl.services.services;

import be.pxl.services.domain.Review;
import be.pxl.services.repo.ReviewRepository;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import be.pxl.services.domain.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// if using 'testcontainers'
// use both annots: @ActiveProfiles("testcontainers") and @Testcontainers

//@ActiveProfiles("h2")
@ActiveProfiles("testcontainers")
@Testcontainers
@SpringBootTest
class ReviewServiceImplTests {

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
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll(); // Ensure a clean state for the database before each test
    }

    @Test
    void testApproveReview_Success() {
        // Arrange
        ReviewRequest request = ReviewRequest.builder()
                .postId(1L)
                .remarks("Great post!")
                .build();

        // Act
        reviewService.approveReview(request);

        // Assert
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(1, reviews.size(), "There should be one review in the database.");
        Review savedReview = reviews.get(0);
        assertEquals(request.getPostId(), savedReview.getPostId());
        assertEquals(request.getRemarks(), savedReview.getRemarks());

        // Capture arguments sent to RabbitTemplate
        ArgumentCaptor<NotificationDTO> notificationCaptor = ArgumentCaptor.forClass(NotificationDTO.class);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("review_exchange"), notificationCaptor.capture());

        NotificationDTO capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification, "Notification should not be null.");
        assertEquals(1L, capturedNotification.getPostId(), "Post ID should match.");
        assertEquals("Great post!", capturedNotification.getMessage(), "Message should match.");
        assertTrue(capturedNotification.getApproved(), "Approved should be true.");

        // Validate createdAt separately due to potential precision differences
        assertNotNull(capturedNotification.getCreatedAt(), "CreatedAt should not be null.");
        assertEquals(savedReview.getCreatedAt().toLocalDate(), capturedNotification.getCreatedAt().toLocalDate(), "CreatedAt date should match.");
    }

    @Test
    void testRejectReview_Success() {
        // Arrange
        ReviewRequest request = ReviewRequest.builder()
                .postId(2L)
                .remarks("Needs improvement.")
                .build();

        // Act
        reviewService.rejectReview(request);

        // Assert
        // Verify the review was saved in the database
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(1, reviews.size(), "There should be one review in the database.");
        Review savedReview = reviews.get(0);
        assertEquals(request.getPostId(), savedReview.getPostId(), "Post ID should match.");
        assertEquals(request.getRemarks(), savedReview.getRemarks(), "Remarks should match.");

        // Capture arguments sent to RabbitTemplate
        ArgumentCaptor<NotificationDTO> notificationCaptor = ArgumentCaptor.forClass(NotificationDTO.class);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("review_exchange"), notificationCaptor.capture());

        NotificationDTO capturedNotification = notificationCaptor.getValue();

        // Assert individual fields of the captured notification
        assertNotNull(capturedNotification, "Notification should not be null.");
        assertEquals(2L, capturedNotification.getPostId(), "Post ID should match.");
        assertEquals("Needs improvement.", capturedNotification.getMessage(), "Message should match.");
        assertFalse(capturedNotification.getApproved(), "Approved should be false.");

        // Validate createdAt separately due to potential precision differences
        assertNotNull(capturedNotification.getCreatedAt(), "CreatedAt should not be null.");
        assertEquals(savedReview.getCreatedAt().toLocalDate(), capturedNotification.getCreatedAt().toLocalDate(), "CreatedAt date should match.");
    }

    @Test
    void testDatabaseIntegrity_WhenMultipleOperations() {
        // Arrange
        ReviewRequest approveRequest = ReviewRequest.builder()
                .postId(3L)
                .remarks("Well done!")
                .build();

        ReviewRequest rejectRequest = ReviewRequest.builder()
                .postId(4L)
                .remarks("Not up to the mark.")
                .build();

        // Act
        reviewService.approveReview(approveRequest);
        reviewService.rejectReview(rejectRequest);

        // Assert
        List<Review> reviews = reviewRepository.findAll();
        assertEquals(2, reviews.size(), "There should be two reviews in the database.");

        // Ensure the reviews are correctly retrieved
        Review approvedReview = reviews.stream()
                .filter(r -> r.getPostId().equals(approveRequest.getPostId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Approved review not found."));

        Review rejectedReview = reviews.stream()
                .filter(r -> r.getPostId().equals(rejectRequest.getPostId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Rejected review not found."));

        assertEquals(approveRequest.getPostId(), approvedReview.getPostId());
        assertEquals(approveRequest.getRemarks(), approvedReview.getRemarks());

        assertEquals(rejectRequest.getPostId(), rejectedReview.getPostId());
        assertEquals(rejectRequest.getRemarks(), rejectedReview.getRemarks());
    }
}
