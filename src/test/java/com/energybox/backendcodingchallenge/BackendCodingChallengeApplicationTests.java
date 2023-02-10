package com.energybox.backendcodingchallenge;

import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.service.SensorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class BackendCodingChallengeApplicationTests {

    private static final String PASSWORD = "foobar";

    @Container
    private static final Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>()
            .withAdminPassword(PASSWORD);

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> PASSWORD);
        registry.add("spring.data.neo4j.database", () -> "neo4j");
    }

    @BeforeEach
    void setup(@Autowired Driver driver) {
        try (Session session = driver.session()) {
            Timestamp lastReadings = new Timestamp(System.currentTimeMillis());
            session.writeTransaction(tx -> {
                tx.run("MATCH (n) DETACH DELETE n");
                tx.run(""
                        + "CREATE (g1:Gateway {name:'Gateway1'})\n"
                        + "CREATE (g2:Gateway {name:'Gateway2'})\n"
                        + "CREATE (sensor1:Sensor {type:'ELECTRICITY', lastReadings:'"+lastReadings+"'})\n"
                        + "CREATE (sensor2:Sensor {type:'HUMIDITY', lastReadings:'"+lastReadings+"'}) -[:CONNECTED_TO]-> (g1)\n");
                return null;
            });
        }
    }

    @Test
    public void searches_gateway_by_name(@Autowired GatewayService service) {
        String name = "Gateway1";
        assertThat(service.findByName(name))
                .extracting(mr -> mr.getName()).isEqualTo("Gateway1");
        assertThat(service.fetchGatewayAll()).hasSize(2);
    }

    @Test
    public void searches_sensor_by_type(@Autowired SensorService service) {
        String type = "ELECTRICITY";
        assertThat(service.fetchBySensorType(type))
                .extracting(mr -> mr.getType().toString()).isEqualTo("ELECTRICITY");
    }

    @Test
    public void searches_allSensor(@Autowired SensorService service) {

        assertThat(service.fetchSensorDetailsAll()).hasSize(2);
        assertThat(service.fetchSensorDetailsAll())
                .extracting(mr -> mr.getType().toString()).containsExactlyInAnyOrder("ELECTRICITY", "HUMIDITY");
    }

    @Test
    public void searches_sesor_with_gateway(@Autowired GatewayService service) {
        assertThat(service.findAllSensorwithGateway())
                .hasSize(1);
        assertThat(service.findAllSensorwithGateway())
                .extracting(mr -> mr.getSensors().get(0).getType().toString()).containsExactly("HUMIDITY");
    }


}
