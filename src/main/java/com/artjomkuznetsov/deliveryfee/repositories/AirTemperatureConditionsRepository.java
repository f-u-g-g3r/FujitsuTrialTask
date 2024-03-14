package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.AirTemperatureConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirTemperatureConditionsRepository extends JpaRepository<AirTemperatureConditions, Long> {
    AirTemperatureConditions findFirstBy();
}
