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

    WeatherData weatherData = new WeatherData("Tallinn-Harku", "26038", 5, 5, "", 1710412650L);

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(airTemperatureRepository.findFirstBy()).thenReturn(Optional.of(airTemperatureConditions));
        Mockito.lenient().when(windSpeedRepository.findFirstBy()).thenReturn(Optional.of(windSpeedConditions));
        Mockito.lenient().when(weatherPhenomenonRepository.findFirstBy()).thenReturn(Optional.of(weatherPhenomenonConditions));
    }

    // ---------------------------------------------------

    @Test
    void calculateExtraFeeScooterAirTempMinus15Returns1() {
        weatherData.setAirTemperature(-15);
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus15Returns1() {
        weatherData.setAirTemperature(-15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterAirTempMinus5Returns0_5() {
        weatherData.setAirTemperature(-5);
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus5Returns0_5() {
        weatherData.setAirTemperature(-5);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempPlus10Returns0() {
        weatherData.setAirTemperature(10);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeBikeWindSpeed15Returns0_5() {
        weatherData.setWindSpeed(15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeBikeWindSpeed5Returns0() {
        weatherData.setWindSpeed(5);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeBikeForbiddenWindSpeedReturnsMinus1() {
        weatherData.setWindSpeed(30);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterDriftingSnowReturns1() {
        weatherData.setWeatherPhenomenon("Drifting snow");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterLightSleetReturns1() {
        weatherData.setWeatherPhenomenon("Light sleet");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterHeavyShowerReturns0_5() {
        weatherData.setWeatherPhenomenon("Heavy shower");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFeeScooterGlazeReturnsMinus1() {
        weatherData.setWeatherPhenomenon("Glaze");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterHailReturnsMinus1() {
        weatherData.setWeatherPhenomenon("Hail");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeScooterThunderReturnsMinus1() {
        weatherData.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("scooter", weatherData);
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFeeCarThunderReturns0() {
        weatherData.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("car", weatherData);
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus12WindSpeed15Returns1_5() {
        weatherData.setAirTemperature(-12);
        weatherData.setWindSpeed(15);
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(1.5, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus12WindSpeed15SnowReturns2_5() {
        weatherData.setAirTemperature(-12);
        weatherData.setWindSpeed(15);
        weatherData.setWeatherPhenomenon("Light snowfall");
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(2.5, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus12WindSpeed15RainReturns2() {
        weatherData.setAirTemperature(-12);
        weatherData.setWindSpeed(15);
        weatherData.setWeatherPhenomenon("Light rain");
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(2, extraFee);
    }

    @Test
    void calculateExtraFeeBikeAirTempMinus12WindSpeed15ThunderReturnsMinus1() {
        weatherData.setAirTemperature(-12);
        weatherData.setWindSpeed(15);
        weatherData.setWeatherPhenomenon("Thunder");
        float extraFee = calculationService.calculateExtraFee("bike", weatherData);
        Assertions.assertEquals(-1, extraFee);
    }
}
