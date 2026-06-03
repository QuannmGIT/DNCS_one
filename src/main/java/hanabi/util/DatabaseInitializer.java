package hanabi.util;

import hanabi.model.Product;
import hanabi.model.Salary;
import hanabi.model.Staff;
import java.util.UUID;

public class DatabaseInitializer {

    public static void initialize() {
        createTables();
        seedData();
    }

    private static void createTables() {
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS staff (" +
            "staff_id UUID PRIMARY KEY, " +
            "staff_name VARCHAR(255) UNIQUE NOT NULL, " +
            "email VARCHAR(255), " +
            "password VARCHAR(512) NOT NULL, " +
            "full_name VARCHAR(255), " +
            "role VARCHAR(50) NOT NULL, " +
            "status BOOLEAN DEFAULT true)"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS products (" +
            "product_id UUID PRIMARY KEY, " +
            "product_name VARCHAR(255) NOT NULL, " +
            "category VARCHAR(100), " +
            "price DOUBLE PRECISION NOT NULL, " +
            "cost DOUBLE PRECISION NOT NULL, " +
            "image VARCHAR(255), " +
            "status BOOLEAN DEFAULT true)"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS invoices (" +
            "invoice_id UUID PRIMARY KEY, " +
            "staff_id UUID REFERENCES staff(staff_id), " +
            "invoice_date DATE NOT NULL, " +
            "total INTEGER NOT NULL, " +
            "status BOOLEAN DEFAULT true)"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS orders (" +
            "order_id UUID PRIMARY KEY, " +
            "invoice_id UUID REFERENCES invoices(invoice_id), " +
            "staff_id UUID REFERENCES staff(staff_id), " +
            "status BOOLEAN DEFAULT true, " +
            "order_date DATE NOT NULL, " +
            "total INTEGER NOT NULL)"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS orders_details (" +
            "order_id UUID REFERENCES orders(order_id), " +
            "product_id UUID REFERENCES products(product_id), " +
            "quantity INTEGER NOT NULL, " +
            "PRIMARY KEY (order_id, product_id))"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS salaries (" +
            "staff_id UUID PRIMARY KEY REFERENCES staff(staff_id), " +
            "base_salary DOUBLE PRECISION NOT NULL, " +
            "commission_rate DOUBLE PRECISION NOT NULL)"
        );
        SupabaseUtil.execute(
            "CREATE TABLE IF NOT EXISTS chat_messages (" +
            "message_id UUID PRIMARY KEY, " +
            "sender_id UUID REFERENCES staff(staff_id), " +
            "receiver_id UUID REFERENCES staff(staff_id), " +
            "content TEXT, " +
            "message_type VARCHAR(20) DEFAULT 'TEXT', " +
            "file_path VARCHAR(255), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        );
    }

    private static void seedData() {
        Staff admin = SupabaseUtil.querySingle(
                "SELECT * FROM staff WHERE role = ? AND status = true LIMIT 1",
                rs -> {
                    Staff s = new Staff();
                    s.setStaffId(UUID.fromString(rs.getString("staff_id")));
                    s.setRole(rs.getString("role"));
                    return s;
                }, "admin");
        if (admin == null) {
            createAdminAccount();
        }

        Long productCount = SupabaseUtil.querySingle(
                "SELECT COUNT(*) AS cnt FROM products",
                rs -> rs.getLong("cnt"));
        if (productCount != null && productCount == 0) {
            createMockData();
        }
    }

    private static void createAdminAccount() {
        String salt = PasswordUtil.generateSalt();
        UUID adminId = UUID.randomUUID();
        SupabaseUtil.update(
                "INSERT INTO staff (staff_id, staff_name, email, password, full_name, role, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                adminId, "admin", null, salt + ":" + PasswordUtil.hash("admin", salt),
                "System Admin", "admin", true);
        SupabaseUtil.update(
                "INSERT INTO salaries (staff_id, base_salary, commission_rate) VALUES (?::uuid, ?, ?)",
                adminId, 0.0, 0.0);
        System.out.println("DatabaseInitializer: default admin account created (admin/admin)");
    }

    private static void createMockData() {
        String salt = PasswordUtil.generateSalt();

        UUID staff1Id = UUID.randomUUID();
        UUID staff2Id = UUID.randomUUID();
        UUID staff3Id = UUID.randomUUID();

        SupabaseUtil.update(
                "INSERT INTO staff (staff_id, staff_name, email, password, full_name, role, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                staff1Id, "Nguyen", null, salt + ":" + PasswordUtil.hash("staff123", salt),
                "Nguyen", "staff", true);
        SupabaseUtil.update(
                "INSERT INTO staff (staff_id, staff_name, email, password, full_name, role, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                staff2Id, "Dat", null, salt + ":" + PasswordUtil.hash("staff123", salt),
                "Tran Dat", "staff", true);
        SupabaseUtil.update(
                "INSERT INTO staff (staff_id, staff_name, email, password, full_name, role, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                staff3Id, "Le", null, salt + ":" + PasswordUtil.hash("staff123", salt),
                "Le", "staff", true);

        SupabaseUtil.update("INSERT INTO salaries (staff_id, base_salary, commission_rate) VALUES (?::uuid, ?, ?)",
                staff1Id, 5000000.0, global.COMMISSION_RATE);
        SupabaseUtil.update("INSERT INTO salaries (staff_id, base_salary, commission_rate) VALUES (?::uuid, ?, ?)",
                staff2Id, 4500000.0, global.COMMISSION_RATE);
        SupabaseUtil.update("INSERT INTO salaries (staff_id, base_salary, commission_rate) VALUES (?::uuid, ?, ?)",
                staff3Id, 4800000.0, global.COMMISSION_RATE);

        createProduct("Matcha Ice Blended", "Iced", 35000.0, 15000.0,"1");
        createProduct("Americano", "Hot", 28000.0, 12000.0, "2");
        createProduct("Croissants", "Bakery", 25000.0, 10000.0, "3");
        createProduct("Ice Latte", "Iced", 25000.0, 12000.0, "4");
        createProduct("Ice Black Coffee", "Iced", 35000.0, 15000.0, "5");
        createProduct("Caramel Machito", "Hot", 40000.0, 18000.0, "6");
        createProduct("Tiramisu", "Bakery", 45000.0, 20000.0, "7");
        createProduct("Lemon Tea", "Iced", 19000.0, 8000.0, "8");
        createProduct("Orange", "Iced", 23000.0, 10000.0, "9");
        createProduct("Espresso", "Hot", 20000.0, 8000.0, "10");
        createProduct("Hot Chocolate", "Hot", 35000.0, 15000.0, "11");
        createProduct("Cappuccino", "Hot", 32000.0, 12000.0, "12");
        createProduct("Hot Matcha Latte", "Hot", 35000.0, 15000.0, "13");
        createProduct("Earl Grey Tea", "Hot", 25000.0, 10000.0, "14");
        createProduct("Iced Mocha", "Iced", 38000.0, 15000.0, "15");
        createProduct("Peach Tea", "Iced", 25000.0, 10000.0, "16");
        createProduct("Cold Brew", "Iced", 35000.0, 15000.0, "17");
        createProduct("Mango Smoothie", "Iced", 40000.0, 18000.0, "18");
        createProduct("Strawberry Macchiato", "Iced", 42000.0, 18000.0, "19");
        createProduct("Cheesecake", "Bakery", 40000.0, 18000.0, "20");
        createProduct("Choco Cookie", "Bakery", 15000.0, 5000.0, "21");
        createProduct("Red Velvet", "Bakery", 45000.0, 20000.0, "22");
        createProduct("Macaron (Set 3)", "Bakery", 30000.0, 12000.0, "23");
        createProduct("Blueberry Muffin", "Bakery", 25000.0, 10000.0, "24");

        System.out.println("DatabaseInitializer: Mock data created");
    }

    private static void createProduct(String name, String category, double price, double cost, String image) {
        SupabaseUtil.update(
                "INSERT INTO products (product_id, product_name, category, price, cost, image, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                UUID.randomUUID(), name, category, price, cost, image, true);
    }
}
