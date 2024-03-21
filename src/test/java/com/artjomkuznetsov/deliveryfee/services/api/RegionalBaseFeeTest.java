package com.artjomkuznetsov.deliveryfee.services.api;


import com.artjomkuznetsov.deliveryfee.services.api.spec.Specifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class RegionalBaseFeeTest {
    private static final String url = "http://localhost:8080/base-fees";

    @BeforeEach
    void setUp() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecOK200());
    }

    @Test
    public void getAllRegionalBaseFeesNotNullValue() {
        given()
                .when()
                .get("")
                .then()
                .body("_embedded.regionalBaseFeeList", notNullValue());
    }

    @Test
    public void updateTallinnCarFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newCarFee = 5.5f;
        updatedBaseFee.put("carFee", newCarFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tallinn")
                .then()
                .body("carFee", equalTo(newCarFee));
    }

    @Test
    public void updateTallinnBikeFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newBikeFee = 5.5f;
        updatedBaseFee.put("bikeFee", newBikeFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tallinn")
                .then()
                .body("bikeFee", equalTo(newBikeFee));
    }

    @Test
    public void updateTallinnScooterFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newScooterFee = 5.5f;
        updatedBaseFee.put("scooterFee", newScooterFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tallinn")
                .then()
                .body("scooterFee", equalTo(newScooterFee));
    }

    @Test
    public void updateTartuCarFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newCarFee = 5.5f;
        updatedBaseFee.put("carFee", newCarFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tartu")
                .then()
                .body("carFee", equalTo(newCarFee));
    }

    @Test
    public void updateTartuBikeFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newBikeFee = 5.5f;
        updatedBaseFee.put("bikeFee", newBikeFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tartu")
                .then()
                .body("bikeFee", equalTo(newBikeFee));
    }

    @Test
    public void updateTartuScooterFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newScooterFee = 5.5f;
        updatedBaseFee.put("scooterFee", newScooterFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/tartu")
                .then()
                .body("scooterFee", equalTo(newScooterFee));
    }

    @Test
    public void updateParnuCarFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newCarFee = 5.5f;
        updatedBaseFee.put("carFee", newCarFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/pärnu")
                .then()
                .body("carFee", equalTo(newCarFee));
    }

    @Test
    public void updateParnuBikeFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newBikeFee = 5.5f;
        updatedBaseFee.put("bikeFee", newBikeFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/pärnu")
                .then()
                .body("bikeFee", equalTo(newBikeFee));
    }

    @Test
    public void updateParnuScooterFee() {
        Map<String, Float> updatedBaseFee = new HashMap<>();
        Float newScooterFee = 5.5f;
        updatedBaseFee.put("scooterFee", newScooterFee);

        given()
                .body(updatedBaseFee)
                .when()
                .put("/pärnu")
                .then()
                .body("scooterFee", equalTo(newScooterFee));
    }
    
    @Test
    public void getRegionalBaseFeeBySpecificCity() {
        List<String> cities = List.of("tallinn", "tartu", "pärnu");
        cities.forEach(city -> {
            given()
                    .when()
                    .get("/" + city)
                    .then()
                    .body("city", equalTo(city));
        });
    }

    @Test
    public void getRegionalBaseFeeByWrongCity() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecNotFound404());
        String city = "wrongCity";
        given()
                .when()
                .get("/" + city)
                .then()
                .body("error", equalTo("Regional base fee for city " + city + " is not found."));

    }
}
