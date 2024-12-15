package be.pxl.services.domain.dto;

import be.pxl.services.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long postId;
    private String remarks;
    private Boolean approved;
    private LocalDateTime createdAt;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.postId = review.getPostId();
        this.remarks = review.getRemarks();
        this.approved = review.getApproved();
        this.createdAt = review.getCreatedAt();
    }
}

