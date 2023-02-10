package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.dto.GatewayDetailDto;
import com.energybox.backendcodingchallenge.dto.SensorResultDto;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.service.SensorService;
import com.energybox.backendcodingchallenge.util.ObjectMapperHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/sensors")
public class SensorController {

    private final SensorService service;
    private final GatewayService gatewayService;

    @Autowired
    ObjectMapperHelper objectMapperHelper;

    public SensorController(SensorService service, ObjectMapperHelper objectMapperHelper, GatewayService gatewayService) {
        this.objectMapperHelper = objectMapperHelper;
        this.service = service;
        this.gatewayService = gatewayService;
    }

    @ApiOperation(value = "create a sensor", response = Sensor.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create(
            @RequestBody Object comment
    ) throws IOException, InterruptedException {

        Sensor request = objectMapperHelper.getObjectMapper().convertValue(comment, Sensor.class);

        if (request.getType() == null) {
            return new ResponseEntity<>("Sensor type not acceptable, Accepted sensor types are:" + Sensor.printTypes(), HttpStatus.BAD_REQUEST);
        } else if (Sensor.findByName(String.valueOf(request.getType())) == null) {
            return new ResponseEntity<>("Empty Sensor type not acceptable, Accepted sensor types are: " + Sensor.printTypes(), HttpStatus.BAD_REQUEST);
        } else {
            service.createSensor(request);
            return new ResponseEntity<>("Sensor created with type: " + request.getType(), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "create a relationship")
    @RequestMapping(value = "/createrelation/{sensortype}/{gatewayname}", method = RequestMethod.POST)
    public ResponseEntity<Object> create(
            @PathVariable(value = "sensortype") String sensorType,
            @PathVariable(value = "gatewayname") String gatewayName
    ) throws IOException, InterruptedException {

        if (sensorType == null || gatewayName == null) {
            return new ResponseEntity<>("Sensor or gateway missing:", HttpStatus.BAD_REQUEST);
        } else if (Sensor.findByName(sensorType) == null) {
            return new ResponseEntity<>("Sensor type not acceptable, Accepted sensor types are: " + Sensor.printTypes(), HttpStatus.BAD_REQUEST);
        } else if (gatewayService.findByName(gatewayName) == null) {
            return new ResponseEntity<>("Gateway doesn't exists for connection ", HttpStatus.BAD_REQUEST);
        }else if (service.fetchBySensorType(Sensor.findByName(sensorType).toString()).getType() == null) {
            return new ResponseEntity<>("Sensor doesn't exists for connection ", HttpStatus.BAD_REQUEST);
        } else {
            service.createSensorToGateway(Sensor.findByName(sensorType).toString(), gatewayName);
            return new ResponseEntity<>("Relationship created for sensor: " + Sensor.findByName(sensorType).toString() + " and gateway: " + gatewayName, HttpStatus.OK);
        }
    }


    @GetMapping("/get/all")
    public Collection<Sensor> findAll() {
        return service.fetchSensorDetailsAll();
    }

    @GetMapping("/get/{sensortype}")
    public Sensor fetchBySensorType(@PathVariable(value = "sensortype") String sensorType) {
        return service.fetchBySensorType(Sensor.findByName(sensorType).toString());
    }

    @GetMapping("/lastreading/{sensortype}")
    public String fetchLastReadingBySensorType(@PathVariable(value = "sensortype") String sensorType) {
        Sensor sen = service.fetchLastReadingBySensorType(Sensor.findByName(sensorType).toString());
        return sen.getLastReadings().toString();
    }


}
