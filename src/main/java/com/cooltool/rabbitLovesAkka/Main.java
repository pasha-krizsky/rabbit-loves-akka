package com.cooltool.rabbitLovesAkka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("test");
        ActorRef testActorRef = actorSystem.actorOf(TestActor.props());
        testActorRef.tell(new TestActor.InitMsg(), ActorRef.noSender());
    }
}
