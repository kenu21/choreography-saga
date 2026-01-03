package com.keniu;

import com.keniu.orders.OrderCreatedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/orders")
    public String createOrder() {
        String orderId = UUID.randomUUID().toString();

        OrderCreatedEvent event =
                new OrderCreatedEvent(orderId, "product-1", 1, 100.0);

        kafkaTemplate.send("order-created", orderId, event);

        return orderId;
    }
}
