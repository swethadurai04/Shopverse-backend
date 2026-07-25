package com.store.backend.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @Builder
public class OrderItemDto {
    private Long id;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
}