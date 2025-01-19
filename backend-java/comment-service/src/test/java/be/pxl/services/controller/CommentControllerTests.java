package be.pxl.services.controller;

import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.services.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddComment() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setPostId(1L);
        request.setContent("Test content");

        CommentDTO responseDto = CommentDTO.builder().postId(1L).content("Test content").author("JohnDoe").build();

        Mockito.when(commentService.addComment(any(CreateCommentRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comments/delete/1")
                        .header("Role", "ADMIN"))
                .andExpect(status().isOk());

        Mockito.verify(commentService).deleteComment(1L, "ADMIN");
    }

    @Test
    void testUpdateComment() throws Exception {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Updated content");

        CommentDTO responseDto = CommentDTO.builder().postId(1L).content("Updated content").author("JohnDoe").build();

        Mockito.when(commentService.updateComment(eq(1L), any(UpdateCommentRequest.class), eq("ADMIN"))).thenReturn(responseDto);

        mockMvc.perform(put("/api/comments/edit/1")
                        .header("Role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCommentsByPostId() throws Exception {
        CommentDTO commentDTO = CommentDTO.builder().postId(1L).content("Sample comment").author("JohnDoe").build();

        List<CommentDTO> commentDTOList = Collections.singletonList(commentDTO);

        Mockito.when(commentService.getCommentsByPostId(1L)).thenReturn(commentDTOList);

        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk());
    }
}
