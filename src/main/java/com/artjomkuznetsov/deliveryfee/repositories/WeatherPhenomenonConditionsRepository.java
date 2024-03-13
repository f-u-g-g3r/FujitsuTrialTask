package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WeatherPhenomenonConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherPhenomenonConditionsRepository extends JpaRepository<WeatherPhenomenonConditions, Long> {

}
