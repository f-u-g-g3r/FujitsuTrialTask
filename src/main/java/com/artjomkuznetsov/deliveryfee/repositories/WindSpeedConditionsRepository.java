package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WindSpeedConditionsRepository extends JpaRepository<WindSpeedConditions, Long> {
}
