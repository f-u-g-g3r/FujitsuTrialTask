package com.artjomkuznetsov.deliveryfee.controllers;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.services.BaseFeeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/base-fees")
public class BaseFeeController {
    private final BaseFeeService baseFeeService;

    public BaseFeeController(BaseFeeService baseFeeService) {
        this.baseFeeService = baseFeeService;
    }

    @GetMapping
    public CollectionModel<EntityModel<RegionalBaseFee>> all() {
        return baseFeeService.getAllBaseFees();
    }

    @GetMapping("/{city}")
    public EntityModel<RegionalBaseFee> oneByCity(@PathVariable String city) {
        return baseFeeService.getOneByCity(city);
    }

    @PatchMapping("/{city}")
    public ResponseEntity<?> updateBaseFee(@RequestBody RegionalBaseFee fieldsToUpdate, @PathVariable String city) {
        return baseFeeService.updateBaseFee(fieldsToUpdate, city);
    }


}
