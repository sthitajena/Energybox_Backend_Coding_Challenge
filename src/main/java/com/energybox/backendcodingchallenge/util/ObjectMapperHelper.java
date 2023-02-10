package com.energybox.backendcodingchallenge.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperHelper {

    private final ObjectMapper objectMapper;

    public ObjectMapperHelper() {
        this.objectMapper = JsonMapper.builder()
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
