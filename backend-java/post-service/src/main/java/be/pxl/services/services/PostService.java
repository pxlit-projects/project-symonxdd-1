package be.pxl.services.services;

import be.pxl.services.domain.dto.CreatePostRequest;
import be.pxl.services.domain.dto.PostDTO;
import be.pxl.services.domain.dto.UpdatePostRequest;

import java.util.List;

public interface PostService {
    PostDTO createPost(CreatePostRequest createPostRequest);
    PostDTO getPostById(Long id);
    PostDTO updatePost(Long id, UpdatePostRequest request);
    List<PostDTO> getUnpublishedPosts();
    List<PostDTO> getPublishedPosts();
    List<PostDTO> filterPosts(String category, String author);
}
