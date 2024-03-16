package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherPhenomenonConditionsRepository extends JpaRepository<WeatherPhenomenonConditions, Long> {
    Optional<WeatherPhenomenonConditions> findFirstBy();
}
