package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WindSpeedConditionsRepository extends JpaRepository<WindSpeedConditions, Integer> {
    Optional<WindSpeedConditions> findFirstBy();
}
