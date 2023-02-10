package com.energybox.backendcodingchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Node
public class Sensor {
    @Id @GeneratedValue
    private Long id;

    public enum TYPE {
        @JsonProperty("temperature")
        TEMPERATURE,
        @JsonProperty("humidity")
        HUMIDITY,
        @JsonProperty("electricity")
        ELECTRICITY
    }

    public Timestamp getLastReadings() {
        return lastReadings;
    }

    private Timestamp lastReadings = new Timestamp(System.currentTimeMillis());

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    private TYPE type;

    public Sensor() {
    }

    public Sensor(Timestamp lastReadings, TYPE type) {
        this.lastReadings = lastReadings;
        this.type = type;
    }

    public Sensor(TYPE type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor that = (Sensor) o;
        return Objects.equals(type, that.type) && Objects.equals(lastReadings, that.lastReadings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lastReadings);
    }

    @JsonCreator
    public static TYPE findByName(String name) {
        TYPE result = null;
        for (TYPE type : TYPE.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                result = type;
                break;
            }
        }
        return result;
    }

    public static String printTypes() {
        return Stream.of(TYPE.values()).
                map(TYPE::name).
                collect(Collectors.joining(","));
    }
}
