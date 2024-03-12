package com.artjomkuznetsov.deliveryfee.models;

import jakarta.persistence.*;

@Entity
@Table(name = "regional_base_fee")
public class RegionalBaseFee {
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;
    private String city;
    private float carFee;
    private float bikeFee;
    private float scooterFee;

    public RegionalBaseFee() {}

    public RegionalBaseFee(Long id, String city, int carFee, int bikeFee, int scooterFee) {
        this.id = id;
        this.city = city;
        this.carFee = carFee;
        this.bikeFee = bikeFee;
        this.scooterFee = scooterFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
