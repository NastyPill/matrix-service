package com.matrix.reception.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.matrix.reception.model.ChangeRequest;
import com.matrix.reception.model.Order;
import org.testng.annotations.Test;

import static com.matrix.reception.model.mapper.Nm2DomainMapper.mapToOrder;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Nm2DomainMapperTest {

    @Test
    public void shouldMapFromJson() throws JsonProcessingException {
        Order expectedOrder = new Order(asList(new ChangeRequest(false, 1, 1), new ChangeRequest(true, 1, 2)));
        Order actualOrder = mapToOrder("{\"requests\":[{\"isOn\":false,\"x\":1,\"y\":1},{\"isOn\":true,\"x\":1,\"y\":2}]}");
        assertThat(expectedOrder, is(actualOrder));
    }

}
