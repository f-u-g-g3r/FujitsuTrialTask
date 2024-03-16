package com.artjomkuznetsov.deliveryfee.controllers;


import com.artjomkuznetsov.deliveryfee.controllers.exceptions.vehicleForbidden.VehicleForbiddenException;
import com.artjomkuznetsov.deliveryfee.controllers.responses.FeeResponse;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param city Parameter from request - the city for which you need to check the weather.
     * @param vehicle Parameter from request - transport for which you need to calculate the fee.
     * @return EntityModel with calculated fee based on specified parameters or the error message.
     * @throws BadRequestException Throws if parameters were specified improperly.
     * @throws VehicleForbiddenException Throws if weather conditions are forbidden for the specified transport.
     */
    @GetMapping
    public EntityModel<FeeResponse> getFee(@RequestParam Optional<String> city,
                                           @RequestParam Optional<String> vehicle)
            throws BadRequestException, VehicleForbiddenException {
        FeeResponse feeResponse = new FeeResponse(calculationService.calculateFee(city, vehicle));
        return EntityModel.of(feeResponse, linkTo(methodOn(FeeController.class).getFee(city, vehicle)).withSelfRel());
    }
}
