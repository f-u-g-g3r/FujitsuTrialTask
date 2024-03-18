package com.artjomkuznetsov.deliveryfee.services.calculationservice;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import com.artjomkuznetsov.deliveryfee.services.CalculationService;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CalculateTotalFeeTest {
    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private RegionalBaseFeeRepository baseFeeRepository;

    @Mock
    private WeatherDataRepository weatherDataRepository;

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



    RegionalBaseFee regionalBaseFeeTallinn = new RegionalBaseFee("tallinn", 4, 3.5f, 3);
    RegionalBaseFee regionalBaseFeeTartu = new RegionalBaseFee("tartu", 3.5f, 3, 2.5f);
    RegionalBaseFee regionalBaseFeeParnu = new RegionalBaseFee("pärnu", 3, 2.5f, 2);

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(baseFeeRepository.findByCity("tallinn")).thenReturn(Optional.of(regionalBaseFeeTallinn));
        Mockito.lenient().when(baseFeeRepository.findByCity("tartu")).thenReturn(Optional.of(regionalBaseFeeTartu));
        Mockito.lenient().when(baseFeeRepository.findByCity("pärnu")).thenReturn(Optional.of(regionalBaseFeeParnu));

        Mockito.lenient().when(airTemperatureRepository.findFirstBy()).thenReturn(Optional.of(airTemperatureConditions));
        Mockito.lenient().when(windSpeedRepository.findFirstBy()).thenReturn(Optional.of(windSpeedConditions));
        Mockito.lenient().when(weatherPhenomenonRepository.findFirstBy()).thenReturn(Optional.of(weatherPhenomenonConditions));
    }


    @Test
    void calculateTotalFeeTallinnBikeAirTempMinus12Returns4() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 5, "", 1710412650L));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"));
        Assertions.assertEquals(4, fee);
    }

    @Test
    void calculateTotalFeeTallinnBikeAirTempMinus12WindSpeed15Returns4_5() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc("Tallinn-Harku"))
                .thenReturn(new WeatherData("Tallinn-Harku", "26038", -12, 15, "", 1710412650L));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"));
        Assertions.assertEquals(4.5, fee);
    }

    @Test
    void calculateTotalFeeByDateTimeTallinnBikeAirTempMinus2Returns3_5() throws BadRequestException {
        Mockito.lenient().when(weatherDataRepository.findByStationAndObservationTimestamp("Tallinn-Harku", 1710681299L))
                .thenReturn(Optional.of(new WeatherData("Tallinn-Harku", "26038", -2, 4, "", 1710681299L)));
        float fee = calculationService.calculateTotalFee(Optional.of("tallinn"), Optional.of("bike"),
                Optional.of(LocalDateTime.of(2024, Month.MARCH, 17, 15, 30, 40)));

        Assertions.assertEquals(3.5, fee);
    }

}
