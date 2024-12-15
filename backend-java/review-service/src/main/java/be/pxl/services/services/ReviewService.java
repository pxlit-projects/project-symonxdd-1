package be.pxl.services.services;

import be.pxl.services.domain.dto.ReviewDTO;
import be.pxl.services.domain.dto.ReviewRequest;

public interface ReviewService {
    void approveReview(ReviewRequest request);
    void rejectReview(ReviewRequest request);
}
