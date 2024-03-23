package com.artjomkuznetsov.deliveryfee.controllers;

import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.exceptions.ExtraWeatherConditionsNotFoundException;
import com.artjomkuznetsov.deliveryfee.services.ExtraFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/extra-fees")
public class ExtraFeeController {
    private final ExtraFeeService service;
    public ExtraFeeController(ExtraFeeService service) {
        this.service = service;
    }

    /**
     * Retrieve all available extra weather conditions and their associated fees.
     * @return A CollectionModel containing EntityModel instances of all available extra weather conditions and their fees with links to the corresponding operations.
     */
    @Tag(name = "extra-fee-controller-get")
    @Operation(summary = "Retrieve all extra weather conditions and their associated fees",
            description = "The response is all the extra weather conditions and their associated fees with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "_embedded": {
                                        "airTemperatureConditionsList": [
                                            {
                                                "id": 0,
                                                "vehicleTypes": [
                                                    "string"
                                                ],
                                                "lessThan": 0,
                                                "lessThanFee": 0,
                                                "betweenMin": 0,
                                                "betweenMax": 0,
                                                "betweenFee": 0,
                                                "_links": {
                                                    "self": {
                                                        "href": "string"
                                                    },
                                                    "extraFees": {
                                                        "href": "string"
                                                    }
                                                }
                                            }
                                        ],
                                        "windSpeedConditionsList": [
                                            {
                                                "id": 0,
                                                "vehicleTypes": [
                                                    "string"
                                                ],
                                                "betweenMin": 0,
                                                "betweenMax": 0,
                                                "betweenFee": 0,
                                                "forbiddenSpeed": 0,
                                                "_links": {
                                                    "self": {
                                                        "href": "string"
                                                    },
                                                    "extraFees": {
                                                        "href": "string"
                                                    }
                                                }
                                            }
                                        ],
                                        "weatherPhenomenonConditionsList": [
                                          {
                                            "id": 0,
                                            "vehicleTypes": [
                                              "string"
                                            ],
                                            "snowOrSleetFee": 0,
                                            "rainFee": 0,
                                            "forbiddenPhenomenons": [
                                              "string"
                                            ],
                                            "_links": {
                                              "self": {
                                                "href": "string"
                                              },
                                              "extraFees": {
                                                "href": "string"
                                              }
                                            }
                                          }
                                        ]
                                    },
                                    "_links": {
                                        "self": {
                                            "href": "string"
                                        }
                                    }
                                }"""
                    ))})
            })
    @GetMapping
    public CollectionModel<EntityModel<? extends ExtraWeatherFee>> all() {
        return service.all();
    }

    /**
     * Retrieve the entity model representing the air temperature conditions.
     * @return An EntityModel containing the air temperature conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the air temperature conditions are not found.
     */
    @Tag(name = "extra-fee-controller-get")
    @Operation(summary = "Retrieve the air temperature conditions.",
            description = "The response is air temperature conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                        "string"
                                    ],
                                    "lessThan": 0,
                                    "lessThanFee": 0,
                                    "betweenMin": 0,
                                    "betweenMax": 0,
                                    "betweenFee": 0,
                                    "_links": {
                                        "self": {
                                            "href": "string"
                                        },
                                        "extraFees": {
                                            "href": "string"
                                        }
                                    }
                                }"""
                    ))}),
                    @ApiResponse(responseCode = "500", content = @Content,
                            description = "Extra fees for air temperature conditions haven't been configured. The database may not be configured correctly.")
            })
    @GetMapping("/air-temperature-conditions")
    public EntityModel<AirTemperatureConditions> getAirConditions() {
        return service.getAirConditions();
    }

    /**
     * Retrieve the entity model representing the wind speed conditions.
     * @return An EntityModel containing the wind speed conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the wind speed conditions are not found.
     */
    @Tag(name = "extra-fee-controller-get")
    @Operation(summary = "Retrieve the wind speed conditions.",
            description = "The response is wind speed conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                        "string"
                                    ],
                                    "betweenMin": 0,
                                    "betweenMax": 0,
                                    "betweenFee": 0,
                                    "forbiddenSpeed": 0,
                                    "_links": {
                                        "self": {
                                            "href": "string"
                                        },
                                        "extraFees": {
                                            "href": "string"
                                        }
                                    }
                                }"""
                    ))}), @ApiResponse(responseCode = "500", content = @Content,
                    description = "Extra fees for wind speed conditions haven't been configured. The database may not be configured correctly.")

            })
    @GetMapping("/wind-speed-conditions")
    public EntityModel<WindSpeedConditions> getWindConditions() {
        return service.getWindConditions();
    }

    /**
     * Retrieve the entity model representing the weather phenomenon conditions.
     * @return An EntityModel containing the weather phenomenon conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the weather phenomenon conditions are not found.
     */
    @Tag(name = "extra-fee-controller-get")
    @Operation(summary = "Retrieve the weather phenomenon conditions.",
            description = "The response is weather phenomenon conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                      "string"
                                    ],
                                    "snowOrSleetFee": 0,
                                    "rainFee": 0,
                                    "forbiddenPhenomenons": [
                                      "string"
                                    ],
                                    "_links": {
                                      "self": {
                                        "href": "string"
                                      },
                                      "extraFees": {
                                        "href": "string"
                                      }
                                    }
                                }"""
                    ))}), @ApiResponse(responseCode = "500", content = @Content,
                    description = "Extra fees for weather phenomenon conditions haven't been configured. The database may not be configured correctly.")
            })
    @GetMapping("/phenomenon-conditions")
    public EntityModel<WeatherPhenomenonConditions> getPhenomenonConditions() {
        return service.getPhenomenonConditions();
    }

    /**
     * Updates the air temperature conditions by specifying the fields to update and their new values in the request body.
     * @param fieldsToUpdate Query parameter - fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated air temperature conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the air temperature conditions are not found.
     */
    @Tag(name = "extra-fee-controller-put")
    @Operation(summary = "Update air temperature conditions.",
            description = "Update the air temperature conditions by specifying the fields to update and their new values in the request body." +
                    " The response is air temperature conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                        "string"
                                    ],
                                    "lessThan": 0,
                                    "lessThanFee": 0,
                                    "betweenMin": 0,
                                    "betweenMax": 0,
                                    "betweenFee": 0,
                                    "_links": {
                                        "self": {
                                            "href": "string"
                                        },
                                        "extraFees": {
                                            "href": "string"
                                        }
                                    }
                                }"""
                    ))}),
                    @ApiResponse(responseCode = "500", content = @Content,
                            description = "Extra fees for air temperature conditions haven't been configured. The database may not be configured correctly.")
            })
    @PutMapping("/air-temperature-conditions")
    public EntityModel<AirTemperatureConditions> updateAirConditions(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Fields to update and their new values.",
            content = {@Content(schema = @Schema(example = """
                    {
                        "vehicleTypes": [
                            "string"
                        ],
                        "lessThan": 0,
                        "lessThanFee": 0,
                        "betweenMin": 0,
                        "betweenMax": 0,
                        "betweenFee": 0
                    }
                    """))}
    ) @RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updateAirTemperatureConditions(fieldsToUpdate);
    }

    /**
     * Update the wind speed conditions by specifying the fields to update and their new values in the request body.
     * @param fieldsToUpdate Query parameter - fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated wind speed conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the wind speed conditions are not found.
     */
    @Tag(name = "extra-fee-controller-put")
    @Operation(summary = "Update wind speed conditions.",
            description = "Update the wind speed conditions by specifying the fields to update and their new values in the request body." +
                    " The response is wind speed conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                        "string"
                                    ],
                                    "betweenMin": 0,
                                    "betweenMax": 0,
                                    "betweenFee": 0,
                                    "forbiddenSpeed": 0,
                                    "_links": {
                                        "self": {
                                            "href": "string"
                                        },
                                        "extraFees": {
                                            "href": "string"
                                        }
                                    }
                                }"""
                    ))}), @ApiResponse(responseCode = "500", content = @Content,
                    description = "Extra fees for wind speed conditions haven't been configured. The database may not be configured correctly.")
            })
    @PutMapping("/wind-speed-conditions")
    public ResponseEntity<?> updateWindConditions(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Fields to update and their new values.",
            content = {@Content(schema = @Schema(example = """
                    {
                        "vehicleTypes": [
                            "string"
                        ],
                        "betweenMin": 0,
                        "betweenMax": 0,
                        "betweenFee": 0,
                        "forbiddenSpeed": 0
                    }
                    """))}
    ) @RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updateWindConditions(fieldsToUpdate);
    }

    /**
     * Update the weather phenomenon conditions by specifying the fields to update and their new values in the request body.
     * @param fieldsToUpdate Query parameter - fields to update and their new values.
     * @return EntityModel indicating the success of the update operation and containing the updated weather phenomenon conditions with links to the corresponding operations.
     * @throws ExtraWeatherConditionsNotFoundException if the weather phenomenon conditions are not found.
     */
    @Tag(name = "extra-fee-controller-put")
    @Operation(summary = "Update weather phenomenon conditions.",
            description = "Update the weather phenomenon conditions by specifying the fields to update and their new values in the request body." +
                    " The response is weather phenomenon conditions with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                    schema = @Schema(example = """
                                {
                                    "id": 0,
                                    "vehicleTypes": [
                                      "string"
                                    ],
                                    "snowOrSleetFee": 0,
                                    "rainFee": 0,
                                    "forbiddenPhenomenons": [
                                      "string"
                                    ],
                                    "_links": {
                                      "self": {
                                        "href": "string"
                                      },
                                      "extraFees": {
                                        "href": "string"
                                      }
                                    }
                                }"""
                    ))}), @ApiResponse(responseCode = "500", content = @Content,
                    description = "Extra fees for weather phenomenon conditions haven't been configured. The database may not be configured correctly.")
            })
    @PutMapping("/phenomenon-conditions")
    public ResponseEntity<?> updatePhenomenonConditions(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Fields to update and their new values.",
            content = {@Content(schema = @Schema(example = """
                    {
                        "vehicleTypes": [
                          "string"
                        ],
                        "snowOrSleetFee": 0,
                        "rainFee": 0,
                        "forbiddenPhenomenons": [
                          "string"
                        ]
                    }
                    """))}
    ) @RequestBody Map<String, Object> fieldsToUpdate) {
        return service.updatePhenomenonConditions(fieldsToUpdate);
    }

}
