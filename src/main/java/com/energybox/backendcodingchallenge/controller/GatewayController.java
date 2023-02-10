package com.energybox.backendcodingchallenge.controller;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.dto.GatewayDetailDto;
import com.energybox.backendcodingchallenge.service.GatewayService;
import com.energybox.backendcodingchallenge.util.ObjectMapperHelper;
import io.swagger.annotations.ApiOperation;
import net.logstash.logback.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;


@RestController
@RequestMapping( value = "/gateways" )
public class GatewayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);
    private final GatewayService service;

    @Autowired
    ObjectMapperHelper objectMapperHelper;

    public GatewayController( GatewayService service ,ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
        this.service = service;
    }

    @ApiOperation( value = "create a gateway", response = Gateway.class )
    @RequestMapping( value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> create(
            @RequestBody Object comment
    ) throws IOException, InterruptedException {

        Gateway request = objectMapperHelper.getObjectMapper().convertValue(comment, Gateway.class);

        if (StringUtils.isBlank(request.getName())) {
            return new ResponseEntity<>("Gateway name can't be empty.", HttpStatus.BAD_REQUEST);
        } else {
            service.createGateway(request.getName());
            return new ResponseEntity<>("Gateway created with name: " + request.getName(), HttpStatus.OK);
        }
    }

    @GetMapping("/get/all")
    public Collection<Gateway> findAll() {
        return service.fetchGatewayAll();
    }

    @GetMapping("/get/electrical")
    public Collection<GatewayDetailDto> fetchByElectricitySensorType() {
        return service.fetchBySensorTypeElectrical(Sensor.findByName("ELECTRICITY").toString());
    }

    @GetMapping("/get/allsensorwithgateway")
    public Collection<GatewayDetailDto> findAllsensorwithGateway() {
        return service.findAllSensorwithGateway();
    }
}
