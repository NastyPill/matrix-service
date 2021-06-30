package com.matrix.state.message;

import com.matrix.reception.model.ChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChangeStateMessage {

    List<ChangeRequest> requests;

}
