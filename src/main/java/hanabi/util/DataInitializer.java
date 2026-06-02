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

            Staff staff1 = createStaff(session, "alice", "Alice Nguyen", "staff", "staff123", salt);
            Staff staff2 = createStaff(session, "bob", "Bob Tran", "staff", "staff123", salt);
            Staff staff3 = createStaff(session, "carol", "Carol Le", "staff", "staff123", salt);

            createSalary(session, staff1, 5000000.0, global.COMMISSION_RATE);
            createSalary(session, staff2, 4500000.0, global.COMMISSION_RATE);
            createSalary(session, staff3, 4800000.0, global.COMMISSION_RATE);

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

    private static Product createProduct(Session session, String name, String category, double price, double cost) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setCost(cost);
        product.setStatus(true);
        session.persist(product);
        return product;
    }
}
