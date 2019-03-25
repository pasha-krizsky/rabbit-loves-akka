package com.cooltool.rabbitLovesAkka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitmqActor extends AbstractActor {

    @Data
    public static class SubscribeMsg {
        private final String queueName;
    }

    public static Props props(String host) {
        return Props.create(RabbitmqActor.class, host);
    }

    private Connection connection;
    private Channel channel;

    public RabbitmqActor(String host) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        try {
            connection = factory.newConnection();
            System.out.println("Connection created");
        } catch (IOException | TimeoutException ignored) {
        }
        if (connection != null) {
            try {
                this.channel = connection.createChannel();
                System.out.println("Channel created");
            } catch (IOException ignored) {
            }
        }
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(SubscribeMsg.class, this::handleSubscribeMsg)
                .build();
    }

    private void handleSubscribeMsg(SubscribeMsg subscribeMsg) throws IOException {
        String queueName = subscribeMsg.getQueueName();
        ActorRef sender = RabbitmqActor.this.getSender();
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("message received in RabbitActor: " + message);
            sender.tell(new DeliveryMsg(consumerTag, delivery.getBody()), getSelf());
        };
        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });
    }
}
