package com.matrix.reception.message;

import akka.actor.ActorRef;
import com.matrix.reception.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Queue;

@Data
@AllArgsConstructor
public class GetOrdersMessage {

    private Queue<Order> orders;

}
