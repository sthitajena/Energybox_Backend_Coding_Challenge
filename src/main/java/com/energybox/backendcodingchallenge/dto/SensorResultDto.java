package com.energybox.backendcodingchallenge.dto;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

import java.util.List;
import java.util.Objects;

public class SensorResultDto {

    private final List<Sensor> sensors;

    public SensorResultDto(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gateway that = (Gateway) o;
        return  Objects.equals(sensors, that.getSensors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensors);
    }
}
