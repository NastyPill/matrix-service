package com.matrix.reception.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.reception.model.ChangeRequest;
import com.matrix.reception.model.ChangeRequestNm;
import com.matrix.reception.model.Order;
import com.matrix.reception.model.OrderNm;

import java.util.List;
import java.util.stream.Collectors;

public class Nm2DomainMapper {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Order mapToOrder(String orderJson) throws JsonProcessingException {
        OrderNm orderNm = OBJECT_MAPPER.readValue(orderJson, OrderNm.class);
        return Order.builder()
                .requests(mapToChangeRequest(orderNm.getRequestNmList()))
                .build();
    }

    private static List<ChangeRequest> mapToChangeRequest(List<ChangeRequestNm> changeRequestNmList) {
        return changeRequestNmList
                .stream()
                .map(nm -> ChangeRequest.builder()
                        .isOn(nm.getIsOn())
                        .x(nm.getX())
                        .y(nm.getY())
                        .build()
                )
                .collect(Collectors.toList());
    }


}
