package com.artjomkuznetsov.deliveryfee.controllers;

import com.artjomkuznetsov.deliveryfee.exceptions.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.services.BaseFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/base-fees")
public class RegionalBaseFeeController {
    private final BaseFeeService baseFeeService;

    public RegionalBaseFeeController(BaseFeeService baseFeeService) {
        this.baseFeeService = baseFeeService;
    }

    /**
     * Retrieve all regional base fees. The response is all the regional base fees with links to the corresponding operations.
     * @return A CollectionModel containing EntityModel instances of all RegionalBaseFee entities.
     */
    @Tag(name = "regional-base-fee-controller-get")
    @Operation(
            summary = "Retrieve all regional base fees.",
            description = "The response is all the regional base fees with links to the corresponding operations.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "_embedded": {
                                            "regionalBaseFeeList": [
                                                {
                                                    "id": 0,
                                                    "city": "string",
                                                    "carFee": 0,
                                                    "bikeFee": 0,
                                                    "scooterFee": 0,
                                                    "_links": {
                                                        "self": {
                                                            "href": "string"
                                                        },
                                                        "regionalBaseFees": {
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
                                    }"""))})
            })
    @GetMapping
    public CollectionModel<EntityModel<RegionalBaseFee>> all() {
        return baseFeeService.getAllBaseFees();
    }

    /**
     * Retrieve one regional base fee for the specified city. The response is one regional base fee with links to the corresponding operations.
     * @param city Query parameter - the name of the city for which to retrieve the RegionalBaseFee.
     * @return An EntityModel representing the RegionalBaseFee entity for the specified city.
     * @throws RegionalBaseFeeNotFoundException if no RegionalBaseFee is found for the specified city.
     */
    @Tag(name = "regional-base-fee-controller-get")
    @Operation(summary = "Retrieve one regional base fee for the specified city.",
            description = "The response is one regional base fee with links to the corresponding operations.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok.", content = {@Content(mediaType = "application/json",
                             schema = @Schema(example = """
                                    {
                                        "id": 0,
                                        "city": "string",
                                        "carFee": 0,
                                        "bikeFee": 0,
                                        "scooterFee": 0,
                                        "_links": {
                                            "self": {
                                                "href": "string"
                                            },
                                            "regionalBaseFees": {
                                                "href": "string"
                                            }
                                        }
                                    }"""))}, useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "404", description = "Regional base fee for the specified city is not found.",
                            content = @Content)
            })
    @GetMapping("/{city}")
    public EntityModel<RegionalBaseFee> oneByCity(@Parameter(
            description = "The name of the city for which to retrieve the RegionalBaseFee.",
            required = true)
            @PathVariable String city) {
        return baseFeeService.getOneByCity(city);
    }

    /**
     * Update the existing regional base fee for the specified city by specifying the fields to update and their new values in the request body.
     * The response is updated regional base fee with links to the corresponding operations.
     * @param fieldsToUpdate Query parameter - Fields to update and their new values.
     * @param city Query parameter - the name of the city for which to update the RegionalBaseFee.
     * @return EntityModel containing the updated RegionalBaseFee with links to the corresponding operations.
     * @throws RegionalBaseFeeNotFoundException if no RegionalBaseFee is found for the specified city.
     */
    @Tag(name = "regional-base-fee-controller-put")
    @Operation(summary = "Update regional base fee.",
            description = "Update the existing regional base fee for the specified city by " +
                    " specifying the fields to update and their new values in the request body." +
                    " The response is updated regional base fee with links to the corresponding operations.",
            responses = { @ApiResponse(responseCode = "200", description = "Ok.", content = { @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                        "id": 0,
                                        "city": "string",
                                        "carFee": 0,
                                        "bikeFee": 0,
                                        "scooterFee": 0,
                                        "_links": {
                                            "self": {
                                                "href": "string"
                                            },
                                            "regionalBaseFees": {
                                                "href": "string"
                                            }
                                        }
                                    }"""))}),
                    @ApiResponse(responseCode = "404", description = "Regional base fee for the specified city is not found.",
                            content = @Content)
            })
    @PutMapping("/{city}")
    public EntityModel<RegionalBaseFee> updateBaseFee(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Fields to update and their new values.",
            content = {@Content(schema = @Schema(example = """
                    {
                        "carFee": 0,
                        "bikeFee": 0,
                        "scooterFee": 0
                    }
                    """))}
            ) @RequestBody Map<String, Object> fieldsToUpdate,
            @Parameter(
            description = "The name of the city for which to update the RegionalBaseFee.",
            required = true)
            @PathVariable String city) {
        return baseFeeService.updateBaseFee(fieldsToUpdate, city);
    }


}
