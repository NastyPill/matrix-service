package com.matrix.operation.message;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperatorStartMessage {

    ActorRef operator;

}
