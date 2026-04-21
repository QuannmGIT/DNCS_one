package StoreManagement.Utility;

import java.sql.*;

public class DATABBASE {

    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/StoreManagement";
    private static final String DEFAULT_DATABASE_USER = "root";
    private static final String DEFAULT_DB_PASSWORD = "";
    private final String URL;
    private final String DATABASE_NAME;
    private final String USER;
    private final String PASSWORD;
    private Connection connection;

    public DATABBASE(String NameDatabase, String user, String password, String databaseURL) {
        if (NameDatabase == null || databaseURL == null) {
            URL = DEFAULT_DB_URL;
            DATABASE_NAME = "StoreManagement";
        } else {
            DATABASE_NAME = NameDatabase;
            URL = "jdbc:mysql://" + databaseURL + "/" + NameDatabase;
        }

        if (user == null) {
            USER = DEFAULT_DATABASE_USER;
        } else
            USER = user;

        if (password == null) {
            PASSWORD = DEFAULT_DB_PASSWORD;
        } else
            PASSWORD = password;
    }

    public Connection testConnection(){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
            return connection;
        } catch (SQLException ex) {
            System.err.println("Connection failed!" + ex.getMessage());
            return null;
        }
    }

    static void main(String[] args) {
        DATABBASE db = new DATABBASE("test", "root", "", "localhost:3306");
        db.testConnection();
    }
}