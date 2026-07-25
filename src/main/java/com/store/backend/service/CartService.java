package com.store.backend.service;

import com.store.backend.dto.*;
import com.store.backend.entity.*;
import com.store.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartItemDto> getCart(String email) {
        User user = getUser(email);
        return cartItemRepository.findByUser(user).stream().map(this::toDto).toList();
    }

    public List<CartItemDto> addToCart(String email, AddToCartRequest req) {
        User user = getUser(email);
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByUserAndProductId(user, req.getProductId())
                .orElse(CartItem.builder().user(user).product(product).quantity(0).build());
        item.setQuantity(item.getQuantity() + req.getQuantity());
        cartItemRepository.save(item);
        return getCart(email);
    }

    public List<CartItemDto> updateQuantity(String email, Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return getCart(email);
    }

    public List<CartItemDto> removeItem(String email, Long itemId) {
        cartItemRepository.deleteById(itemId);
        return getCart(email);
    }

    public void clearCart(String email) {
        User user = getUser(email);
        cartItemRepository.deleteByUser(user);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CartItemDto toDto(CartItem item) {
        Product product = item.getProduct();
        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .brand(product.getBrand())
                .imageUrl(product.getImageUrl())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .stock(product.getStock())
                .isFeatured(product.getIsFeatured())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .officialUrl(product.getOfficialUrl())
                .build();

        return CartItemDto.builder()
                .id(item.getId())
                .product(productDto)
                .quantity(item.getQuantity())
                .build();
    }
}