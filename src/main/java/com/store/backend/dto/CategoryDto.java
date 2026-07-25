package com.store.backend.dto;

import lombok.*;

@Data @Builder
public class CategoryDto {
    private Long id;
    private String name;
    private String slug;
    private String imageUrl;
}