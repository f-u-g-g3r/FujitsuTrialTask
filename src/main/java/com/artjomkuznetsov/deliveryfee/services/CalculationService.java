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
     * Calculates the fee for a given city, vehicle type, and optional date and time.
     *
     * @param city (Optional) The name of the city for which to calculate the fee.
     * @param vehicle (Optional) The type of vehicle for which to calculate the fee.
     * @param dateTime (Optional) The date and time for which to calculate the fee.
     * @return An EntityModel containing the calculated fee response, with a self-referencing link.
     * @throws BadRequestException if the provided parameters are invalid or if the calculation fails.
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
     * Calculates the total fee based on the optional parameters for city and vehicle.
     *
     * @param city An optional parameter representing the city for which the fee is calculated.
     * @param vehicle An optional parameter representing the vehicle type for which the fee is calculated.
     * @return The calculated total fee.
     * @throws BadRequestException if there is a problem with the request parameters.
     */
    public float calculateTotalFee(Optional<String> city, Optional<String> vehicle)
            throws BadRequestException {
        return calculateTotalFee(city, vehicle, Optional.empty());
    }

    /**
     * Calculates the total fee based on the optional parameters for city, vehicle, and date/time.
     *
     * @param optCity An optional parameter representing the city for which the fee is calculated.
     * @param optVehicle An optional parameter representing the vehicle type for which the fee is calculated.
     * @param dateTime An optional parameter representing the date and time for which weather data will be taken.
     *                 If dateTime is empty, the latest weather data will be taken.
     * @return The calculated total fee.
     * @throws BadRequestException if there is a problem with the request parameters, such as missing or invalid data.
     * @throws VehicleForbiddenException if the specified vehicle is not allowed according to the weather data.
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
                float airExtraFee = calculateTemperatureExtraFee(airConditions, weatherData.getAirTemperature());
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

    private float calculateTemperatureExtraFee(AirTemperatureConditions airConditions, float airTemp) {
        float extraFeeForAir = 0;
        if (airConditions.getLessThan() >= airTemp) {
            extraFeeForAir = airConditions.getLessThanFee();
        } else if (airTemp > airConditions.getBetweenMin() && airTemp < airConditions.getBetweenMax()) {
            extraFeeForAir = airConditions.getBetweenFee();
        }
        return extraFeeForAir;
    }

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
