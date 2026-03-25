package StoreManagement.Utility;

import java.sql.*;

public class dbConnect {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/quanly";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Không tìm thấy Driver MySQL!", ex);
        }
    }

   
}