package com.cooltool.rabbitLovesAkka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.nio.charset.StandardCharsets;

public class TestActor extends AbstractActor {

    public static class InitMsg {
    }

    public static Props props() {
        return Props.create(TestActor.class);
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        System.out.println("TestActorPreStart");
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
        System.out.println("TestActorPostStop");
    }

    public TestActor() {

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(InitMsg.class, initMsg -> {
                    ActorRef rabbitmqActor = getContext().actorOf(RabbitmqActor.props("localhost"));
                    System.out.println("RabbitmqActor created");
                    rabbitmqActor.tell(new RabbitmqActor.SubscribeMsg("queue1"), getSelf());
                    System.out.println("Send subscribe message");
                })
                .match(DeliveryMsg.class, deliveryMsg -> {
                    String message = new String(deliveryMsg.getDelivery(), StandardCharsets.UTF_8);
                    System.out.println("message received in TestActor: " + message);
                })
                .matchAny((a) -> System.out.println("Hello"))
                .build();
    }
}
