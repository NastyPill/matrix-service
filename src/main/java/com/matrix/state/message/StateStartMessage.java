package com.matrix.state.message;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StateStartMessage {

    ActorRef stateActor;

}
