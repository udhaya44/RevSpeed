package com.revspeed.service;

import com.revspeed.main.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection con = null;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                con = DriverManager.getConnection(properties.getProperty("DB_URL"), properties.getProperty("DB_USERNAME"), properties.getProperty("DB_PASSWORD"));
                System.out.println("Connection established");
                logger.info("Database connection established {}", DatabaseConnection.class.getSimpleName());
            } else {
                System.out.println("Unable to find config.properties");
            }
        } catch (IOException | SQLException e) {
            System.out.println("Connection not established");
            logger.error("Exception in getting DB_connection");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }
}
