package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.dto.GatewayDetailDto;
import com.energybox.backendcodingchallenge.dto.GatewayResultDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.types.TypeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GatewayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayService.class);
    private final GatewayRepository gatewayRepository;

    private final Neo4jClient neo4jClient;

    private final Driver driver;

    private final DatabaseSelectionProvider databaseSelectionProvider;

    GatewayService(GatewayRepository gatewayRepository,
                   Neo4jClient neo4jClient,
                   Driver driver,
                   DatabaseSelectionProvider databaseSelectionProvider) {

        this.gatewayRepository = gatewayRepository;
        this.neo4jClient = neo4jClient;
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    public List<GatewayResultDto> searchAllByName(String name) {
        return this.gatewayRepository.findSearchResults(name)
                .stream()
                .map(GatewayResultDto::new)
                .collect(Collectors.toList());
    }

    public Gateway findByName(String name) {
        return this.gatewayRepository.findByName(name);
    }

    public Collection<Gateway> fetchGatewayAll() {
        return this.neo4jClient
                .query("" +
                        "MATCH (gateway:Gateway) " +
                        "RETURN gateway"
                )
                .in(database())
                .fetchAs(Gateway.class)
                .mappedBy(this::toSingleGateway)
                .all();
    }

    public Collection<GatewayDetailDto> fetchBySensorTypeElectrical(String type) {
        return this.neo4jClient
                .query("" +
                        "MATCH (sensor:Sensor)-[:CONNECTED_TO]->(gateway:Gateway) " +
                        " WHERE sensor.type = '" + type + "'" +
                        " RETURN sensor, gateway"
                )
                .in(database())
                .fetchAs(GatewayDetailDto.class)
                .mappedBy(this::toGatewayDetails)
                .all();
    }

    public Collection<GatewayDetailDto> findAllSensorwithGateway() {
        return this.neo4jClient
                .query("" +
                        "MATCH (sensor:Sensor)-[:CONNECTED_TO]->(gateway:Gateway) " +
                        " RETURN sensor, gateway"
                )
                .in(database())
                .fetchAs(GatewayDetailDto.class)
                .mappedBy(this::toGatewayDetails)
                .all();
    }


    public void createGateway(String name) {
        this.neo4jClient
                .query("" +
                        "CREATE (a:Gateway) SET a.name ='" + name + "' RETURN a"
                )
                .in(database()).run();
    }


    private GatewayDetailDto toGatewayDetails(TypeSystem ignored, org.neo4j.driver.Record record) {
        var gateway = record.get("gateway");
        List<Sensor> sensorsList = Collections.emptyList();
        if (!record.get("sensor").isNull()) {
            var sensor = record.get("sensor");
            if (!sensor.isNull()) {
                sensorsList = new ArrayList<>();
                sensorsList.add(new Sensor(
                        Timestamp.valueOf(sensor.get("lastReadings").asString()),
                        Sensor.TYPE.valueOf(sensor.get("type").asString())
                ));
            }
        }

        return new GatewayDetailDto(
                gateway.get("name").asString(),
                sensorsList

        );
    }

    private Gateway toSingleGateway(TypeSystem ignored, org.neo4j.driver.Record record) {
        var gateway = record.get("gateway");
        return new Gateway(gateway.get("name").asString());
    }


    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }


}
