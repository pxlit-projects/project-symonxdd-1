package be.pxl.services.controller;

import be.pxl.services.domain.dto.ReviewDTO;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/approve")
    public ResponseEntity<Void> approveReview(@RequestBody ReviewRequest request) {
        reviewService.approveReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectReview(@RequestBody ReviewRequest request) {
        reviewService.rejectReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

