package com.artjomkuznetsov.deliveryfee.services.api;

import com.artjomkuznetsov.deliveryfee.services.api.spec.Specifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ExtraFeeTest {
    private static final String url = "http://localhost:8080/extra-fees";

    @BeforeEach
    void setUp() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecOK200());
    }

    @Test
    public void getAllExtraFeesAirTemperaturesConditionsListNotNull() {
        given()
                .when()
                .get("")
                .then()
                .body("_embedded.airTemperatureConditionsList", notNullValue());
    }

    @Test
    public void getAllExtraFeesWindSpeedConditionsListNotNull() {
        given()
                .when()
                .get("")
                .then()
                .body("_embedded.windSpeedConditionsList", notNullValue());
    }

    @Test
    public void getAllExtraFeeWeatherPhenomenonConditionsListNotNull() {
        given()
                .when()
                .get("")
                .then()
                .body("_embedded.weatherPhenomenonConditionsList", notNullValue());
    }

    @Test
    public void getAirTemperatureConditionsNotNull() {
        given()
                .when()
                .get("/air")
                .then()
                .body("id", notNullValue());
    }

    @Test
    public void getWindSpeedConditionsNotNull() {
        given()
                .when()
                .get("/wind")
                .then()
                .body("id", notNullValue());
    }

    @Test
    public void getWeatherPhenomenonConditionsNotNull() {
        given()
                .when()
                .get("/phenomenon")
                .then()
                .body("id", notNullValue());
    }

    @Test
    public void updateAirTemperatureConditionsSuccess() {
        Map<String, Float> updatedTempConditions = new HashMap<>();
        updatedTempConditions.put("lessThan", -12f);
        updatedTempConditions.put("lessThanFee", 2.5f);

        given()
                .body(updatedTempConditions)
                .when()
                .put("/air")
                .then()
                .body("lessThan", equalTo(updatedTempConditions.get("lessThan")))
                .body("lessThanFee", equalTo(updatedTempConditions.get("lessThanFee")));
    }

    @Test
    public void updateWindSpeedConditionsSuccess() {
        Map<String, Float> updatedWindConditions = new HashMap<>();
        updatedWindConditions.put("betweenMin", -5f);
        updatedWindConditions.put("betweenMax", -20f);

        given()
                .body(updatedWindConditions)
                .when()
                .put("/wind")
                .then()
                .body("betweenMin", equalTo(updatedWindConditions.get("betweenMin")))
                .body("betweenMax", equalTo(updatedWindConditions.get("betweenMax")));
    }

    @Test
    public void updateWeatherPhenomenonConditionsSuccess() {
        Map<String, Float> updatedPhenomenonConditions = new HashMap<>();
        updatedPhenomenonConditions.put("snowOrSleetFee", 2f);

        given()
                .body(updatedPhenomenonConditions)
                .when()
                .put("/phenomenon")
                .then()
                .body("snowOrSleetFee", equalTo(updatedPhenomenonConditions.get("snowOrSleetFee")));
    }

    @Test
    public void updateAirTemperatureConditionsNegativeFeeError() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecBadRequest400());
        given()
                .body(Map.of("lessThanFee", -2))
                .when()
                .put("/air")
                .then()
                .body("error", equalTo("lessThanFee cannot be negative."));
    }

    @Test
    public void updateAirTemperatureConditionsNonNumberFeeError() {
        Specifications.installSpecification(Specifications.requestSpec(url), Specifications.responseSpecBadRequest400());
        given()
                .body(Map.of("lessThanFee", "abc"))
                .when()
                .put("/air")
                .then()
                .body("error", equalTo("lessThanFee must be a number."));
    }
}
