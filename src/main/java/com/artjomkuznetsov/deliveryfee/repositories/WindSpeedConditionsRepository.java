package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.extra_weather_fee.WindSpeedConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WindSpeedConditionsRepository extends JpaRepository<WindSpeedConditions, Integer> {
    Optional<WindSpeedConditions> findFirstBy();
}
