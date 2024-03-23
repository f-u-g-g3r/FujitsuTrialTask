package com.artjomkuznetsov.deliveryfee.repositories;

import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    /**
     * Finds the last entry by station name
     */
    WeatherData findFirstByStationOrderByObservationTimestampDesc(String station);

    Optional<WeatherData> findByStationAndObservationTimestamp(String station, Long observationTimestamp);
}
