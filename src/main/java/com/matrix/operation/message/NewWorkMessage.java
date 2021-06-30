package com.matrix.operation.message;

import com.matrix.reception.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewWorkMessage {

    Order order;

}
