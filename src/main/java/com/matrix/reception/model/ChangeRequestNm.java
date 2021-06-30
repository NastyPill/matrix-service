package com.matrix.reception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestNm implements Serializable {

    @JsonProperty("isOn")
    @NonNull
    Boolean isOn;

    @JsonProperty("x")
    @NonNull
    @Min(1)
    @Max(8)
    Integer x;

    @JsonProperty("y")
    @NonNull
    @Min(1)
    @Max(8)
    Integer y;
}
