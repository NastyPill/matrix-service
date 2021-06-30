package com.matrix.reception;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.matrix.operation.message.NewWorkMessage;
import com.matrix.operation.message.WorkReportMessage;
import com.matrix.reception.message.GetOrdersMessage;
import com.matrix.reception.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class DispatcherActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherActor.class);

    private final Queue<Order> orderQueue;
    private final ActorRef consumer;
    private final ActorRef operator;

    public DispatcherActor(ActorRef consumer, ActorRef operator) {
        this.consumer = consumer;
        this.operator = operator;
        this.orderQueue = new LinkedList<>();
        startWork();
    }

    private void startWork() {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            LOG.error("Something went wrong wile consumer waiting: {}", e.toString());
        }
        operator.tell(new NewWorkMessage(new Order(new ArrayList<>())), getSelf());
        LOG.info("Dispatcher was started");
    }

    public static Props props(ActorRef consumer, ActorRef operator) {
        return Props.create(DispatcherActor.class, consumer, operator);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetOrdersMessage.class, msg -> handleGetOrdersMessage(msg))
                .match(WorkReportMessage.class, msg -> handleWorkReportMessage(msg))
                .build();
    }

    private void handleWorkReportMessage(WorkReportMessage msg) {
        LOG.info("Report from operator, QUEUE: {}", orderQueue.size());
        if(orderQueue.isEmpty()) {
            sendGetOrdersMessage();
        } else {
            sendNewWorkMessage();
        }
    }

    private void sendGetOrdersMessage() {
        LOG.info("Get orders from consumer, dispQueueSize: {}", orderQueue.size());
        consumer.tell(new GetOrdersMessage(new LinkedList<>()), this.getSelf());
    }

    private void sendNewWorkMessage() {
        LOG.info("NEW WORK MESSAGE : {}", orderQueue.size());
        operator.tell(new NewWorkMessage(orderQueue.poll()), getSelf());
    }

    private void handleGetOrdersMessage(GetOrdersMessage getOrdersMessage) {
        LOG.info("RECEIVED: {} orders", getOrdersMessage.getOrders().size());
        if(!getOrdersMessage.getOrders().isEmpty()) {
            orderQueue.addAll(getOrdersMessage.getOrders());
            LOG.trace("Current dispatcher queue size is: {}", orderQueue.size());
            sendNewWorkMessage();
        }
        else {
            try {
                Thread.sleep(100l);
                sendGetOrdersMessage();
            } catch (InterruptedException e) {
                LOG.error("Exception during sleep: {}", e.toString());
            }
        }
    }
}
