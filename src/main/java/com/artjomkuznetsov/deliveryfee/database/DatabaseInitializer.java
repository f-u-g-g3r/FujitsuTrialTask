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
                        "('Tallinn', 4.0, 3.5, 3.0), ('Tartu', 3.5, 3.0, 2.5), ('Pärnu', 3.0, 2.5, 2.0);");



        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            e.printStackTrace();
        }

    }
}
