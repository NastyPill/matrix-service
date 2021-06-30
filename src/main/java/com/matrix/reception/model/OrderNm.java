package com.matrix.reception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderNm implements Serializable {

    @Valid
    @JsonProperty("requests")
    @NonNull
    List<ChangeRequestNm> requestNmList = null;

}
