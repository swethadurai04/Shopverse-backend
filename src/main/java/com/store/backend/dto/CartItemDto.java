package com.store.backend.dto;
import lombok.*;

@Data @Builder
public class CartItemDto {
    private Long id;
    private ProductDto product;   
    private Integer quantity;
}