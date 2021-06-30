package com.matrix.reception;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.matrix.reception.message.GetOrdersMessage;
import com.matrix.reception.model.ChangeRequestNm;
import com.matrix.reception.model.Order;
import com.matrix.reception.model.OrderNm;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedList;
import java.util.Queue;

import static com.matrix.reception.model.mapper.Nm2DomainMapper.OBJECT_MAPPER;
import static com.matrix.reception.model.mapper.Nm2DomainMapper.mapToOrder;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

public class ConsumerActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerActor.class);
    private static final String QUEUE_NAME = "queue";

    private final Queue<Order> orderQueue;

    private Channel channel;
    private Boolean consumingNow = false;

    public ConsumerActor(Channel channel) {
        this.orderQueue = new LinkedList<>();
        this.channel = channel;

        try {
            consumeOrders();
            LOG.info("Consumer was started");
        } catch (Exception e) {
            LOG.error("Someting went wrong with consumer initialization: {}", e.toString());
        }
    }

    public static Props props(Channel channel) {
        return Props.create(ConsumerActor.class, channel);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetOrdersMessage.class, msg -> handleGetOrdersMessage(msg))
                .build();
    }

    private void handleGetOrdersMessage(GetOrdersMessage getOrdersMessage) {
        try {

            LOG.info("Message queue size is CONSUMER: {}", String.valueOf(orderQueue.size()));
            int size = orderQueue.size();
            for (int i = 0; i < size; i++) {
                getOrdersMessage.getOrders().add(orderQueue.poll());
            }
            this.getSender().tell(getOrdersMessage, this.getSelf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToQueue(String order) {
        try {
            orderQueue.add(mapToOrder(order));
        } catch (Exception e) {
            LOG.error("Something wrong with mapping from JSON for: {}. \nException is: {}", order, e.toString());
        }
    }

    private void consumeOrders() throws Exception {
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if(orderQueue.size() < 500) {
                addToQueue(new String(delivery.getBody(), UTF_8));
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
                return;
            }
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}
