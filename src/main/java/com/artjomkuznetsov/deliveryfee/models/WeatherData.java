package com.artjomkuznetsov.deliveryfee.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Table(name = "weather_data")
@Entity
public class WeatherData {
    @NotNull
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @NotNull
    private String station;
    @NotNull
    private String stationWMO;
    private float airTemperature;
    private float windSpeed;
    private String weatherPhenomenon;
    @NotNull
    private Long observationTimestamp;

    public WeatherData() {
    }

    public WeatherData(String station, String stationWMO, float airTemperature, float windSpeed, String weatherPhenomenon, Long observationTimestamp) {
        this.station = station;
        this.stationWMO = stationWMO;
        this.airTemperature = airTemperature;
        this.windSpeed = windSpeed;
        this.weatherPhenomenon = weatherPhenomenon;
        this.observationTimestamp = observationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStationWMO() {
        return stationWMO;
    }

    public void setStationWMO(String stationWMO) {
        this.stationWMO = stationWMO;
    }

    public float getAirTemperature() {
        return airTemperature;
    }

    public void setAirTemperature(float airTemperature) {
        this.airTemperature = airTemperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    public void setWeatherPhenomenon(String weatherPhenomenon) {
        this.weatherPhenomenon = weatherPhenomenon;
    }

    public Long getObservationTimestamp() {
        return observationTimestamp;
    }

    public void setObservationTimestamp(Long observationTimestamp) {
        this.observationTimestamp = observationTimestamp;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "id=" + id +
                ", station='" + station + '\'' +
                ", stationWMO='" + stationWMO + '\'' +
                ", airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", weatherPhenomenon='" + weatherPhenomenon + '\'' +
                ", observationTimestamp=" + observationTimestamp +
                '}';
    }
}