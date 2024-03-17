package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.controllers.FeeController;
import com.artjomkuznetsov.deliveryfee.controllers.responses.FeeResponse;
import com.artjomkuznetsov.deliveryfee.exceptions.ExtraWeatherConditionsNotFoundException;
import com.artjomkuznetsov.deliveryfee.exceptions.RegionalBaseFeeNotFoundException;
import com.artjomkuznetsov.deliveryfee.exceptions.VehicleForbiddenException;
import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import com.artjomkuznetsov.deliveryfee.repositories.*;
import jakarta.annotation.Nullable;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CalculationService {
    private static final List<String> VEHICLES = List.of("car", "bike", "scooter");

    private static final Map<String, String> CITIES = Map.of(
            "tallinn", "Tallinn-Harku",
            "tartu", "Tartu-Tõravere",
            "pärnu", "Pärnu");

    private static final List<String> SNOW_OR_SLEET = List.of(
            "Light snow shower", "Moderate snow shower", "Heavy snow shower",
            "Light snowfall", "Moderate snowfall", "Heavy snowfall", "Blowing snow", "Drifting snow",
            "Light sleet", "Moderate sleet");

    private static final List<String> RAIN = List.of(
            "Light shower", "Moderate shower", "Heavy shower",
            "Light rain", "Moderate rain", "Heavy rain");

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
     *
     * @param city
     * @param vehicle
     * @param dateTime
     * @return
     * @throws BadRequestException
     */
    public EntityModel<FeeResponse> calculateFee(Optional<String> city, Optional<String> vehicle, Optional<LocalDateTime> dateTime)
            throws BadRequestException {
        FeeResponse feeResponse;
        String uri = linkTo(methodOn(FeeController.class).getFee(city, vehicle, dateTime)).toString();

        if (dateTime.isPresent()) {
            String formattedDateTime = dateTime.get().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            uri = UriComponentsBuilder.fromUriString(uri)
                    .replaceQueryParam("dateTime", formattedDateTime)
                    .build().toUriString();
            feeResponse = new FeeResponse(calculateTotalFee(city, vehicle, dateTime));
        } else {
            feeResponse = new FeeResponse(calculateTotalFee(city, vehicle));
            uri = uri.replace("&dateTime=", "").replace("{&dateTime}", "");
        }

        return EntityModel.of(feeResponse, Link.of(uri).withSelfRel());
    }

    /**
     *
     * @param optCity
     * @param optVehicle
     * @return
     * @throws BadRequestException
     */
    public float calculateTotalFee(Optional<String> optCity, Optional<String> optVehicle)
            throws BadRequestException {
        return calculateTotalFee(optCity, optVehicle, Optional.empty());
    }

    /**
     * Calculates total fee based on the city, vehicle and current weather data.
     *
     * @param optCity    The city for which calculations will be made.
     * @param optVehicle vehicle for which calculations will be made.
     * @return Total fee or -1 if the use of the specified vehicle is forbidden due to the current weather.
     */
    public float calculateTotalFee(Optional<String> optCity, Optional<String> optVehicle, Optional<LocalDateTime> dateTime)
            throws BadRequestException, VehicleForbiddenException {
        if (optCity.isPresent() && optVehicle.isPresent() && !optCity.get().isEmpty() && !optVehicle.get().isEmpty()) {
            String city = optCity.get().toLowerCase();
            String vehicle = optVehicle.get().toLowerCase();
            if (CITIES.containsKey(city)) {
                if (VEHICLES.contains(vehicle)) {
                    String station = CITIES.get(city);
                    float baseFee = calculateRegionalBaseFee(city, vehicle);
                    WeatherData weatherData;
                    if (dateTime.isPresent()) {
                        weatherData = getWeatherDataByLocalDateTime(station, dateTime.get());
                    } else {
                        weatherData = weatherDataRepository.findFirstByStationOrderByObservationTimestampDesc(station);
                    }
                    float extraFee = calculateExtraFee(vehicle, weatherData);

                    if (extraFee == -1) {
                        throw new VehicleForbiddenException();
                    } else {
                        return baseFee + extraFee;
                    }
                }
            }
        }
        throw new BadRequestException("Invalid request. Make sure you specified the city and transport parameters properly.");
    }

    /**
     *
     * @param station
     * @param dateTime
     * @return
     * @throws BadRequestException
     */
    private WeatherData getWeatherDataByLocalDateTime(String station, LocalDateTime dateTime) throws BadRequestException {
        if (dateTime.getMinute() >= 15) {
            dateTime = dateTime.minusMinutes(dateTime.getMinute() - 15)
                    .minusSeconds(dateTime.getSecond());
        } else {
            dateTime = dateTime.minusMinutes(dateTime.getMinute() + 45)
                    .minusSeconds(dateTime.getSecond() + 1);
        }

        // Converts to UTC Timezone
        dateTime = dateTime.minusHours(2);

        long timestamp = dateTime.atZone(ZoneOffset.UTC).toEpochSecond();
        try {
            return weatherDataRepository.findByStationAndObservationTimestamp(station, timestamp)
                        .orElseGet(() -> weatherDataRepository.findByStationAndObservationTimestamp(station, timestamp - 1)
                                .orElseGet(() -> weatherDataRepository.findByStationAndObservationTimestamp(station, timestamp - 2)
                                        .orElseThrow()));
        } catch (NoSuchElementException e) {
            throw new BadRequestException("It is not possible to obtain weather data for the specified time period.");
        }
    }

    /**
     * Calculates fee only for regional rules, based on the city and vehicle.
     *
     * @param city    The city for which calculations will be made.
     * @param vehicle vehicle for which calculations will be made.
     * @return Regional base fee.
     */
    public float calculateRegionalBaseFee(String city, String vehicle) {
        RegionalBaseFee RBF = baseFeeRepository.findByCity(city)
                .orElseThrow(() -> new RegionalBaseFeeNotFoundException(city));

        return switch (vehicle) {
            case "car" -> RBF.getCarFee();
            case "scooter" -> RBF.getScooterFee();
            case "bike" -> RBF.getBikeFee();
            default -> 0;
        };
    }

    /**
     * Calculates fee based on the specified city, vehicle and weather conditions.
     *
     * @param vehicle vehicle for which conditions will be checked.
     * @return Extra fee for weather conditions.
     */
    public float calculateExtraFee(String vehicle, WeatherData weatherData) {
        AirTemperatureConditions airConditions = airTemperatureRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);
        WindSpeedConditions windConditions = windSpeedRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);
        WeatherPhenomenonConditions phenomenonConditions = weatherPhenomenonRepository.findFirstBy()
                .orElseThrow(ExtraWeatherConditionsNotFoundException::new);
        float extraFee = 0;
        if (weatherData != null && airConditions != null && windConditions != null && phenomenonConditions != null) {
            if (airConditions.getVehicleTypes().contains(vehicle)) {
                float airExtraFee = calculateAirExtraFee(airConditions, weatherData.getAirTemperature());
                extraFee += airExtraFee;
            }

            if (windConditions.getVehicleTypes().contains(vehicle)) {
                float windExtraFee = calculateWindExtraFee(windConditions, weatherData.getWindSpeed());
                if (windExtraFee == -1) {
                    return -1;
                } else {
                    extraFee += windExtraFee;
                }
            }

            if (phenomenonConditions.getVehicleTypes().contains(vehicle)) {
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
     * If returns -1, it means that specified vehicle cannot be used
     */
    private float calculateWindExtraFee(WindSpeedConditions windConditions, float windSpeed) {
        float extraFeeForWind = 0;
        if (windSpeed > windConditions.getBetweenMin() && windSpeed < windConditions.getBetweenMax()) {
            extraFeeForWind = windConditions.getBetweenFee();
        } else if (windSpeed >= windConditions.getForbiddenSpeed()) {
            return -1;
        }
        return extraFeeForWind;
    }

    private float calculatePhenomenonExtraFee(WeatherPhenomenonConditions phenomenonConditions, String phenomenon) {
        float extraFeeForPhenomenon = 0;
        if (SNOW_OR_SLEET.contains(phenomenon)) {
            extraFeeForPhenomenon = phenomenonConditions.getSnowOrSleetFee();
        } else if (RAIN.contains(phenomenon)) {
            extraFeeForPhenomenon = phenomenonConditions.getRainFee();
        } else if (phenomenonConditions.getForbiddenPhenomenons().contains(phenomenon)) {
            return -1;
        }
        return extraFeeForPhenomenon;
    }
}
