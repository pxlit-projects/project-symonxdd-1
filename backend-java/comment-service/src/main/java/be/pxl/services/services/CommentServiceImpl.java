package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.repo.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public CommentDTO addComment(CreateCommentRequest request) {
        Comment comment = request.toEntity();
        return new CommentDTO(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        // Check if the comment exists before attempting deletion
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDTO updateComment(Long id, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        request.updateEntity(comment);
        return new CommentDTO(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
}

