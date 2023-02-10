package com.energybox.backendcodingchallenge.service;

import com.energybox.backendcodingchallenge.domain.Sensor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.types.TypeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.Collection;


@Service
public class SensorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorService.class);
    private final SensorRepository sensorRepository;

    private final Neo4jClient neo4jClient;

    private final Driver driver;

    private final DatabaseSelectionProvider databaseSelectionProvider;

    SensorService(SensorRepository sensorRepository,
                  Neo4jClient neo4jClient,
                  Driver driver,
                  DatabaseSelectionProvider databaseSelectionProvider) {

        this.sensorRepository = sensorRepository;
        this.neo4jClient = neo4jClient;
        this.driver = driver;
        this.databaseSelectionProvider = databaseSelectionProvider;
    }

    public void createSensorToGateway(String sensorType, String gateway) {
        this.neo4jClient
                .query("" +
                        "MATCH (a:Gateway), (b:Sensor) " +
                        " WHERE a.name = '" + gateway + "' AND b.type = '" + sensorType + "'" +
                        " CREATE (b) - [:CONNECTED_TO] -> (a) RETURN a, b"
                )
                .in(database()).run();
    }


    public Collection<Sensor> fetchSensorDetailsAll() {
        return this.neo4jClient
                .query("" +
                        "MATCH (sensor:Sensor)" +
                        " RETURN sensor"
                )
                .in(database())
                .fetchAs(Sensor.class)
                .mappedBy(this::toSensorDetails)
                .all();
    }

    public Sensor fetchBySensorType(String type) {
        return this.neo4jClient
                .query("" +
                        "MATCH (sensor:Sensor) " +
                        " WHERE sensor.type = '" + type + "'" +
                        " RETURN sensor"
                )
                .in(database())
                .fetchAs(Sensor.class)
                .mappedBy(this::toSensorDetails)
                .one()
                .orElse(new Sensor());
    }

    public Sensor fetchLastReadingBySensorType(String type) {
        return this.neo4jClient
                .query("" +
                        "MATCH (sensor:Sensor) " +
                        " WHERE sensor.type = '" + type + "'" +
                        " RETURN sensor"
                )
                .in(database())
                .fetchAs(Sensor.class)
                .mappedBy(this::toSensorDetails)
                .one()
                .orElse(new Sensor());
    }


    private Sensor toSensorDetails(TypeSystem ignored, org.neo4j.driver.Record record) {
        var sensor = record.get("sensor");
        if (!sensor.isNull()) {
            return new Sensor(
                    Timestamp.valueOf(sensor.get("lastReadings").asString()),
                    Sensor.TYPE.valueOf(sensor.get("type").asString())
            );
        }
        return null;
    }

    public void createSensor(Sensor sensor) {
        this.neo4jClient
                .query("" +
                        "CREATE (n:Sensor {type: '" + sensor.getType() + "' , lastReadings: '" + sensor.getLastReadings() + "'})"
                )
                .in(database()).run();
    }


    private String database() {
        return databaseSelectionProvider.getDatabaseSelection().getValue();
    }

}
