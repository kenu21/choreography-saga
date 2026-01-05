package com.keniu;

import com.keniu.inventories.InventoryFailedEvent;
import com.keniu.inventories.InventoryReservedEvent;
import com.keniu.payments.PaymentReservedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    // Probability threshold for successful inventory reservation (50%)
    private static final double INVENTORY_SUCCESS_PROBABILITY = 0.5;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryListener(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-reserved")
    public void handle(PaymentReservedEvent event) {

        boolean inStock = Math.random() > INVENTORY_SUCCESS_PROBABILITY;

        if (inStock) {
            kafkaTemplate.send(
                    "inventory-reserved",
                    event.orderId(),
                    new InventoryReservedEvent(event.orderId())
            );
            System.out.println("inventory-reserved");
        } else {
            kafkaTemplate.send(
                    "inventory-failed",
                    event.orderId(),
                    new InventoryFailedEvent(event.orderId())
            );
            System.out.println("inventory-failed");
        }
    }
}
