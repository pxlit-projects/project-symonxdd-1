package be.pxl.services.controller;

import be.pxl.services.domain.UserRoles;
import be.pxl.services.domain.dto.ReviewDTO;
import be.pxl.services.domain.dto.ReviewRequest;
import be.pxl.services.services.ReviewService;
import be.pxl.services.services.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    @PostMapping("/approve")
    public ResponseEntity<Void> approveReview(@RequestHeader("Role") String role, @RequestBody ReviewRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't approve review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        reviewService.approveReview(request);
        logger.info("Approved review");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectReview(@RequestHeader("Role") String role, @RequestBody ReviewRequest request) {
        if (!role.equalsIgnoreCase(UserRoles.EDITOR.getDisplayName())) {
            logger.info("Can't approve review, FORBIDDEN");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        reviewService.rejectReview(request);
        logger.info("Rejected review");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

