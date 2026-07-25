package com.store.backend.service;

import com.store.backend.dto.*;
import com.store.backend.entity.*;
import com.store.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewDto addReview(Long productId, String email, ReviewRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();

        reviewRepository.save(review);

        updateProductRating(product);

        return toDto(review);
    }

    public List<ReviewDto> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream().map(this::toDto).toList();
    }

    private void updateProductRating(Product product) {
        List<Review> allReviews = reviewRepository.findByProductId(product.getId());

        double avg = allReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        double roundedAvg = BigDecimal.valueOf(avg)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        product.setRating(roundedAvg);
        product.setReviewCount(allReviews.size());
        productRepository.save(product);
    }

    private ReviewDto toDto(Review r) {
        return ReviewDto.builder()
                .id(r.getId())
                .userName(r.getUser().getName())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}