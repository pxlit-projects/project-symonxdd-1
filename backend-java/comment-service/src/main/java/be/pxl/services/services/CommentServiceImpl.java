package be.pxl.services.services;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.dto.CommentDTO;
import be.pxl.services.domain.dto.CreateCommentRequest;
import be.pxl.services.domain.dto.UpdateCommentRequest;
import be.pxl.services.exception.ResourceNotFoundException;
import be.pxl.services.exception.UnauthorizedActionException;
import be.pxl.services.repo.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public CommentDTO addComment(CreateCommentRequest request) {
        Comment comment = request.toEntity();

        logger.info("Added a new comment: {}", comment);
        return new CommentDTO(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId, String role) {
        // Retrieve the comment from the database
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (comment.getAuthor().equals(role)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new UnauthorizedActionException("User has no permission to delete this comment");
        }

        logger.info("Deleted a comment: {}", commentId);
    }

    @Override
    public CommentDTO updateComment(Long id, UpdateCommentRequest request, String role) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found" + id));

        if (comment.getAuthor().equals(role)) {
            request.updateEntity(comment);
            logger.info("Updating a comment: {}", comment);
            return new CommentDTO(commentRepository.save(comment));
        } else {
            throw new UnauthorizedActionException("User has no permission to update this comment");
        }
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        logger.info("Fetching comments by post id: {}", postId);
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }
}

