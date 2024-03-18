package com.artjomkuznetsov.deliveryfee.controllers;


import com.artjomkuznetsov.deliveryfee.exceptions.VehicleForbiddenException;
import com.artjomkuznetsov.deliveryfee.controllers.responses.FeeResponse;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class FeeController {

    private final CalculationService calculationService;

    public FeeController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    /**
     *
     * @param city Query parameter - the city for which you need to check the weather.
     * @param vehicle Query parameter - transport for which you need to calculate the fee.
     * @param dateTime Optional query parameter - if present, specifies the date and time for which weather data should be obtained,
     *                otherwise the latest weather data is taken.
     * @return EntityModel with calculated fee based on specified parameters or the error message.
     * @throws BadRequestException Throws if parameters were specified improperly.
     * @throws VehicleForbiddenException Throws if weather conditions are forbidden for the specified transport.
     */
    @GetMapping
    public EntityModel<FeeResponse> getFee(@RequestParam Optional<String> city,
                                           @RequestParam Optional<String> vehicle,
                                           @RequestParam(required = false) Optional<LocalDateTime> dateTime)
            throws BadRequestException, VehicleForbiddenException {

        return calculationService.calculateFee(city, vehicle, dateTime);
    }
}
