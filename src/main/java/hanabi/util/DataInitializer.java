package hanabi.util;

import hanabi.model.Product;
import hanabi.model.Salary;
import hanabi.model.Staff;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DataInitializer {

    public static void initialize(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            Long adminCount = session.createQuery(
                    "SELECT COUNT(s) FROM Staff s WHERE s.role = :role", Long.class)
                    .setParameter("role", "admin")
                    .getSingleResult();

            if (adminCount == null || adminCount == 0) {
                createAdminAccount(session);
            }

            Long productCount = session.createQuery(
                    "SELECT COUNT(p) FROM Product p", Long.class)
                    .getSingleResult();

            if (productCount == null || productCount == 0) {
                createMockData(session);
            }
        } catch (Exception e) {
            System.err.println("DataInitializer: " + e.getMessage());
        }
    }

    private static void createAdminAccount(Session session) {
        String salt = PasswordUtil.generateSalt();
        Staff admin = new Staff();
        admin.setStaffId(UUID.randomUUID());
        admin.setStaffName("admin");
        admin.setFullName("System Admin");
        admin.setPassword(salt + ":" + PasswordUtil.hash("admin", salt));
        admin.setRole("admin");
        admin.setStatus(true);

        Salary salary = new Salary();
        salary.setStaff(admin);
        salary.setBaseSalary(0.0);
        salary.setCommissionRate(0.0);

        Transaction tx = session.beginTransaction();
        session.persist(admin);
        session.persist(salary);
        tx.commit();
        System.out.println("DataInitializer: default admin account created (admin/admin)");
    }

    private static void createMockData(Session session) {
        Transaction tx = session.beginTransaction();
        try {
            String salt = PasswordUtil.generateSalt();

            Staff staff1 = createStaff(session, "Nguyen", "Nguyen", "staff", "staff123", salt);
            Staff staff2 = createStaff(session, "Dat", "Tran Dat", "staff", "staff123", salt);
            Staff staff3 = createStaff(session, "Le", "Le", "staff", "staff123", salt);

            createSalary(session, staff1, 5000000.0, global.COMMISSION_RATE);
            createSalary(session, staff2, 4500000.0, global.COMMISSION_RATE);
            createSalary(session, staff3, 4800000.0, global.COMMISSION_RATE);

            createProduct(session, "Matcha Ice Blended", "Iced", 35000.0, 15000.0,"1");
            createProduct(session, "Americano", "Hot", 28000.0, 12000.0, "2");
            createProduct(session, "Croissants", "Bakery", 25000.0, 10000.0, "3");
            createProduct(session, "Ice Latte", "Iced", 25000.0, 12000.0, "4");
            createProduct(session, "Ice Black Coffee", "Iced", 35000.0, 15000.0, "5");
            createProduct(session, "Caramel Machito", "Hot", 40000.0, 18000.0, "6");
            createProduct(session, "Tiramisu", "Bakery", 45000.0, 20000.0, "7");
            createProduct(session, "Lemon Tea", "Iced", 19000.0, 8000.0, "8");
            createProduct(session, "Orange", "Iced", 23000.0, 10000.0, "9");
            createProduct(session, "Espresso", "Hot", 20000.0, 8000.0, "10");
            createProduct(session, "Hot Chocolate", "Hot", 35000.0, 15000.0, "11");
            createProduct(session, "Cappuccino", "Hot", 32000.0, 12000.0, "12");
            createProduct(session, "Hot Matcha Latte", "Hot", 35000.0, 15000.0, "13");
            createProduct(session, "Earl Grey Tea", "Hot", 25000.0, 10000.0, "14");
            createProduct(session, "Iced Mocha", "Iced", 38000.0, 15000.0, "15");
            createProduct(session, "Peach Tea", "Iced", 25000.0, 10000.0, "16");
            createProduct(session, "Cold Brew", "Iced", 35000.0, 15000.0, "17");
            createProduct(session, "Mango Smoothie", "Iced", 40000.0, 18000.0, "18");
            createProduct(session, "Strawberry Macchiato", "Iced", 42000.0, 18000.0, "19");
            createProduct(session, "Cheesecake", "Bakery", 40000.0, 18000.0, "20");
            createProduct(session, "Choco Cookie", "Bakery", 15000.0, 5000.0, "21");
            createProduct(session, "Red Velvet", "Bakery", 45000.0, 20000.0, "22");
            createProduct(session, "Macaron (Set 3)", "Bakery", 30000.0, 12000.0, "23");
            createProduct(session, "Blueberry Muffin", "Bakery", 25000.0, 10000.0, "24");

            tx.commit();
            System.out.println("Mock Data init completed");
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    private static Staff createStaff(Session session, String username, String fullName, String role, String password, String salt) {
        Staff staff = new Staff();
        staff.setStaffId(UUID.randomUUID());
        staff.setStaffName(username);
        staff.setFullName(fullName);
        staff.setPassword(salt + ":" + PasswordUtil.hash(password, salt));
        staff.setRole(role);
        staff.setStatus(true);
        session.persist(staff);
        return staff;
    }

    private static void createSalary(Session session, Staff staff, double base, double commission) {
        Salary salary = new Salary();
        salary.setStaff(staff);
        salary.setBaseSalary(base);
        salary.setCommissionRate(commission);
        session.persist(salary);
    }

    private static Product createProduct(Session session, String name, String category, double price, double cost, String image) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setCost(cost);
        product.setImage(image);
        product.setStatus(true);
        session.persist(product);
        return product;
    }
}
