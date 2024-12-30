package be.pxl.services.services;

import be.pxl.services.domain.Review;
import be.pxl.services.domain.dto.NotificationDTO;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    public void approveReview(ReviewRequest request) {
        Review review = request.toEntity();
        review.setApproved(true);
        review = reviewRepository.save(review);

        NotificationDTO notification = NotificationDTO.builder()
                .postId(review.getPostId())
                .message(review.getRemarks())
                .createdAt(review.getCreatedAt())
                .approved(true)
                .build();

        rabbitTemplate.convertAndSend("review_exchange", notification);
        logger.info("Approved review: {}", review);
    }

    @Override
    public void rejectReview(ReviewRequest request) {
        Review review = request.toEntity();
        review.setApproved(false);
        review = reviewRepository.save(review);

        NotificationDTO notification = NotificationDTO.builder()
                .postId(review.getPostId())
                .message(review.getRemarks())
                .createdAt(review.getCreatedAt())
                .approved(false)
                .build();

        rabbitTemplate.convertAndSend("review_exchange", notification);
        logger.info("Rejected review: {}", review);
    }
}