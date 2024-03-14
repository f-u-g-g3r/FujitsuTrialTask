package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

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
        Mockito.lenient().when(baseFeeRepository.findByCity("tallinn")).thenReturn(regionalBaseFeeTallinn);
        Mockito.lenient().when(baseFeeRepository.findByCity("tartu")).thenReturn(regionalBaseFeeTartu);
        Mockito.lenient().when(baseFeeRepository.findByCity("pärnu")).thenReturn(regionalBaseFeeParnu);

        Mockito.lenient().when(airTemperatureRepository.findFirstBy()).thenReturn(airTemperatureConditions);
        Mockito.lenient().when(windSpeedRepository.findFirstBy()).thenReturn(windSpeedConditions);
        Mockito.lenient().when(weatherPhenomenonRepository.findFirstBy()).thenReturn(weatherPhenomenonConditions);
    }

    // Car
    @Test
    void calculateRegionalBaseFee_tallinn_car() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "car");
        Assertions.assertEquals(4.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_tartu_car() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "car");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_parnu_car() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "car");
        Assertions.assertEquals(3.0, fee);
    }

    // Bike
    @Test
    void calculateRegionalBaseFee_tallinn_bike() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "bike");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_tartu_bike() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "bike");
        Assertions.assertEquals(2.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_parnu_bike() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "bike");
        Assertions.assertEquals(2.0, fee);
    }

    // Scooter
    @Test
    void calculateRegionalBaseFee_tallinn_scooter() {
        float fee = calculationService.calculateRegionalBaseFee("tallinn", "scooter");
        Assertions.assertEquals(3.5, fee);
    }

    @Test
    void calculateRegionalBaseFee_tartu_scooter() {
        float fee = calculationService.calculateRegionalBaseFee("tartu", "scooter");
        Assertions.assertEquals(3.0, fee);
    }

    @Test
    void calculateRegionalBaseFee_parnu_scooter() {
        float fee = calculationService.calculateRegionalBaseFee("pärnu", "scooter");
        Assertions.assertEquals(2.5, fee);
    }

    // ---------------------------------------------------

    @Test
    void calculateExtraFee_tallinn_scooter_air_lessThan() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -15, 6, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_lessThan() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -15, 6, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_air_between() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -5.4f, 6, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_between() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -5.4f, 6, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_normal() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 6, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_wind_between() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 15, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_wind_normal() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_wind_forbidden() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 30, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_snow() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Snow", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_sleet() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Sleet", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_rain() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Rain", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(0.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_glaze() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Glaze", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_hail() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Hail", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_scooter_phenomenon_thunder() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Thunder", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "scooter");
        Assertions.assertEquals(-1, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_car_phenomenon_thunder() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", 10, 5, "Thunder", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "car");
        Assertions.assertEquals(0, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_lessThan_wind_between() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(1.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_lessThan_wind_between_snow() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "Snow", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(2.5, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_lessThan_wind_between_rain() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "Rain", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(2, extraFee);
    }

    @Test
    void calculateExtraFee_tallinn_bike_air_lessThan_wind_between_thunder() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "Thunder", 1710412650L));

        float extraFee = calculationService.calculateExtraFee("tallinn", "bike");
        Assertions.assertEquals(-1, extraFee);
    }


    @Test
    void calculateTotalFee_tallinn_bike_air_lessThan() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 5, "", 1710412650L));
        float fee = calculationService.calculateFee("tallinn", "bike");
        Assertions.assertEquals(4, fee);
    }

    @Test
    void calculateTotalFee_tallinn_bike_air_lessThan_wind_between() {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "", 1710412650L));
        float fee = calculationService.calculateFee("tallinn", "bike");
        Assertions.assertEquals(4.5, fee);
    }
}
