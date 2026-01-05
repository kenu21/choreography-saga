package com.keniu;

import com.keniu.orders.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {
    private static final int DEFAULT_QUANTITY = 1;
    private static final double DEFAULT_PRICE = 100.0;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/orders")
    public String createOrder() {
        String orderId = UUID.randomUUID().toString();

        OrderCreatedEvent event =
                new OrderCreatedEvent(orderId, "product-1", DEFAULT_QUANTITY, DEFAULT_PRICE);

        kafkaTemplate.send("order-created", orderId, event);

        return orderId;
    }
}
