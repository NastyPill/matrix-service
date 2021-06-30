package com.matrix.operation.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkReportMessage {

    private Long time;

    private Integer requestCount;

}
