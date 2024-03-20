package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirTemperatureConditionsRepository extends JpaRepository<AirTemperatureConditions, Integer> {
    Optional<AirTemperatureConditions> findFirstBy();
}
