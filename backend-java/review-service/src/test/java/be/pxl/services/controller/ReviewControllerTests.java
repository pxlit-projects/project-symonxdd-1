package be.pxl.services.controller;

import be.pxl.services.domain.UserRoles;
import be.pxl.services.domain.dto.ReviewDTO;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testApproveReview_withEditorRole_shouldReturnCreated() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .postId(1L)
                .approved(true)
                .remarks("Looks good!")
                .build();

        mockMvc.perform(post("/api/reviews/approve")
                        .header("Role", UserRoles.EDITOR.getDisplayName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(reviewService).approveReview(request);
    }

    @Test
    void testApproveReview_withNonEditorRole_shouldReturnForbidden() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .postId(1L)
                .approved(true)
                .remarks("Looks good!")
                .build();

        mockMvc.perform(post("/api/reviews/approve")
                        .header("Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }

    @Test
    void testRejectReview_withEditorRole_shouldReturnCreated() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .postId(1L)
                .approved(false)
                .remarks("Needs more work.")
                .build();

        mockMvc.perform(post("/api/reviews/reject")
                        .header("Role", UserRoles.EDITOR.getDisplayName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(reviewService).rejectReview(request);
    }

    @Test
    void testRejectReview_withNonEditorRole_shouldReturnForbidden() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .postId(1L)
                .approved(false)
                .remarks("Needs more work.")
                .build();

        mockMvc.perform(post("/api/reviews/reject")
                        .header("Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(reviewService);
    }
}
