package com.artjomkuznetsov.deliveryfee.database;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {
    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void initializeDatabase() {
        try (Connection connection = dataSource.getConnection()) {

            Statement insertRegionalBaseFee = connection.createStatement();
            insertRegionalBaseFee.execute(
                    "INSERT INTO regional_base_fee (city, car_fee, scooter_fee, bike_fee) VALUES " +
                        "('tallinn', 4.0, 3.5, 3.0), ('tartu', 3.5, 3.0, 2.5), ('pärnu', 3.0, 2.5, 2.0);");

            Statement insertAirTemperatureConditions = connection.createStatement();
            insertAirTemperatureConditions.execute(
                    "INSERT INTO air_temperature_conditions " +
                            "(vehicle_types, less_than, less_than_fee, between_min, between_max, between_fee) VALUES " +
                            "('scooter, bike', -10.0, 1.0, -10.0, 0, 0.5)");

            Statement insertWindSpeedConditions = connection.createStatement();
            insertWindSpeedConditions.execute(
                    "INSERT INTO wind_speed_conditions " +
                            "(vehicle_types, between_min, between_max, between_fee, forbidden_speed) VALUES " +
                            "('bike', 10.0, 20.0, 0.5, 20)");

            Statement insertWeatherPhenomenonConditions = connection.createStatement();
            insertWeatherPhenomenonConditions.execute(
                    "INSERT INTO weather_phenomenon_conditions " +
                            "(vehicle_types, snow_or_sleet_fee, rain_fee, forbidden_phenomenons) VALUES " +
                            "('scooter, bike', 1.0, 0.5, 'Glaze, Hail, Thunder')");

        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
        }

    }
}
