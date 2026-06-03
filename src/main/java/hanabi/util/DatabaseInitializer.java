package hanabi.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import hanabi.model.Product;
import hanabi.model.Salary;
import hanabi.model.Staff;
import hanabi.model.Tenant;
import java.util.UUID;

public class DatabaseInitializer {

    public static void initialize() {
        MongoDatabase db = MongoDBUtil.getDatabase();
        initializeGlobalCollections(db);
        seedGlobalData(db);
    }

    private static void initializeGlobalCollections(MongoDatabase db) {
        for (String col : new String[]{"tenants"}) {
            if (!collectionExists(db, col)) {
                db.createCollection(col);
            }
        }
    }

    private static boolean collectionExists(MongoDatabase db, String name) {
        for (String colName : db.listCollectionNames()) {
            if (colName.equals(name)) return true;
        }
        return false;
    }

    private static void seedGlobalData(MongoDatabase db) {
        MongoCollection<Tenant> tenantCol = db.getCollection("tenants", Tenant.class);
        if (tenantCol.countDocuments() == 0) {
            createDefaultTenant(tenantCol, db);
        }
    }

    private static void createDefaultTenant(MongoCollection<Tenant> tenantCol, MongoDatabase db) {
        UUID tenantId = UUID.randomUUID();
        String salt = PasswordUtil.generateSalt();
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        tenant.setTenantName("hanabi");
        tenant.setCafeName("HANABI CAFE");
        tenant.setFullName("System Admin");
        tenant.setPassword(salt + ":" + PasswordUtil.hash("admin", salt));
        tenant.setStatus(true);
        tenantCol.insertOne(tenant);

        UUID oldTenantId = TenantContext.getCurrentTenantId();
        TenantContext.setCurrentTenantId(tenantId);
        try {
            initializeTenantCollections(db, tenantId);
            seedTenantData(db, tenantId);
        } finally {
            if (oldTenantId != null) {
                TenantContext.setCurrentTenantId(oldTenantId);
            } else {
                TenantContext.clear();
            }
        }
        System.out.println("DatabaseInitializer: default tenant created (hanabi / admin)");
    }

    private static void initializeTenantCollections(MongoDatabase db, UUID tenantId) {
        String prefix = MongoDBUtil.tenantPrefix(tenantId);
        for (String col : new String[]{"staff", "products", "invoices", "orders", "orders_details", "salaries", "chat_messages"}) {
            String fullName = prefix + col;
            if (!collectionExists(db, fullName)) {
                db.createCollection(fullName);
            }
        }
    }

