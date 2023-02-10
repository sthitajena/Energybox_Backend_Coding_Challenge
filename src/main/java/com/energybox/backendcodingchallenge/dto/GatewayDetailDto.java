package com.energybox.backendcodingchallenge.dto;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

import java.util.List;
import java.util.Objects;

public class GatewayDetailDto {

    private final String name;

    private final List<Sensor> sensors;

    public GatewayDetailDto(String name, List<Sensor> sensors) {
        this.name = name;
        this.sensors = sensors;
    }

    public String getName() {
        return name;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gateway that = (Gateway) o;
        return Objects.equals(name, that.getName()) && Objects.equals(sensors, that.getSensors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sensors);
    }
}
