package com.artjomkuznetsov.deliveryfee.services.api;

import com.artjomkuznetsov.deliveryfee.services.api.spec.Specifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TotalFeeTest {
    private static final String URL = "http://localhost:8080/fee";
    private static final List<String> CITIES = List.of("Tallinn", "Tartu", "Pärnu");
    private static final List<String> VEHICLES = List.of("car", "scooter", "bike");

    @BeforeEach
    void setUp() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
    }

    @Test
    public void getFeeNotNullValue() {
        CITIES.forEach(city -> {
            VEHICLES.forEach(vehicle -> {
                given()
                        .when()
                        .params(Map.of("city", city,
                                "vehicle", vehicle))
                        .get()
                        .then()
                        .body("deliveryFee", notNullValue());
            });
        });
    }

    @Test
    public void getFeeWithDateTimeError400() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecBadRequest400());
        CITIES.forEach(city -> {
            VEHICLES.forEach(vehicle -> {
                given()
                        .when()
                        .params(Map.of(
                                "city", city,
                                "vehicle", vehicle,
                                "dateTime", "2024-03-23T12:55:40"
                        ))
                        .get()
                        .then()
                        .body("error", equalTo("It is not possible to obtain weather data for the specified time period."));
            });
        });
    }

    @Test
    public void getFeeWithDateTimeSuccess() {
        CITIES.forEach(city -> {
            VEHICLES.forEach(vehicle -> {
                given()
                        .when()
                        .params(Map.of(
                                "city", city,
                                "vehicle", vehicle,
                                "dateTime", "2024-03-17T15:17:40"
                        ))
                        .get()
                        .then()
                        .body("deliveryFee", notNullValue());
            });
        });
    }
}
