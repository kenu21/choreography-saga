package com.keniu.orders;

public record OrderCreatedEvent(
        String orderId,
        String productId,
        int quantity,
        double price
) {
}
