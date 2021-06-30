package com.matrix.reception;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.matrix.operation.message.OperatorStartMessage;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceptionManagerActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(ReceptionManagerActor.class);

    private static final String RABBITMQ_HOST = "localhost";
    private static final Integer RABBITMQ_PORT = 5672;

    public ReceptionManagerActor() {
    }

    public static Props props() {
        return Props.create(ReceptionManagerActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(OperatorStartMessage.class, msg -> handleOperatorStartMessage(msg))
                .build();
    }


    private void handleOperatorStartMessage(OperatorStartMessage msg) throws InterruptedException {
        LOG.info("HandleStartSystemMessage");
        ActorRef consumer = null;
        ActorRef dispatcher;
        try {
            consumer = initConsumer();
        } catch (Exception e) {
            LOG.error("Failed to initConsumer: {}", e.toString());
        }
        dispatcher = initDispatcher(consumer, msg.getOperator());
        while (true) {
            Thread.sleep(1000);
            dispatcher.tell("", ActorRef.noSender());
        }
    }

    private ActorRef initDispatcher(ActorRef consumer, ActorRef operator) {
        return context().actorOf(DispatcherActor.props(consumer, operator), "dispatcher");
    }

    private ActorRef initConsumer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(RABBITMQ_PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return context().actorOf(ConsumerActor.props(channel), "consumer");
    }

}
