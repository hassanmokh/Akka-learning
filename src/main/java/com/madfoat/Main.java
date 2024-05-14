package com.madfoat;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.FromConfig;
import com.madfoat.actors.SensorActor;
import com.madfoat.messages.Message;
import com.madfoat.messages.TypeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Main {

    private static List<String> devices = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        ActorSystem system = ActorSystem.create("DDs-Task");

        ActorRef sensorActor = system.actorOf(SensorActor.props().withRouter(FromConfig.getInstance()), "sensorActor");

        Random random = new Random();

        for(int idx = 0; idx < 800; idx++){
            Integer rand = random.nextInt(10000);

            if(rand < 2000)
                insert(sensorActor);

            else if(rand > 2000 && rand <= 4000)
                bulkInsert(10, sensorActor);

            else if(rand > 4000 && rand <= 6000 && devices.size() > 1)
                replace(sensorActor);

            else if (rand > 6000 && rand <= 9998 && devices.size() > 0){
                read(sensorActor);
            }

            else if (rand == 9999 && devices.size() > 0)
                remove(sensorActor);

            else if(rand > 9999)
                clear(sensorActor);

        }

    }


    private static void insert(ActorRef sensorActor){
        Message msg = new Message();
        msg.setTypeMessage(TypeMessage.PUT);
        String device = UUID.randomUUID().toString();
        devices.add(device);
        msg.setKey(device);
        msg.setValue(Math.random());

        sensorActor.tell(msg, ActorRef.noSender());
    }

    private static void bulkInsert(Integer size, ActorRef sensorActor){

        for(int idx = 0; idx< size; idx++){
            Message msg = new Message();
            msg.setTypeMessage(TypeMessage.PUT);
            String device = UUID.randomUUID().toString();
            devices.add(device);
            msg.setKey(device);
            msg.setValue(Math.random());

            sensorActor.tell(msg, ActorRef.noSender());
        }

    }

    private static void read(ActorRef sensorActor){

        for (String device : devices) {

            Message msg = new Message();
            msg.setTypeMessage(TypeMessage.READ);
            msg.setKey(device);

            sensorActor.tell(msg, ActorRef.noSender());
        }
    }

    private static void remove(ActorRef sensorActor){
        Random random = new Random();
        Integer idx = random.nextInt(devices.size() - 2) + 1;
        Message msg = new Message();
        msg.setTypeMessage(TypeMessage.REMOVE);
        msg.setKey(devices.get(idx));

        sensorActor.tell(msg, ActorRef.noSender());
    }

    private static void clear(ActorRef sensorActor){
        Message msg = new Message();
        msg.setTypeMessage(TypeMessage.CLEAR);

        sensorActor.tell(msg, ActorRef.noSender());
    }

    private static void replace(ActorRef sensorActor){
        Random random = new Random();
        Integer idx = random.nextInt(devices.size() - 1);

        Message msg = new Message();
        msg.setTypeMessage(TypeMessage.REPLACE);
        msg.setKey(devices.get(idx));
        msg.setValue(Math.random());

        sensorActor.tell(msg, ActorRef.noSender());
    }

}
