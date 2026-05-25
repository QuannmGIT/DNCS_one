package hanabi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {

    private static final String DB_URL = global.DB_URL;
    private static final String DB_NAME = global.DB_NAME;
    private static final String USER = global.USER;
    private static final String PASSWORD = global.PASSWORD;

    public static void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + DB_NAME
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot create database '" + DB_NAME + "'", e);
        }
    }
}
