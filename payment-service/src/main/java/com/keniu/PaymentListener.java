package com.keniu;

import com.keniu.inventories.InventoryFailedEvent;
import com.keniu.orders.OrderCreatedEvent;
import com.keniu.payments.PaymentFailedEvent;
import com.keniu.payments.PaymentReservedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {
    private static final int PAYMENT_LIMIT = 1000;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentListener(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created")
    public void handle(OrderCreatedEvent event) {

        boolean success = event.price() < PAYMENT_LIMIT;

        if (success) {
            kafkaTemplate.send(
                    "payment-reserved",
                    event.orderId(),
                    new PaymentReservedEvent(event.orderId())
            );
            System.out.println("payment-reserved");
        } else {
            kafkaTemplate.send(
                    "payment-failed",
                    event.orderId(),
                    new PaymentFailedEvent(event.orderId())
            );
            System.out.println("payment-failed");
        }
    }

    @KafkaListener(topics = "inventory-failed")
    public void rollbackPayment(InventoryFailedEvent event) {
        System.out.println("Rollback payment for " + event.orderId());
    }
}
