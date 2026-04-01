package edu.icet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection dbConnection;
    private Connection connection;

    // Private constructor to prevent external instantiation
    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // NOTE: Update "root" and "1234" to your actual MySQL username and password
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pharmacy_db",
                    "root",
                    "mysqllol@123"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found!", e);
        }
    }

    // Global access point
    public static DBConnection getInstance() throws SQLException {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    // Method to get the actual connection
    public Connection getConnection() {
        return connection;
    }
}