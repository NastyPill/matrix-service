package com.matrix.reception;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.matrix.operation.OperationLayerManagerActor;
import com.matrix.state.StateManagerActor;
import com.matrix.state.message.StartStateMessage;

import static akka.actor.Nobody.tell;

public class Main {

    public static void main(String[] args) {
        ActorRef recepManager = ActorSystem.create().actorOf(ReceptionManagerActor.props(), "manager");
        ActorRef operManager = ActorSystem.create().actorOf(OperationLayerManagerActor.props(recepManager), "manager");
        ActorRef stateManager = ActorSystem.create().actorOf(StateManagerActor.props(operManager), "manager");

        stateManager.tell(new StartStateMessage(), ActorRef.noSender());
    }

}
