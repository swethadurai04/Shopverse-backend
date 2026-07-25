package com.store.backend.dto;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
public class ReviewDto {
    private Long id;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}