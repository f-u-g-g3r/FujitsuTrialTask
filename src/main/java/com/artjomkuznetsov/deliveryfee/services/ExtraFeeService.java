package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.utils.Patcher;
import com.artjomkuznetsov.deliveryfee.assemblers.ExtraFeeModelAssembler;
import com.artjomkuznetsov.deliveryfee.controllers.ExtraFeeController;
import com.artjomkuznetsov.deliveryfee.exceptions.ExtraWeatherConditionsNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.AirTemperatureConditionsRepository;
import com.artjomkuznetsov.deliveryfee.repositories.WeatherPhenomenonConditionsRepository;
import com.artjomkuznetsov.deliveryfee.repositories.WindSpeedConditionsRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ExtraFeeService {
    private final AirTemperatureConditionsRepository airRepository;
    private final WindSpeedConditionsRepository windRepository;
    private final WeatherPhenomenonConditionsRepository phenomenonRepository;
    private final ExtraFeeModelAssembler assembler;
    private final Patcher patcher;

    public ExtraFeeService(AirTemperatureConditionsRepository airRepository, WindSpeedConditionsRepository windRepository, WeatherPhenomenonConditionsRepository phenomenonRepository, ExtraFeeModelAssembler assembler, Patcher patcher) {
        this.airRepository = airRepository;
        this.windRepository = windRepository;
        this.phenomenonRepository = phenomenonRepository;
        this.assembler = assembler;
        this.patcher = patcher;
    }

    /**
     *
     * @return
     */
    public CollectionModel<EntityModel<? extends ExtraWeatherFee>> all() {
        EntityModel<AirTemperatureConditions> airConditions = assembler.toModel(airRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
        EntityModel<WindSpeedConditions> windConditions = assembler.toModel(windRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
        EntityModel<WeatherPhenomenonConditions> phenomenonConditions = assembler.toModel(phenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));

        List<EntityModel<? extends ExtraWeatherFee>> entityModels = List.of(airConditions, windConditions, phenomenonConditions);

        return CollectionModel.of(entityModels, linkTo(methodOn(ExtraFeeController.class).all()).withSelfRel());
    }

    /**
     *
     * @return
     */
    public EntityModel<AirTemperatureConditions> getAirConditions() {
        return assembler.toModel(airRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     *
     * @return
     */
    public EntityModel<WindSpeedConditions> getWindConditions() {
        return assembler.toModel(windRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     *
     * @return
     */
    public EntityModel<WeatherPhenomenonConditions> getPhenomenonConditions() {
        return assembler.toModel(phenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     *
     * @param fields
     * @return
     */
    public ResponseEntity<?> updateAirConditions(Map<String, Object> fields) {
        AirTemperatureConditions updatedAir = airRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        airRepository.save(Patcher.patch(updatedAir, fields));

        EntityModel<AirTemperatureConditions> entityModel = assembler.toModel(updatedAir);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     *
     * @param fields
     * @return
     */
    public ResponseEntity<?> updateWindConditions(Map<String, Object> fields) {
        WindSpeedConditions updatedWind = windRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        windRepository.save(Patcher.patch(updatedWind, fields));

        EntityModel<WindSpeedConditions> entityModel = assembler.toModel(updatedWind);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     *
     * @param fields
     * @return
     */
    public ResponseEntity<?> updatePhenomenonConditions(Map<String, Object> fields) {
        WeatherPhenomenonConditions updatedPhenomenon = phenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        phenomenonRepository.save(Patcher.patch(updatedPhenomenon, fields));

        EntityModel<WeatherPhenomenonConditions> entityModel = assembler.toModel(updatedPhenomenon);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }
}
