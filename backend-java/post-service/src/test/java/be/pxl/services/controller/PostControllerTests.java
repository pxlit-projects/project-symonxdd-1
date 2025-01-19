package be.pxl.services.controller;

import be.pxl.services.domain.dto.*;
import be.pxl.services.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void testGetPostById() throws Exception {
        // Mocking the service response using the builder pattern
        PostDTO mockPost = PostDTO.builder()
                .id(1L)
                .title("Sample Title")
                .content("Sample Content")
                .build();

        when(postService.getPostById(1L)).thenReturn(mockPost);

        // Performing the GET request
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Title"))
                .andExpect(jsonPath("$.content").value("Sample Content"));
    }

    @Test
    void testSubmitPostForReviewWithEditorRole() throws Exception {
        SubmitForReviewRequest request = new SubmitForReviewRequest(1L);
        String requestBody = new ObjectMapper().writeValueAsString(request);

        // No need to mock since we're just verifying a 200 response
        mockMvc.perform(post("/api/posts/submit-for-review")
                        .header("Role", "EDITOR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(postService).submitPostForReview(any(SubmitForReviewRequest.class));
    }

    @Test
    void testSubmitPostForReviewWithoutEditorRole() throws Exception {
        SubmitForReviewRequest request = new SubmitForReviewRequest(1L);
        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/posts/submit-for-review")
                        .header("Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());

        verify(postService, never()).submitPostForReview(any(SubmitForReviewRequest.class));
    }

    @Test
    void testGetUnpublishedPostsWithEditorRole() throws Exception {
        List<PostDTO> mockPosts = List.of(
                PostDTO.builder().id(1L).title("Title1").content("Content1").build(),
                PostDTO.builder().id(2L).title("Title2").content("Content2").build()
        );

        when(postService.getUnpublishedPosts()).thenReturn(mockPosts);

        mockMvc.perform(get("/api/posts/unpublished")
                        .header("Role", "EDITOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    void testGetUnpublishedPostsWithoutEditorRole() throws Exception {
        mockMvc.perform(get("/api/posts/unpublished")
                        .header("Role", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreatePostWithEditorRole() throws Exception {
        CreatePostRequest request = CreatePostRequest.builder()
                .title("Sample Title")
                .content("Sample Content")
                .build();

        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/posts")
                        .header("Role", "EDITOR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        verify(postService).createPost(any(CreatePostRequest.class));
    }

    @Test
    void testCreatePostWithoutEditorRole() throws Exception {
        CreatePostRequest request = CreatePostRequest.builder()
                .title("Sample Title")
                .content("Sample Content")
                .build();

        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/api/posts")
                        .header("Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());

        verify(postService, never()).createPost(any(CreatePostRequest.class));
    }

    @Test
    void testUpdatePostWithEditorRole() throws Exception {
        UpdatePostRequest request = UpdatePostRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(put("/api/posts/1")
                        .header("Role", "EDITOR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(postService).updatePost(eq(1L), any(UpdatePostRequest.class));
    }

    @Test
    void testUpdatePostWithoutEditorRole() throws Exception {
        UpdatePostRequest request = UpdatePostRequest.builder()
                .title("Updated Title")
                .content("Updated Content")
                .build();

        String requestBody = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(put("/api/posts/1")
                        .header("Role", "USER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());

        verify(postService, never()).updatePost(anyLong(), any(UpdatePostRequest.class));
    }

    @Test
    void testGetPublishedPosts() throws Exception {
        List<PostDTO> mockPosts = List.of(
                PostDTO.builder().id(1L).title("Published Title1").content("Published Content1").build(),
                PostDTO.builder().id(2L).title("Published Title2").content("Published Content2").build()
        );

        when(postService.getPublishedPosts()).thenReturn(mockPosts);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Published Title1"))
                .andExpect(jsonPath("$[1].title").value("Published Title2"));
    }

    @Test
    void testGetDraftPostsWithEditorRole() throws Exception {
        List<PostDTO> mockPosts = List.of(
                PostDTO.builder().id(1L).title("Draft Title1").content("Draft Content1").build(),
                PostDTO.builder().id(2L).title("Draft Title2").content("Draft Content2").build()
        );

        when(postService.getDraftPosts()).thenReturn(mockPosts);

        mockMvc.perform(get("/api/posts/drafts")
                        .header("Role", "EDITOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Draft Title1"))
                .andExpect(jsonPath("$[1].title").value("Draft Title2"));
    }

    @Test
    void testGetDraftPostsWithoutEditorRole() throws Exception {
        mockMvc.perform(get("/api/posts/drafts")
                        .header("Role", "USER"))
                .andExpect(status().isForbidden());
    }
}
