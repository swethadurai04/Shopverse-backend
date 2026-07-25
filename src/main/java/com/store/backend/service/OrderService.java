package com.store.backend.service;

import com.store.backend.dto.*;
import com.store.backend.entity.*;
import com.store.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDto checkout(String email, CheckoutRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = Order.builder()
                .user(user)
                .shippingName(req.getShippingName())
                .shippingAddress(req.getShippingAddress())
                .shippingCity(req.getShippingCity())
                .shippingZip(req.getShippingZip())
                .shippingPhone(req.getShippingPhone())
                .paymentMethod(req.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            Product product = ci.getProduct();
            int orderedQty = ci.getQuantity();

            if (product.getStock() < orderedQty) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(orderedQty));
            total = total.add(lineTotal);
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(orderedQty)
                    .build());

            product.setStock(product.getStock() - orderedQty);
            productRepository.save(product);
        }
        order.setTotalAmount(total);

        orderRepository.save(order);
        cartService.clearCart(user.getEmail());

        return toDto(order);
    }

    public List<OrderDto> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toDto).toList();
    }


    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .total(order.getTotalAmount())
                .shippingName(order.getShippingName())
                .shippingAddress(order.getShippingAddress())
                .shippingCity(order.getShippingCity())
                .shippingZip(order.getShippingZip())
                .shippingPhone(order.getShippingPhone())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .orderItems(order.getItems().stream()
                        .map(i -> OrderItemDto.builder()
                                .id(i.getId())
                                .productName(i.getProductName())
                                .productImage(i.getProduct() != null ? i.getProduct().getImageUrl() : null)
                                .price(i.getPrice())
                                .quantity(i.getQuantity())
                                .build())
                        .toList())
                .build();
    }

}