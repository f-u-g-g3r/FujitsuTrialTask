package com.artjomkuznetsov.deliveryfee.services.calculationservice;

import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CalculateExtraFeeTest {
    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private AirTemperatureConditionsRepository airTemperatureRepository;
    @Mock
    private WeatherPhenomenonConditionsRepository weatherPhenomenonRepository;
    @Mock
    private WindSpeedConditionsRepository windSpeedRepository;

    AirTemperatureConditions airTemperatureConditions = new AirTemperatureConditions(
            List.of("scooter", "bike"), -10, 1, -10, 0, 0.5f);

    WindSpeedConditions windSpeedConditions = new WindSpeedConditions(
            List.of("bike"), 10, 20, 0.5f, 20);

    WeatherPhenomenonConditions weatherPhenomenonConditions = new WeatherPhenomenonConditions(
            List.of("scooter", "bike"), 1, 0.5f, List.of("Glaze", "Hail", "Thunder"));

    WeatherData weatherDataTallinn = new WeatherData("Tallinn-Harku", "26038", 5, 5, "", 1710412650L);
    WeatherData weatherDataTartu = new WeatherData("Tartu-Tõravere", "26242", 5, 5, "", 1710412650L);
    WeatherData weatherDataParnu = new WeatherData("Pärnu", "41803", 5, 5, "", 1710412650L);


    @BeforeEach
    void setUp() {
        Mockito.lenient().when(airTemperatureRepository.findFirstBy()).thenReturn(Optional.of(airTemperatureConditions));
        Mockito.lenient().when(windSpeedRepository.findFirstBy()).thenReturn(Optional.of(windSpeedConditions));
        Mockito.lenient().when(weatherPhenomenonRepository.findFirstBy()).thenReturn(Optional.of(weatherPhenomenonConditions));
    }

    // ---------------------------------------------------

    @Test
    void calculateExtraFeeTallinnScooterAirTempMinus15Returns1() {
        weatherDataTallinn.setAirTemperature(-15);
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus15Returns1() {
        weatherDataTallinn.setAirTemperature(-15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterAirTempMinus5Returns0_5() {
        weatherDataTallinn.setAirTemperature(-5);
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus5Returns0_5() {
        weatherDataTallinn.setAirTemperature(-5);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempPlus10Returns0() {
        weatherDataTallinn.setAirTemperature(10);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeWindSpeed15Returns0_5() {
        weatherDataTallinn.setWindSpeed(15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeWindSpeed5Returns0() {
        weatherDataTallinn.setWindSpeed(5);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeForbiddenWindSpeedReturnsMinus1() {
        weatherDataTallinn.setWindSpeed(30);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterDriftingSnowReturns1() {
        weatherDataTallinn.setWeatherPhenomenon("Drifting snow");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterLightSleetReturns1() {
        weatherDataTallinn.setWeatherPhenomenon("Light sleet");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterHeavyShowerReturns0_5() {
        weatherDataTallinn.setWeatherPhenomenon("Heavy shower");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterGlazeReturnsMinus1() {
        weatherDataTallinn.setWeatherPhenomenon("Glaze");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterHailReturnsMinus1() {
        weatherDataTallinn.setWeatherPhenomenon("Hail");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnScooterThunderReturnsMinus1() {
        weatherDataTallinn.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherDataTallinn);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnCarThunderReturns0() {
        weatherDataTallinn.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("car", weatherDataTallinn);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus12WindSpeed15Returns1_5() {
        weatherDataTallinn.setAirTemperature(-12);
        weatherDataTallinn.setWindSpeed(15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(1.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus12WindSpeed15SnowReturns2_5() {
        weatherDataTallinn.setAirTemperature(-12);
        weatherDataTallinn.setWindSpeed(15);
        weatherDataTallinn.setWeatherPhenomenon("Light snowfall");
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(2.5, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus12WindSpeed15RainReturns2() {
        weatherDataTallinn.setAirTemperature(-12);
        weatherDataTallinn.setWindSpeed(15);
        weatherDataTallinn.setWeatherPhenomenon("Light rain");
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(2, extraFee);
    }

    @Test
    void calculateExtraFeeTallinnBikeAirTempMinus12WindSpeed15ThunderReturnsMinus1() {
        weatherDataTallinn.setAirTemperature(-12);
        weatherDataTallinn.setWindSpeed(15);
        weatherDataTallinn.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("bike", weatherDataTallinn);
        Assertions.assertEquals(-1, extraFee);
    }
}
