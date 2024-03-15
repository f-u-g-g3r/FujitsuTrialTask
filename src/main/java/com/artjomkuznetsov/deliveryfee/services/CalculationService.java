package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CalculationService {
    private static final List<String> TRANSPORTS = List.of("car", "bike", "scooter");
    private static final Map<String, String> CITIES = Map.of(
            "tallinn", "Tallinn-Harku",
            "tartu", "Tartu-Tõravere",
            "pärnu", "Pärnu");

    private final WeatherDataRepository weatherDataRepository;
    private final RegionalBaseFeeRepository baseFeeRepository;
    private final AirTemperatureConditionsRepository airTemperatureRepository;
    private final WeatherPhenomenonConditionsRepository weatherPhenomenonRepository;
    private final WindSpeedConditionsRepository windSpeedRepository;

    public CalculationService(WeatherDataRepository weatherDataRepository, RegionalBaseFeeRepository baseFeeRepository, AirTemperatureConditionsRepository airTemperatureRepository, WeatherPhenomenonConditionsRepository weatherPhenomenonRepository, WindSpeedConditionsRepository windSpeedRepository) {
        this.weatherDataRepository = weatherDataRepository;
        this.baseFeeRepository = baseFeeRepository;
        this.airTemperatureRepository = airTemperatureRepository;
        this.weatherPhenomenonRepository = weatherPhenomenonRepository;
        this.windSpeedRepository = windSpeedRepository;
    }


    /**
     * Calculates total fee based on the city, transport and current weather data.
     * @param city The city for which calculations will be made.
     * @param transport Transport for which calculations will be made.
     * @return Total fee or -1 if the use of the specified transport is forbidden due to the current weather.
     */
    public float calculateFee(String city, String transport) {
        if (city != null && CITIES.containsKey(city.toLowerCase())) {
            if (transport != null && TRANSPORTS.contains(transport.toLowerCase())) {
                float baseFee = calculateRegionalBaseFee(city.toLowerCase(), transport.toLowerCase());
                float extraFee = calculateExtraFee(city.toLowerCase(), transport.toLowerCase());
                if (extraFee == -1) {
                    return -1;
                } else {
                    return baseFee + extraFee;
                }
            }
        }
        return 0;
    }

    /**
     * Calculates fee only for regional rules, based on the city and transport.
     * @param city The city for which calculations will be made.
     * @param transport Transport for which calculations will be made.
     * @return Regional base fee.
     */
    public float calculateRegionalBaseFee(String city, String transport) {
        RegionalBaseFee RBF = baseFeeRepository.findByCity(city);

        return switch (transport) {
            case "car" -> RBF.getCarFee();
            case "scooter" -> RBF.getScooterFee();
            case "bike" -> RBF.getBikeFee();
            default -> 0;
        };
    }

    /**
     * Calculates fee based on the specified city, transport and weather conditions.
     * @param city The city which specified the weather station.
     * @param transport Transport for which conditions will be checked.
     * @return Extra fee for weather conditions.
     */
    public float calculateExtraFee(String city, String transport) {
        WeatherData weatherData = weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc(CITIES.get(city));

        AirTemperatureConditions airConditions = airTemperatureRepository.findFirstBy();
        WindSpeedConditions windConditions = windSpeedRepository.findFirstBy();
        WeatherPhenomenonConditions phenomenonConditions = weatherPhenomenonRepository.findFirstBy();
        float extraFee = 0;
        if (weatherData != null && airConditions != null && windConditions != null && phenomenonConditions != null) {
            if (airConditions.getVehicleTypes().contains(transport)) {
                float airExtraFee = calculateAirExtraFee(airConditions, weatherData.getAirTemperature());
                extraFee += airExtraFee;
            }

            if (windConditions.getVehicleTypes().contains(transport)) {
                float windExtraFee = calculateWindExtraFee(windConditions, weatherData.getWindSpeed());
                if (windExtraFee == -1) {
                    return -1;
                } else {
                    extraFee += windExtraFee;
                }
            }

            if (phenomenonConditions.getVehicleTypes().contains(transport)) {
                float phenomenonExtraFee = calculatePhenomenonExtraFee(phenomenonConditions, weatherData.getWeatherPhenomenon());
                if (phenomenonExtraFee == -1) {
                    return -1;
                } else {
                    extraFee += phenomenonExtraFee;
                }
            }
            return extraFee;
        }
        return 0;
    }

    private float calculateAirExtraFee(AirTemperatureConditions airConditions, float airTemp) {
        float extraFeeForAir = 0;
        if (airConditions.getLessThan() >= airTemp) {
            extraFeeForAir = airConditions.getLessThanFee();
        } else if (airTemp > airConditions.getBetweenMin() && airTemp < airConditions.getBetweenMax()) {
            extraFeeForAir = airConditions.getBetweenFee();
        }
        return extraFeeForAir;
    }

    /**
     * If returns -1, it means that specified transport cannot be used
     */
    private float calculateWindExtraFee(WindSpeedConditions windConditions, float windSpeed) {
        float extraFeeForWind = 0;
        if (windSpeed > windConditions.getBetweenMin()  && windSpeed < windConditions.getBetweenMax()) {
            extraFeeForWind = windConditions.getBetweenFee();
        } else if (windSpeed >= windConditions.getForbiddenSpeed()) {
            return -1;
        }
        return extraFeeForWind;
    }

    private float calculatePhenomenonExtraFee(WeatherPhenomenonConditions phenomenonConditions, String phenomenon) {
        float extraFeeForPhenomenon = 0;
        if (phenomenon.equals("Snow") || phenomenon.equals("Sleet")) {
            extraFeeForPhenomenon = phenomenonConditions.getSnowOrSleetFee();
        } else if (phenomenon.equals("Rain")) {
            extraFeeForPhenomenon = phenomenonConditions.getRainFee();
        } else if (phenomenonConditions.getForbiddenPhenomenons().contains(phenomenon)) {
            return -1;
        }
        return extraFeeForPhenomenon;
    }
}
