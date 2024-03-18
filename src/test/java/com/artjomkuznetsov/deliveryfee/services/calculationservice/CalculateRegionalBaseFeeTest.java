package com.artjomkuznetsov.deliveryfee.services.calculationservice;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class CalculateRegionalBaseFeeTest {

    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private RegionalBaseFeeRepository baseFeeRepository;

    RegionalBaseFee regionalBaseFeeTallinn = new RegionalBaseFee("tallinn", 4, 3.5f, 3);
    RegionalBaseFee regionalBaseFeeTartu = new RegionalBaseFee("tartu", 3.5f, 3, 2.5f);
    RegionalBaseFee regionalBaseFeeParnu = new RegionalBaseFee("pärnu", 3, 2.5f, 2);

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(baseFeeRepository.findByCity("tallinn")).thenReturn(Optional.of(regionalBaseFeeTallinn));
        Mockito.lenient().when(baseFeeRepository.findByCity("tartu")).thenReturn(Optional.of(regionalBaseFeeTartu));
        Mockito.lenient().when(baseFeeRepository.findByCity("pärnu")).thenReturn(Optional.of(regionalBaseFeeParnu));

    }

    @Test
    void calculateRegionalBaseFeeTallinnCarReturns4() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "car");
        Assertions.assertEquals(4.0, fee);
    }

    @Test
    void calculateRegionalBaseFeeTartuCarReturns3_5() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "car");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFeeParnuCarReturns3() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "car");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFeeTallinnBikeReturns3() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "bike");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFeeTartuBikeReturns2_5() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "bike");
        Assertions.assertEquals(2.5, fee);
    }

    @Test
    void calculateRegionalBaseFeeParnuBikeReturns2() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "bike");
        Assertions.assertEquals(2.0, fee);
    }

    @Test
    void calculateRegionalBaseFeeTallinnScooterReturns3_5() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "scooter");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFeeTartuScooterReturn3() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "scooter");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFeeParnuScooterReturns2_5() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "scooter");
        Assertions.assertEquals(2.5, fee);
    }
}
