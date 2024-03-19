package com.artjomkuznetsov.deliveryfee.controllers;

import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.services.ExtraFeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/extra-fees")
public class ExtraFeeController {
    private final ExtraFeeService service;

    public ExtraFeeController(ExtraFeeService service) {
        this.service = service;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public CollectionModel<EntityModel<? extends ExtraWeatherFee>> all() {
        return service.all();
    }

    /**
     *
     * @return
     */
    @GetMapping("/air")
    public EntityModel<AirTemperatureConditions> getAirConditions() {
        return service.getAirConditions();
    }

    /**
     *
     * @return
     */
    @GetMapping("/wind")
    public EntityModel<WindSpeedConditions> getWindConditions() {
        return service.getWindConditions();
    }

    /**
     *
     * @return
     */
    @GetMapping("/phenomenon")
    public EntityModel<WeatherPhenomenonConditions> getPhenomenonConditions() {
        return service.getPhenomenonConditions();
    }

    /**
     *
     * @param fieldsToUpdate
     * @return
     */
    @PatchMapping("/air")
    public ResponseEntity<?> updateAirConditions(@RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updateAirTemperatureConditions(fieldsToUpdate);
    }

    /**
     *
     * @param fieldsToUpdate
     * @return
     */
    @PatchMapping("/wind")
    public ResponseEntity<?> updateWindConditions(@RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updateWindConditions(fieldsToUpdate);
    }

    /**
     *
     * @param fieldsToUpdate
     * @return
     */
    @PatchMapping("/phenomenon")
    public ResponseEntity<?> updatePhenomenonConditions(@RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updatePhenomenonConditions(fieldsToUpdate);
    }

}
