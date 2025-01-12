package be.pxl.services.services;

import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;

import java.util.List;

public interface CommentService {
    CommentDTO addComment(CreateCommentRequest request);
    void deleteComment(Long commentId, String role);
    CommentDTO updateComment(Long id, UpdateCommentRequest request, String role);
    List<CommentDTO> getCommentsByPostId(Long postId);
}

