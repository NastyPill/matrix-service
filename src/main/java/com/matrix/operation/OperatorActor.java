package com.matrix.operation;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.matrix.operation.message.NewWorkMessage;
import com.matrix.operation.message.WorkReportMessage;
import com.matrix.state.message.ChangeStateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.sys.Prop;

import static java.util.Objects.isNull;

public class OperatorActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorActor.class);

    private ActorRef stateActor;

    public OperatorActor(ActorRef stateActor) {
        this.stateActor = stateActor;
    }

    public static Props props(ActorRef stateActor) {
        return Props.create(OperatorActor.class, stateActor);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NewWorkMessage.class, msg -> handleNewWorkMessage(msg))
                .build();
    }


    public void handleNewWorkMessage(NewWorkMessage message) {
        long currentMillis = System.nanoTime();
        if (!isNull(message.getOrder())) {
            LOG.trace("New work message with size: {}", message.getOrder().getRequests().size());
            LOG.trace("Sender: {}", this.getSender());
            stateActor.tell(new ChangeStateMessage(message.getOrder().getRequests()), getSelf());
            this.getSender().tell(WorkReportMessage.builder()
                            .time(System.nanoTime() - currentMillis)
                            .requestCount(message.getOrder().getRequests().size())
                            .build()
                    , getSelf());
        } else {
            this.getSender().tell(WorkReportMessage.builder()
            .time(0l)
            .requestCount(0)
            .build(), getSelf());
        }
    }

}
