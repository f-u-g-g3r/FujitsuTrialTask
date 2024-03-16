package com.artjomkuznetsov.deliveryfee.models.extra_weather_fee;

import com.artjomkuznetsov.deliveryfee.utils.StringListConverter;
import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wind_speed_conditions")
public class WindSpeedConditions extends ExtraWeatherFee {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Convert(converter = StringListConverter.class)
    @Column(name = "vehicle_types")
    private List<String> vehicleTypes = new ArrayList<>();

    private float betweenMin;
    private float betweenMax;
    private float betweenFee;
    private float forbiddenSpeed;

    public WindSpeedConditions() {}

    public WindSpeedConditions(List<String> vehicleTypes, float betweenMin, float betweenMax, float betweenFee, float forbiddenSpeed) {
        this.vehicleTypes = vehicleTypes;
        this.betweenMin = betweenMin;
        this.betweenMax = betweenMax;
        this.betweenFee = betweenFee;
        this.forbiddenSpeed = forbiddenSpeed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<String> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
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

    public float getForbiddenSpeed() {
        return forbiddenSpeed;
    }

    public void setForbiddenSpeed(float forbiddenSpeed) {
        this.forbiddenSpeed = forbiddenSpeed;
    }
}
