package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.utils.Updater;
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

    public ExtraFeeService(AirTemperatureConditionsRepository airRepository, WindSpeedConditionsRepository windRepository, WeatherPhenomenonConditionsRepository phenomenonRepository, ExtraFeeModelAssembler assembler) {
        this.airRepository = airRepository;
        this.windRepository = windRepository;
        this.phenomenonRepository = phenomenonRepository;
        this.assembler = assembler;
    }

    /**
     * Retrieves all available extra weather conditions and their associated fees.
     * @return A CollectionModel containing EntityModel instances of all available extra weather conditions and their fees with links to the corresponding operations.
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
     * Retrieves the entity model representing the air temperature conditions.
     * @return An EntityModel containing the air temperature conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the air temperature conditions are not found.
     */
    public EntityModel<AirTemperatureConditions> getAirConditions() {
        return assembler.toModel(airRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     * Retrieves the entity model representing the wind speed conditions.
     * @return An EntityModel containing the wind speed conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the wind speed conditions are not found.
     */
    public EntityModel<WindSpeedConditions> getWindConditions() {
        return assembler.toModel(windRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     * Retrieves the entity model representing the weather phenomenon conditions.
     * @return An EntityModel containing the weather phenomenon conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the weather phenomenon conditions are not found.
     */
    public EntityModel<WeatherPhenomenonConditions> getPhenomenonConditions() {
        return assembler.toModel(phenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new));
    }

    /**
     * Updates the air temperature conditions with the provided fields.
     * @param fields A Map containing the fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated air temperature conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the air temperature conditions are not found.
     */
    public EntityModel<AirTemperatureConditions> updateAirTemperatureConditions(Map<String, Object> fields) {
        AirTemperatureConditions updatedTemperature = airRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        airRepository.save(Updater.updateEntity(updatedTemperature, fields));

        return assembler.toModel(updatedTemperature);
    }

    /**
     * Updates the wind speed conditions with the provided fields.
     * @param fields A Map containing the fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated wind speed conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the wind speed conditions are not found.
     */
    public ResponseEntity<?> updateWindConditions(Map<String, Object> fields) {
        WindSpeedConditions updatedWind = windRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        windRepository.save(Updater.updateEntity(updatedWind, fields));

        EntityModel<WindSpeedConditions> entityModel = assembler.toModel(updatedWind);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    /**
     * Updates the weather phenomenon conditions with the provided fields.
     * @param fields A Map containing the fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated weather phenomenon conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the weather phenomenon conditions are not found.
     */
    public ResponseEntity<?> updatePhenomenonConditions(Map<String, Object> fields) {
        WeatherPhenomenonConditions updatedPhenomenon = phenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);

        phenomenonRepository.save(Updater.updateEntity(updatedPhenomenon, fields));

        EntityModel<WeatherPhenomenonConditions> entityModel = assembler.toModel(updatedPhenomenon);
        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }
}
