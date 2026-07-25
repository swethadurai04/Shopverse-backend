package com.store.backend.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String brand;
    private String imageUrl;
    private Long categoryId;
    private Integer stock;
    private Boolean isFeatured;
    private Double rating;       
    private Integer reviewCount;
    private String officialUrl;
}