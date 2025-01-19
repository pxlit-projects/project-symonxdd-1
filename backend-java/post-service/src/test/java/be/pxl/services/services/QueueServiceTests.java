package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.NotificationDTO;
import be.pxl.services.repo.NotificationRepository;
import be.pxl.services.repo.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueueServiceTests {

    @InjectMocks
    private QueueService queueService; // The class under test

    @Mock
    private PostRepository postRepository; // Mocked repository

    @Mock
    private NotificationRepository notificationRepository; // Mocked repository

    @Captor
    private ArgumentCaptor<Post> postCaptor; // To capture saved Post objects

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor; // To capture saved Notification objects

    @Test
    void testListen_PostApproved() {
        // Arrange
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .postId(1L)
                .approved(true)
                .message("Great content!")
                .build();

        Post post = new Post();
        post.setId(1L);
        post.setStatus(PostStatus.PENDING_REVIEW);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        queueService.listen(notificationDTO);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        verify(notificationRepository).save(notificationCaptor.capture());

        Post savedPost = postCaptor.getValue();
        assertEquals(PostStatus.APPROVED, savedPost.getStatus());

        Notification savedNotification = notificationCaptor.getValue();
        assertEquals(1L, savedNotification.getPostId());
        assertEquals("Post with ID 1 has been approved.", savedNotification.getMessage());
        assertNotNull(savedNotification.getCreatedAt());
    }

    @Test
    void testListen_PostRejected() {
        // Arrange
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .postId(2L)
                .approved(false)
                .message("Needs more details.")
                .build();

        Post post = new Post();
        post.setId(2L);
        post.setStatus(PostStatus.PENDING_REVIEW);

        when(postRepository.findById(2L)).thenReturn(Optional.of(post));

        // Act
        queueService.listen(notificationDTO);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        verify(notificationRepository).save(notificationCaptor.capture());

        Post savedPost = postCaptor.getValue();
        assertEquals(PostStatus.REJECTED, savedPost.getStatus());

        Notification savedNotification = notificationCaptor.getValue();
        assertEquals(2L, savedNotification.getPostId());
        assertEquals("Post with ID 2 has been rejected. Remark: Needs more details.", savedNotification.getMessage());
        assertNotNull(savedNotification.getCreatedAt());
    }

    @Test
    void testListen_PostNotFound() {
        // Arrange
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .postId(3L)
                .approved(true)
                .message("Good job!")
                .build();

        when(postRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> queueService.listen(notificationDTO));
        assertEquals("Post not found", exception.getMessage());

        verify(postRepository).findById(3L);
        verifyNoInteractions(notificationRepository); // Ensure no notifications are saved
    }
}
