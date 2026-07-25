package com.store.backend.controller;
import com.store.backend.dto.*;
import com.store.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/checkout")
    public ResponseEntity<OrderDto> checkout(Authentication auth, @Valid @RequestBody CheckoutRequest req) {
        return ResponseEntity.ok(orderService.checkout(auth.getName(), req));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> myOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getMyOrders(auth.getName()));
    }
}