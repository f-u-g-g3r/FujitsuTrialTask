package com.artjomkuznetsov.deliveryfee.controllers;


import com.artjomkuznetsov.deliveryfee.exceptions.VehicleForbiddenException;
import com.artjomkuznetsov.deliveryfee.controllers.responses.FeeResponse;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/fee")
public class FeeController {

    private final CalculationService calculationService;

    public FeeController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    /**
     *
     * @param city Query parameter - city for which the weather needs to be checked.
     * @param vehicle Query parameter - transport for which the fee needs to be calculated.
     * @param dateTime Optional query parameter - if present, specifies the date and time for which weather data should be obtained,
     *                otherwise the latest weather data is taken.
     * @return EntityModel with calculated fee based on specified parameters or the error message.
     * @throws BadRequestException Throws if parameters were specified improperly.
     * @throws VehicleForbiddenException Throws if weather conditions are forbidden for the specified transport.
     */
    @Tag(name = "fee-controller-get")
    @Operation(summary = "Calculate fee",
            description = "Calculate fee based on specified city and vehicle. If dateTime parameter is not entered " +
                    "the last weather data will be taken. Returns an EntityModel with calculated fee or the error message")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ok."),
            @ApiResponse(responseCode = "400", description = "Parameters are specified improperly.",
            content = @Content),
            @ApiResponse(responseCode = "403", description = "Weather conditions are forbidden for the specified transport.",
            content = @Content)
    })
    @GetMapping
    public EntityModel<FeeResponse> getFee(
            @Parameter(description = "City for which the weather needs to be checked.", required = true)
                @RequestParam Optional<String> city,
            @Parameter(description = "Transport for which the fee needs to be calculated.", required = true)
                @RequestParam Optional<String> vehicle,
            @Parameter(description = "Specifies the date and time for which weather data should be obtained", required = false)
               @RequestParam(required = false) Optional<LocalDateTime> dateTime)
            throws BadRequestException, VehicleForbiddenException {

        return calculationService.calculateFee(city, vehicle, dateTime);
    }
}
