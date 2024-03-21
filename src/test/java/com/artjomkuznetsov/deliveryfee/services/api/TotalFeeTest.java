package com.artjomkuznetsov.deliveryfee.services.api;

import com.artjomkuznetsov.deliveryfee.services.api.spec.Specifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class TotalFeeTest {
    private static final String url = "http://localhost:8080/fee";

    @BeforeEach
    void setUp() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecOK200());
    }

    @Test
    public void getFeeTallinnCarFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tallinn",
                                "vehicle", "car"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeTartuCarFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tartu",
                                "vehicle", "car"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeParnuCarFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "pärnu",
                                "vehicle", "car"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeTallinnBikeFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tallinn",
                                "vehicle", "bike"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeTartuBikeFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tartu",
                                "vehicle", "bike"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeParnuBikeFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "pärnu",
                                "vehicle", "bike"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeTallinnScooterFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tallinn",
                                "vehicle", "scooter"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeTartuScooterFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "tartu",
                                "vehicle", "scooter"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }

    @Test
    public void getFeeParnuScooterFeeNotNull() {
        given()
                .when()
                .params(Map.of("city", "pärnu",
                                "vehicle", "scooter"))
                .get()
                .then()
                .body("deliveryFee", notNullValue());
    }
}
