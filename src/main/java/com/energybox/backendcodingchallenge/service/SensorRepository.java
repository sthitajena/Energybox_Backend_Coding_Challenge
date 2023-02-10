package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorRepository extends Repository<Sensor, Long> {

    @Query("MATCH (sensor:Sensor) WHERE sensor.type CONTAINS type RETURN sensor")
    List<Sensor> findSearchResults(@Param("type") String name);
}

