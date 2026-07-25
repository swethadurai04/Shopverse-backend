package com.store.backend.controller;
import com.store.backend.dto.*;
import com.store.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long productId,
                                                @RequestBody @Valid ReviewRequest req,
                                                Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(reviewService.addReview(productId, email, req));
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }
}
