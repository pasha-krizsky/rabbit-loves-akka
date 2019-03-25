package com.cooltool.rabbitLovesAkka;

import com.rabbitmq.client.Delivery;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Delivery message from RabbitMq's channel
 *
 * @see Delivery
 */
@Data
@AllArgsConstructor
public class DeliveryMsg {
    private String consumerTag;
    private byte[] delivery;
}
