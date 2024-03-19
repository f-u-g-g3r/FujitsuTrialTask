package com.artjomkuznetsov.deliveryfee.models.extra_weather_fee;

import com.artjomkuznetsov.deliveryfee.utils.StringListConverter;
import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "air_temperature_conditions")
public class AirTemperatureConditions extends ExtraWeatherFee {
    @NotNull
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;

    @Convert(converter = StringListConverter.class)
    @Column(name = "vehicle_types")
    private List<String> vehicleTypes = new ArrayList<>();

    private float lessThan;
    private float lessThanFee;

    private float betweenMin;
    private float betweenMax;
    private float betweenFee;

    public AirTemperatureConditions() {}

    public AirTemperatureConditions(List<String> vehicleTypes, float lessThan, float lessThanFee, float betweenMin, float betweenMax, float betweenFee) {
        this.vehicleTypes = vehicleTypes;
        this.lessThan = lessThan;
        this.lessThanFee = lessThanFee;
        this.betweenMin = betweenMin;
        this.betweenMax = betweenMax;
        this.betweenFee = betweenFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

    public float getLessThan() {
        return lessThan;
    }

    public void setLessThan(float lessThan) {
        this.lessThan = lessThan;
    }

    public float getLessThanFee() {
        return lessThanFee;
    }

    public void setLessThanFee(float lessThanFee) {
        this.lessThanFee = lessThanFee;
    }

    public float getBetweenMin() {
        return betweenMin;
    }

    public void setBetweenMin(float betweenMin) {
        this.betweenMin = betweenMin;
    }

    public float getBetweenMax() {
        return betweenMax;
    }

    public void setBetweenMax(float betweenMax) {
        this.betweenMax = betweenMax;
    }

    public float getBetweenFee() {
        return betweenFee;
    }

    public void setBetweenFee(float betweenFee) {
        this.betweenFee = betweenFee;
    }
}
