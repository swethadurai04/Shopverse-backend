package com.store.backend.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class OrderDto {
    private Long id;
    private BigDecimal total;
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZip;
    private String shippingPhone;
    private String paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems;
}