package com.matrix.reception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ChangeRequest {
    private Boolean isOn;
    private Integer x;
    private Integer y;
}
