package be.pxl.services.controller;

import be.pxl.services.domain.Notification;
import be.pxl.services.domain.dto.*;
import be.pxl.services.repo.NotificationRepository;
import be.pxl.services.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    void testGetNotificationsWithEditorRole() throws Exception {
        // Mocking the repository response
        List<Notification> mockNotifications = List.of(
                Notification.builder()
                        .postId(1L)
                        .message("Notification 1")
                        .createdAt(LocalDateTime.now())
                        .build(),
                Notification.builder()
                        .postId(2L)
                        .message("Notification 2")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        when(notificationRepository.findAll()).thenReturn(mockNotifications);

        // Performing the GET request with the "EDITOR" role
        mockMvc.perform(get("/api/notifications")
                        .header("Role", "EDITOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(1L))
                .andExpect(jsonPath("$[0].message").value("Notification 1"))
                .andExpect(jsonPath("$[1].postId").value(2L))
                .andExpect(jsonPath("$[1].message").value("Notification 2"));
    }

    @Test
    void testGetNotificationsWithoutEditorRole() throws Exception {
        // Performing the GET request with a non-editor role
        mockMvc.perform(get("/api/notifications")
                        .header("Role", "USER"))
                .andExpect(status().isForbidden());

        // Ensure repository is never called when the role is invalid
        verify(notificationRepository, never()).findAll();
    }
}
