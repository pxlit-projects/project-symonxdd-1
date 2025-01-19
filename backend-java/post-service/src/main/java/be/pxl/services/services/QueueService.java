package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.dto.NotificationDTO;
import be.pxl.services.repo.NotificationRepository;
import java.time.LocalDateTime;

import be.pxl.services.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

    @RabbitListener(queues = "review_exchange")
    public void listen(NotificationDTO notificationDTO) {
        logger.info("Message read from review_exchange : " + notificationDTO.getMessage());

        // Update post
        Post post = postRepository.findById(notificationDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus(notificationDTO.getApproved() ? PostStatus.APPROVED : PostStatus.REJECTED);
        postRepository.save(post);

        // Create notification
        String message = notificationDTO.getApproved()
                ? "Post with ID " + notificationDTO.getPostId() + " has been approved."
                : "Post with ID " + notificationDTO.getPostId() + " has been rejected. Remark: " + notificationDTO.getMessage();

        logger.info("Creating notification {}", message);

        Notification notification = Notification.builder()
                .postId(notificationDTO.getPostId())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        logger.info("Notification {} created and saved to DB", notificationDTO.getPostId());
    }
}
