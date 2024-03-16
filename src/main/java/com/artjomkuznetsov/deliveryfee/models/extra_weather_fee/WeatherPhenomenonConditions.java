package com.artjomkuznetsov.deliveryfee.models.extra_weather_fee;

import com.artjomkuznetsov.deliveryfee.utils.StringListConverter;
import com.artjomkuznetsov.deliveryfee.models.ExtraWeatherFee;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "weather_phenomenon_conditions")
public class WeatherPhenomenonConditions extends ExtraWeatherFee {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @Convert(converter = StringListConverter.class)
    @Column(name = "vehicle_types")
    private List<String> vehicleTypes = new ArrayList<>();

    private float snowOrSleetFee;
    private float rainFee;

    @Convert(converter = StringListConverter.class)
    @Column(name = "forbidden_phenomenons")
    private List<String> forbiddenPhenomenons = new ArrayList<>();

    public WeatherPhenomenonConditions() {}

    public WeatherPhenomenonConditions(List<String> vehicleTypes, float snowOrSleetFee, float rainFee, List<String> forbiddenPhenomenons) {
        this.vehicleTypes = vehicleTypes;
        this.snowOrSleetFee = snowOrSleetFee;
        this.rainFee = rainFee;
        this.forbiddenPhenomenons = forbiddenPhenomenons;
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

    public float getSnowOrSleetFee() {
        return snowOrSleetFee;
    }

    public void setSnowOrSleetFee(float snowOrSleetFee) {
        this.snowOrSleetFee = snowOrSleetFee;
    }

    public float getRainFee() {
        return rainFee;
    }

    public void setRainFee(float rainFee) {
        this.rainFee = rainFee;
    }

    public List<String> getForbiddenPhenomenons() {
        return forbiddenPhenomenons;
    }

    public void setForbiddenPhenomenons(List<String> forbiddenPhenomenons) {
        this.forbiddenPhenomenons = forbiddenPhenomenons;
    }
}
