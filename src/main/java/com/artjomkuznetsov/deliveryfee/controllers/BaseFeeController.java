package com.artjomkuznetsov.deliveryfee.controllers;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.services.BaseFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/base-fees")
public class BaseFeeController {
    private final BaseFeeService baseFeeService;

    public BaseFeeController(BaseFeeService baseFeeService) {
        this.baseFeeService = baseFeeService;
    }

    /**
     *
     * @return
     */
    @Tag(name = "base-fee-controller-get")
    @Operation(summary = "Retrieve all regional base fees.",
            description = "The response is all the regional base fees with links to the corresponding operations.")
    @GetMapping
    public CollectionModel<EntityModel<RegionalBaseFee>> all() {
        return baseFeeService.getAllBaseFees();
    }

    /**
     *
     * @param city
     * @return
     */
    @Tag(name = "base-fee-controller-get")
    @Operation(summary = "Retrieve one regional base fee for the specified city.",
            description = "The response is one regional base fee with links to the corresponding operations.")
    @GetMapping("/{city}")
    public EntityModel<RegionalBaseFee> oneByCity(@PathVariable String city) {
        return baseFeeService.getOneByCity(city);
    }

    /**
     *
     * @param fieldsToUpdate
     * @param city
     * @return
     */
    @Tag(name = "base-fee-controller-patch")
    @Operation(summary = "Update regional base fee.",
            description = "Update an existing regional base fee for the specified city." +
                    " The response is updated regional base fee with links to the corresponding operations")
    @PatchMapping("/{city}")
    public ResponseEntity<?> updateBaseFee(@RequestBody Map<String, Object> fieldsToUpdate, @PathVariable String city) {
        return baseFeeService.updateBaseFee(fieldsToUpdate, city);
    }


}
