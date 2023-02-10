package com.energybox.backendcodingchallenge.domain;

import java.util.List;
import java.util.Objects;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;


@Node
public class Gateway {

    public Gateway() {
    }

    @Id
    private String name;

    private List<Sensor> sensors;

    public Gateway(String name, List<Sensor> sensors) {
        this.name = name;
        this.sensors = sensors;
    }

    public Gateway(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

}
