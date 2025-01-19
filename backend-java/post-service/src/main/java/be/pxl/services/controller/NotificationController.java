package be.pxl.services.controller;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.UserRoles;
import be.pxl.services.domain.dto.NotificationDTO;
import be.pxl.services.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications(@RequestHeader("Role") String role) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't submit for review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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

        logger.info("Returning notifications");
        return ResponseEntity.ok(notificationDTOs);
    }
}
