package com.matrix.state;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.matrix.state.announce.MatrixStateTCPAnnouncer;
import com.matrix.state.message.StartStateMessage;
import com.matrix.state.message.StateStartMessage;
import com.matrix.state.models.MatrixState;

public class StateManagerActor extends AbstractActor {

    private static final String IP = "192.168.0.17";
    private static final Integer PORT = 6001;

    private final ActorRef operationManager;

    public StateManagerActor(ActorRef operationManager) {
        this.operationManager = operationManager;
    }

    public static Props props(ActorRef operationManager) {
        return Props.create(StateManagerActor.class, operationManager);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StartStateMessage.class, msg -> handleStartStateMessage(msg))
                .build();
    }

    private void handleStartStateMessage(StartStateMessage msg) {
        operationManager.tell(new StateStartMessage(initStateActor()), getSelf());
    }

    private ActorRef initStateActor() {
        return context().actorOf(StateActor.props(new MatrixState(new MatrixStateTCPAnnouncer(IP, PORT))));
    }
}
