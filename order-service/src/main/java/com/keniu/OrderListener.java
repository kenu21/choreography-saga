package com.keniu;

import com.keniu.inventories.InventoryFailedEvent;
import com.keniu.inventories.InventoryReservedEvent;
import com.keniu.orders.OrderCancelledEvent;
import com.keniu.orders.OrderConfirmedEvent;
import com.keniu.payments.PaymentFailedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderListener(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "inventory-reserved")
    public void onInventoryReserved(InventoryReservedEvent event) {
        kafkaTemplate.send(
                "order-confirmed",
                event.orderId(),
                new OrderConfirmedEvent(event.orderId())
        );
        System.out.println("order-confirmed");
    }

    @KafkaListener(topics = "payment-failed")
    public void onPaymentFailed(PaymentFailedEvent event) {
        kafkaTemplate.send(
                "order-cancelled",
                event.orderId(),
                new OrderCancelledEvent(event.orderId())
        );
        System.out.println("order-cancelled");
    }

    @KafkaListener(topics = "inventory-failed")
    public void onInventoryFailed(InventoryFailedEvent event) {
        kafkaTemplate.send(
                "order-cancelled",
                event.orderId(),
                new OrderCancelledEvent(event.orderId())
        );
        System.out.println("order-cancelled");
    }
}
