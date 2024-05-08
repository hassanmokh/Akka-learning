package com.madfoat.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import com.madfoat.messages.Message;

import java.util.HashMap;

public class StoreActor extends AbstractLoggingActor {

    private static HashMap<String, Object> Store = new HashMap<>();

    public static Props props(){
        return Props.create(StoreActor.class, StoreActor::new);
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg->{
                    log().info("Actor {} In progress now", getSender().toString());
                    switch (msg.getTypeMessage()){
                        case PUT -> put(msg);
                        case READ -> read(msg);
                        case REMOVE -> remove(msg);
                        case REPLACE -> replace(msg);
                        case CLEAR -> clear();
                    }
                })
                .matchAny((Object o)->{})
                .build();
    }

    private void put(Message msg){

        log().info("Insert new Sensor: {} , value {}", msg.getKey(), msg.getValue());

        Store.put(msg.getKey(), msg.getValue());

    }

    private void read(Message msg){
        log().info("Read Data from Sensor {}", msg.getKey());

        log().info("Sensor {} has a Data {}", msg.getKey(), Store.get(msg.getKey()));
    }

    private void remove(Message msg){
        log().warning("A sensor {} will be removed", msg.getKey());
        Store.remove(msg.getKey());

    }

    private void replace(Message msg){
        Object oldVal = Store.get(msg.getKey());
        log().warning("A sensor {} with data {} will be replace it to {} ", msg.getKey(), oldVal, Store.replace(msg.getKey(), msg.getValue()));
    }

    private void clear(){
        log().warning("waiting to clear all Store");
        Store.clear();
        log().notifyInfo("Successfully cleared");

    }

}
