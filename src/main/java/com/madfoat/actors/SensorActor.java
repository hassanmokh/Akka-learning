package com.madfoat.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.madfoat.messages.Message;

public class SensorActor extends AbstractLoggingActor {

    public static Props props(){
        return Props.create(SensorActor.class, SensorActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg-> {
                        log().info("Add a new sensor data {}", msg.getKey());
                        getContext()
                        .actorOf(StoreActor.props())
                        .tell(msg, getSelf());
                }
                )
                .matchAny((Object o) -> {})
                .build();
    }
}
