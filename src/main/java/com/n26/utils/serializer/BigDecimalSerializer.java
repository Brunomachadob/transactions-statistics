package com.n26.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;


@JsonComponent
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private int scale = 2;
    private int roundingMode = BigDecimal.ROUND_HALF_UP;

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.setScale(this.scale, this.roundingMode).toString());
    }
}