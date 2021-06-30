package com.matrix.state;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.matrix.reception.model.ChangeRequest;
import com.matrix.state.message.ChangeStateMessage;
import com.matrix.state.models.MatrixState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(StateActor.class);

    private final MatrixState matrixState;

    public StateActor(MatrixState matrixState) {
        this.matrixState = matrixState;
    }

    public static Props props(MatrixState matrixState) {
        return Props.create(StateActor.class, matrixState);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().
                match(ChangeStateMessage.class, this::handleChangeStateMessage)
                .build();
    }

    public void handleChangeStateMessage(ChangeStateMessage message) {
        LOG.trace("Handled change state message with size: {}", message.getRequests().size());
        for (ChangeRequest request : message.getRequests()) {
            matrixState.changeState(request.getIsOn(), request.getX(), request.getY());
        }
    }

}
