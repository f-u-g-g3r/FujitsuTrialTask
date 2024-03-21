package com.artjomkuznetsov.deliveryfee.models;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "regional_base_fee")
@Schema(description = "Pet model")
public class RegionalBaseFee {
    @Schema(description = "ID of the pet")
    @NotNull
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id int id;

    @NotNull
    private String city;

    @NotNull
    private float carFee;

    @NotNull
    private float bikeFee;

    @NotNull
    private float scooterFee;

    public RegionalBaseFee() {}

    public RegionalBaseFee(String city, float carFee, float scooterFee, float bikeFee) {
        this.city = city;
        this.carFee = carFee;
        this.scooterFee = scooterFee;
        this.bikeFee = bikeFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getCarFee() {
        return carFee;
    }

    public void setCarFee(float carFee) {
        this.carFee = carFee;
    }

    public float getBikeFee() {
        return bikeFee;
    }

    public void setBikeFee(float bikeFee) {
        this.bikeFee = bikeFee;
    }

    public float getScooterFee() {
        return scooterFee;
    }

    public void setScooterFee(float scooterFee) {
        this.scooterFee = scooterFee;
    }

    @Override
    public String toString() {
        return "RegionalBaseFee{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", carFee=" + carFee +
                ", bikeFee=" + bikeFee +
                ", scooterFee=" + scooterFee +
                '}';
    }
}
