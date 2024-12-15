package be.pxl.services.controller;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.dto.NotificationDTO;
import be.pxl.services.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        List<Notification> notifications = notificationRepository.findAll();

        // Transform entities to DTOs
        List<NotificationDTO> notificationDTOs = notifications.stream()
            .map(notification -> NotificationDTO.builder()
                    .postId(notification.getPostId())
                    .approved(null)
                    .message(notification.getMessage())
                    .createdAt(notification.getCreatedAt())
                    .build())
            .toList();

        return ResponseEntity.ok(notificationDTOs);
    }
}
