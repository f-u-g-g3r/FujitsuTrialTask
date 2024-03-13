package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import com.artjomkuznetsov.deliveryfee.repositories.WeatherDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {
    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private RegionalBaseFeeRepository baseFeeRepository;

    RegionalBaseFee regionalBaseFeeTallinn = new RegionalBaseFee("Tallinn", 4.0f, 3.5f, 3.0f);
    RegionalBaseFee regionalBaseFeeTartu = new RegionalBaseFee("Tartu", 3.5f, 3.0f, 2.5f);
    RegionalBaseFee regionalBaseFeeParnu = new RegionalBaseFee("Pärnu", 3.0f, 2.5f, 2.0f);

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(baseFeeRepository.findByCity("tallinn")).thenReturn(regionalBaseFeeTallinn);
        Mockito.lenient().when(baseFeeRepository.findByCity("tartu")).thenReturn(regionalBaseFeeTartu);
        Mockito.lenient().when(baseFeeRepository.findByCity("pärnu")).thenReturn(regionalBaseFeeParnu);
    }

    // Car
    @Test
    void calculateFee_tallinn_car() {
        float fee = calculationService.calculateFee("tallinn", "car");
        Assertions.assertEquals(4.0, fee);
    }

    @Test
    void calculateFee_tartu_car() {
        float fee = calculationService.calculateFee("tartu", "car");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateFee_parnu_car() {
        float fee = calculationService.calculateFee("pärnu", "car");
        Assertions.assertEquals(3.0, fee);
    }

    // Bike
    @Test
    void calculateFee_tallinn_bike() {
        float fee = calculationService.calculateFee("tallinn", "bike");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateFee_tartu_bike() {
        float fee = calculationService.calculateFee("tartu", "bike");
        Assertions.assertEquals(2.5, fee);
    }

    @Test
    void calculateFee_parnu_bike() {
        float fee = calculationService.calculateFee("pärnu", "bike");
        Assertions.assertEquals(2.0, fee);
    }

    // Scooter
    @Test
    void calculateFee_tallinn_scooter() {
        float fee = calculationService.calculateFee("tallinn", "scooter");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateFee_tartu_scooter() {
        float fee = calculationService.calculateFee("tartu", "scooter");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateFee_parnu_scooter() {
        float fee = calculationService.calculateFee("pärnu", "scooter");
        Assertions.assertEquals(2.5, fee);
    }

}
