package com.store.backend.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotBlank private String shippingName;
    @NotBlank private String shippingAddress;
    @NotBlank private String shippingCity;
    @NotBlank private String shippingZip;
    @NotBlank private String shippingPhone;
    @NotBlank private String paymentMethod;
}