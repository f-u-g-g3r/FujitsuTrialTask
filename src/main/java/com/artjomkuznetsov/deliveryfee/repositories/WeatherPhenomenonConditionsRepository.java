package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WeatherPhenomenonConditionsRepository extends JpaRepository<WeatherPhenomenonConditions, Integer> {
    Optional<WeatherPhenomenonConditions> findFirstBy();
}
