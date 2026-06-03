package hanabi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TableDBInitializer {

    private static final String DB_URL = global.DB_URL;
    private static final String USER = global.USER;
    private static final String PASSWORD = global.PASSWORD;

    private static final String[] CREATE_TABLES = {

            "CREATE TABLE IF NOT EXISTS staff ("
                    + "  staff_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  staff_name VARCHAR(50) NOT NULL UNIQUE,"
                    + "  email VARCHAR(100) DEFAULT NULL,"
                    + "  password VARCHAR(255) NOT NULL,"
                    + "  full_name VARCHAR(100) DEFAULT NULL,"
                    + "  role ENUM('admin','staff') DEFAULT 'staff',"
                    + "  status TINYINT(1) DEFAULT 1"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS products ("
                    + "  product_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  product_name VARCHAR(50) NOT NULL UNIQUE,"
                    + "  category VARCHAR(100) DEFAULT NULL,"
                    + "  price DECIMAL DEFAULT NULL,"
                    + "  cost DECIMAL DEFAULT NULL,"
                    + "  image VARCHAR(255) DEFAULT NULL,"
                    + "  status TINYINT(1) DEFAULT 1"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS invoices ("
                    + "  invoice_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  staff_id BINARY(16) NOT NULL,"
                    + "  invoice_date DATE DEFAULT NULL,"
                    + "  total INT DEFAULT NULL,"
                    + "  status TINYINT(1) DEFAULT 1,"
                    + "  INDEX idx_invoices_staff (staff_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS orders ("
                    + "  order_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  invoice_id BINARY(16) NOT NULL,"
                    + "  staff_id BINARY(16) NOT NULL,"
                    + "  status TINYINT(1) DEFAULT 1,"
                    + "  order_date DATE DEFAULT NULL,"
                    + "  total INT DEFAULT NULL,"
                    + "  INDEX idx_orders_staff (staff_id),"
                    + "  INDEX idx_orders_invoice (invoice_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS orders_details ("
                    + "  order_id BINARY(16) NOT NULL,"
                    + "  product_id BINARY(16) NOT NULL,"
                    + "  quantity INT DEFAULT NULL,"
                    + "  PRIMARY KEY (order_id, product_id),"
                    + "  INDEX idx_od_product (product_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS salaries ("
                    + "  staff_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  baseSalary DECIMAL DEFAULT NULL,"
                    + "  commissionRate DECIMAL DEFAULT NULL"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",

            "CREATE TABLE IF NOT EXISTS chat_messages ("
                    + "  message_id BINARY(16) NOT NULL PRIMARY KEY,"
                    + "  sender_id BINARY(16) NOT NULL,"
                    + "  receiver_id BINARY(16) NOT NULL,"
                    + "  content TEXT NOT NULL,"
                    + "  message_type VARCHAR(10) DEFAULT 'TEXT',"
                    + "  file_path VARCHAR(500) DEFAULT NULL,"
                    + "  created_at DATETIME NOT NULL,"
                    + "  INDEX idx_chat_sender (sender_id),"
                    + "  INDEX idx_chat_receiver (receiver_id)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci"
    };

    public static void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                    Statement stmt = conn.createStatement()) {
                for (String ddl : CREATE_TABLES) {
                    stmt.executeUpdate(ddl);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot create tables", e);
        }
    }
}
