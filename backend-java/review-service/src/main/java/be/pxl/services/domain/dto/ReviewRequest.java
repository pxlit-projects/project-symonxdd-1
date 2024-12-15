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
public class ReviewRequest {
    private Long postId;
    private String remarks;
    private Boolean approved;

    public Review toEntity() {
        Review review = new Review();
        review.setPostId(this.postId);
        review.setRemarks(this.remarks);
        review.setApproved(this.approved);
        review.setCreatedAt(LocalDateTime.now());
        return review;
    }
}