    private static void seedTenantData(MongoDatabase db, UUID tenantId) {
        String prefix = MongoDBUtil.tenantPrefix(tenantId);
        MongoCollection<Staff> staffCol = db.getCollection(prefix + "staff", Staff.class);
        MongoCollection<Salary> salaryCol = db.getCollection(prefix + "salaries", Salary.class);
        MongoCollection<Product> productCol = db.getCollection(prefix + "products", Product.class);

        if (staffCol.countDocuments(Filters.eq("role", "admin")) > 0) return;
        if (productCol.countDocuments() > 0) return;

        String salt = PasswordUtil.generateSalt();

        Staff adminStaff = new Staff();
        adminStaff.setStaffId(tenantId);
        adminStaff.setStaffName("admin");
        adminStaff.setFullName("System Admin");
        adminStaff.setPassword(salt + ":" + PasswordUtil.hash("admin", salt));
        adminStaff.setRole("admin");
        adminStaff.setStatus(true);
        staffCol.insertOne(adminStaff);

        Salary adminSalary = new Salary();
        adminSalary.setStaffId(tenantId);
        adminSalary.setBaseSalary(0.0);
        adminSalary.setCommissionRate(0.0);
        salaryCol.insertOne(adminSalary);

        Staff staff1 = createStaff(staffCol, "Nguyen", "Nguyen", "staff", "staff123", salt);
        Staff staff2 = createStaff(staffCol, "Dat", "Tran Dat", "staff", "staff123", salt);
        Staff staff3 = createStaff(staffCol, "Le", "Le", "staff", "staff123", salt);

        createSalary(salaryCol, staff1.getStaffId(), 5000000.0, global.COMMISSION_RATE);
        createSalary(salaryCol, staff2.getStaffId(), 4500000.0, global.COMMISSION_RATE);
        createSalary(salaryCol, staff3.getStaffId(), 4800000.0, global.COMMISSION_RATE);

        createProduct(productCol, "Matcha Ice Blended", "Iced", 35000.0, 15000.0,"1");
        createProduct(productCol, "Americano", "Hot", 28000.0, 12000.0, "2");
        createProduct(productCol, "Croissants", "Bakery", 25000.0, 10000.0, "3");
        createProduct(productCol, "Ice Latte", "Iced", 25000.0, 12000.0, "4");
        createProduct(productCol, "Ice Black Coffee", "Iced", 35000.0, 15000.0, "5");
        createProduct(productCol, "Caramel Machito", "Hot", 40000.0, 18000.0, "6");
        createProduct(productCol, "Tiramisu", "Bakery", 45000.0, 20000.0, "7");
        createProduct(productCol, "Lemon Tea", "Iced", 19000.0, 8000.0, "8");
        createProduct(productCol, "Orange", "Iced", 23000.0, 10000.0, "9");
        createProduct(productCol, "Espresso", "Hot", 20000.0, 8000.0, "10");
        createProduct(productCol, "Hot Chocolate", "Hot", 35000.0, 15000.0, "11");
        createProduct(productCol, "Cappuccino", "Hot", 32000.0, 12000.0, "12");
        createProduct(productCol, "Hot Matcha Latte", "Hot", 35000.0, 15000.0, "13");
        createProduct(productCol, "Earl Grey Tea", "Hot", 25000.0, 10000.0, "14");
        createProduct(productCol, "Iced Mocha", "Iced", 38000.0, 15000.0, "15");
        createProduct(productCol, "Peach Tea", "Iced", 25000.0, 10000.0, "16");
        createProduct(productCol, "Cold Brew", "Iced", 35000.0, 15000.0, "17");
        createProduct(productCol, "Mango Smoothie", "Iced", 40000.0, 18000.0, "18");
        createProduct(productCol, "Strawberry Macchiato", "Iced", 42000.0, 18000.0, "19");
        createProduct(productCol, "Cheesecake", "Bakery", 40000.0, 18000.0, "20");
        createProduct(productCol, "Choco Cookie", "Bakery", 15000.0, 5000.0, "21");
        createProduct(productCol, "Red Velvet", "Bakery", 45000.0, 20000.0, "22");
        createProduct(productCol, "Macaron (Set 3)", "Bakery", 30000.0, 12000.0, "23");
        createProduct(productCol, "Blueberry Muffin", "Bakery", 25000.0, 10000.0, "24");

        System.out.println("DatabaseInitializer: Tenant data created");
    }

    private static Staff createStaff(MongoCollection<Staff> col, String username, String fullName, String role, String password, String salt) {
        Staff staff = new Staff();
        staff.setStaffId(UUID.randomUUID());
        staff.setStaffName(username);
        staff.setFullName(fullName);
        staff.setPassword(salt + ":" + PasswordUtil.hash(password, salt));
        staff.setRole(role);
        staff.setStatus(true);
        col.insertOne(staff);
        return staff;
    }

    private static void createSalary(MongoCollection<Salary> col, UUID staffId, double base, double commission) {
        Salary salary = new Salary();
        salary.setStaffId(staffId);
        salary.setBaseSalary(base);
        salary.setCommissionRate(commission);
        col.insertOne(salary);
    }

    private static void createProduct(MongoCollection<Product> col, String name, String category, double price, double cost, String image) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setCost(cost);
        product.setImage(image);
        product.setStatus(true);
        col.insertOne(product);
    }
}
