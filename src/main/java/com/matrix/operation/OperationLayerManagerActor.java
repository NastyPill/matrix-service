package com.matrix.operation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.matrix.operation.message.OperatorStartMessage;
import com.matrix.state.message.StateStartMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperationLayerManagerActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(OperationLayerManagerActor.class);
    private final ActorRef receptionManager;

    public OperationLayerManagerActor(ActorRef dispatcherManager) {
        this.receptionManager = dispatcherManager;
    }

    public static Props props(ActorRef receptionManager) {
        return Props.create(OperationLayerManagerActor.class, receptionManager);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(StateStartMessage.class, msg -> handleStateStartMessage(msg))
                .build();
    }

    private void handleStateStartMessage(StateStartMessage message) {
        initOperator(message.getStateActor());
        LOG.info("Operator was started");
        receptionManager.tell(new OperatorStartMessage(initOperator(message.getStateActor())), getSelf());
    }

    private ActorRef initOperator(ActorRef stateActor) {
        return context().actorOf(OperatorActor.props(stateActor));
    }
}
