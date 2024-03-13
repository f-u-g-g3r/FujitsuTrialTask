package com.artjomkuznetsov.deliveryfee.controllers;


import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    private final CalculationService calculationService;

    public MainController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public float test() {
        return calculationService.calculateFee("tallinn", "scooter");
    }
}
