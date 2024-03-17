package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {
    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @Mock
    private RegionalBaseFeeRepository baseFeeRepository;

    @Mock
    private AirTemperatureConditionsRepository airTemperatureRepository;
    @Mock
    private WeatherPhenomenonConditionsRepository weatherPhenomenonRepository;
    @Mock
    private WindSpeedConditionsRepository windSpeedRepository;

    RegionalBaseFee regionalBaseFeeTallinn = new RegionalBaseFee("tallinn", 4, 3.5f, 3);
    RegionalBaseFee regionalBaseFeeTartu = new RegionalBaseFee("tartu", 3.5f, 3, 2.5f);
    RegionalBaseFee regionalBaseFeeParnu = new RegionalBaseFee("pärnu", 3, 2.5f, 2);

    AirTemperatureConditions airTemperatureConditions = new AirTemperatureConditions(
            List.of("scooter", "bike"), -10, 1, -10, 0, 0.5f);

    WindSpeedConditions windSpeedConditions = new WindSpeedConditions(
            List.of("bike"), 10, 20, 0.5f, 20);

    WeatherPhenomenonConditions weatherPhenomenonConditions = new WeatherPhenomenonConditions(
            List.of("scooter", "bike"), 1, 0.5f, List.of("Glaze", "Hail", "Thunder"));


    @BeforeEach
    void setUp() {
        Mockito.lenient().when(baseFeeRepository.findByCity("tallinn")).thenReturn(Optional.of(regionalBaseFeeTallinn));
        Mockito.lenient().when(baseFeeRepository.findByCity("tartu")).thenReturn(Optional.of(regionalBaseFeeTartu));
        Mockito.lenient().when(baseFeeRepository.findByCity("pärnu")).thenReturn(Optional.of(regionalBaseFeeParnu));

        Mockito.lenient().when(airTemperatureRepository.findFirstBy()).thenReturn(Optional.of(airTemperatureConditions));
        Mockito.lenient().when(windSpeedRepository.findFirstBy()).thenReturn(Optional.of(windSpeedConditions));
        Mockito.lenient().when(weatherPhenomenonRepository.findFirstBy()).thenReturn(Optional.of(weatherPhenomenonConditions));
    }

    // Car
    @Test
    void calculateRegionalBaseFee_Tallinn_Car_Returns4() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "car");
        Assertions.assertEquals(4.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_Tartu_Car_Returns3_5() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "car");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_Parnu_Car_Returns3() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "car");
        Assertions.assertEquals(3.0, fee);
    }

    // Bike
    @Test
    void calculateRegionalBaseFee_Tallinn_Bike_Returns3() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "bike");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_Tartu_Bike_Returns2_5() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "bike");
        Assertions.assertEquals(2.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_Parnu_Bike_Returns2() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "bike");
        Assertions.assertEquals(2.0, fee);
    }

    // Scooter
    @Test
    void calculateRegionalBaseFee_Tallinn_Scooter_Returns3_5() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "scooter");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_Tartu_Scooter_Return3() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "scooter");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_Parnu_Scooter_Returns2_5() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "scooter");
        Assertions.assertEquals(2.5, fee);
    }

    // ---------------------------------------------------

    @Test
    void calculateExtraFee_Tallinn_Scooter_AirTempMinus15_Returns1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", -15, 6, "", 1710412650L));
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus15_Returns1() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -15, 6, "", 1710412650L));
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_AirTempMinus5_Returns0_5() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", -5, 6, "", 1710412650L));
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus5_Returns0_5() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -5, 6, "", 1710412650L));
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempPlus10_Returns0() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", 10, 6, "", 1710412650L));
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_WindSpeed15_Returns0_5() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", 10, 15, "", 1710412650L));
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_WindSpeed5_Returns0() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "", 1710412650L));
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_ForbiddenWindSpeed_ReturnsMinus1() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", 10, 30, "", 1710412650L));
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_DriftingSnow_Returns1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Drifting snow", 1710412650L));
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_LightSleet_Returns1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Light sleet", 1710412650L));
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_HeavyShower_Returns0_5() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Heavy shower", 1710412650L));
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_Glaze_ReturnsMinus1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Glaze", 1710412650L));
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_Hail_ReturnsMinus1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Hail", 1710412650L));
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Scooter_Thunder_ReturnsMinus1() {
        float extraFee = calculationService.calculateExtraFee("scooter",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Thunder", 1710412650L));
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Car_Thunder_Returns0() {
        float extraFee = calculationService.calculateExtraFee("car",
                new WeatherData("Tallinn-Harku", "26038", 10, 5, "Thunder", 1710412650L));
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus12_WindSpeed15_Returns1_5() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -12, 15, "", 1710412650L));
        Assertions.assertEquals(1.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus12_WindSpeed15_Snow_Returns2_5() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -12, 15, "Light snowfall", 1710412650L));
        Assertions.assertEquals(2.5, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus12_WindSpeed15_Rain_Returns2() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -12, 15, "Light rain", 1710412650L));
        Assertions.assertEquals(2, extraFee);
    }

    @Test
    void calculateExtraFee_Tallinn_Bike_AirTempMinus12_WindSpeed15_Thunder_ReturnsMinus1() {
        float extraFee = calculationService.calculateExtraFee("bike",
                new WeatherData("Tallinn-Harku", "26038", -12, 15, "Thunder", 1710412650L));
        Assertions.assertEquals(-1, extraFee);
    }


    @Test
    void calculateTotalFee_Tallinn_Bike_AirTempMinus12_Returns4() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 5, "", 1710412650L));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"));
        Assertions.assertEquals(4, fee);
    }

    @Test
    void calculateTotalFee_Tallinn_Bike_AirTempMinus12_WindSpeed15_Returns4_5() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "", 1710412650L));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"));
        Assertions.assertEquals(4.5, fee);
    }

    @Test
    void calculateTotalFeeByDateTime_Tallinn_Bike_AirTempMinus2_Returns3_5() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findByStationAndObservationTimestamp("Tallinn-Harku", 1710681299L))
                .thenReturn(Optional.of(new WeatherData("Tallinn-Harku", "26038", -2, 4, "", 1710681299L)));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"),
                Optional.of(LocalDateTime.of(2024, Month.MARCH, 17, 15, 30, 40)));

        Assertions.assertEquals(3.5, fee);
    }
}
