package com.store.backend.controller;
import com.store.backend.dto.*;
import com.store.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<List<CartItemDto>> addToCart(Authentication auth, @Valid @RequestBody AddToCartRequest req) {
        return ResponseEntity.ok(cartService.addToCart(auth.getName(), req));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<List<CartItemDto>> updateQuantity(Authentication auth, @PathVariable Long itemId,
                                                              @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(cartService.updateQuantity(auth.getName(), itemId, body.get("quantity")));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<List<CartItemDto>> removeItem(Authentication auth, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(auth.getName(), itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication auth) {
        cartService.clearCart(auth.getName());
        return ResponseEntity.noContent().build();
    }
}