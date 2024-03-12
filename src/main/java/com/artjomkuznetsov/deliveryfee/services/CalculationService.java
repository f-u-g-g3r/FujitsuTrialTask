package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.RegionalBaseFee;
import com.artjomkuznetsov.deliveryfee.repositories.RegionalBaseFeeRepository;
import com.artjomkuznetsov.deliveryfee.repositories.WeatherDataRepository;
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

    public CalculationService(WeatherDataRepository weatherDataRepository, RegionalBaseFeeRepository baseFeeRepository) {
        this.weatherDataRepository = weatherDataRepository;
        this.baseFeeRepository = baseFeeRepository;
    }

    public float calculateFee(String city, String transport) {
        if (city != null && CITIES.containsKey(city.toLowerCase())) {
            if (transport != null && TRANSPORTS.contains(transport.toLowerCase())) {
                float baseFee = calculateRegionalBaseFee(city.toLowerCase(), transport.toLowerCase());


            }
        }
        return 0;
    }

    private float calculateRegionalBaseFee(String city, String transport) {
        RegionalBaseFee RBF = baseFeeRepository.findByCity(city);

        return switch (transport) {
            case "car" -> RBF.getCarFee();
            case "scooter" -> RBF.getScooterFee();
            case "bike" -> RBF.getBikeFee();
            default -> 0;
        };
    }

    private int calculateExtraFee() {
        return 0;
    }
}
