package com.artjomkuznetsov.deliveryfee.assemblers;


import com.artjomkuznetsov.deliveryfee.controllers.ExtraFeeController;
import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ExtraFeeModelAssembler implements RepresentationModelAssembler<ExtraWeatherFee, EntityModel<ExtraWeatherFee>> {

    public EntityModel<AirTemperatureConditions> toModel(@NonNull AirTemperatureConditions airConditions) {
        return EntityModel.of(airConditions,
                linkTo(methodOn(ExtraFeeController.class).getAirConditions()).withSelfRel(),
                linkTo(methodOn(ExtraFeeController.class).all()).withRel("extraFees"));
    }

    public EntityModel<WindSpeedConditions> toModel(@NonNull WindSpeedConditions windConditions) {
        return EntityModel.of(windConditions,
                linkTo(methodOn(ExtraFeeController.class).getWindConditions()).withSelfRel(),
                linkTo(methodOn(ExtraFeeController.class).all()).withRel("extraFees"));
    }

    public EntityModel<WeatherPhenomenonConditions> toModel(@NonNull WeatherPhenomenonConditions phenomenonConditions) {
        return EntityModel.of(phenomenonConditions,
                linkTo(methodOn(ExtraFeeController.class).getPhenomenonConditions()).withSelfRel(),
                linkTo(methodOn(ExtraFeeController.class).all()).withRel("extraFees"));
    }

    @Override
    public EntityModel<ExtraWeatherFee> toModel(@NonNull ExtraWeatherFee entity) {
        return null;
    }
}
