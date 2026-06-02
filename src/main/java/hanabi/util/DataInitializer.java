package hanabi.util;

import hanabi.model.Average;
import hanabi.model.Invoice;
import hanabi.model.Order;
import hanabi.model.OrderDetail;
import hanabi.model.Product;
import hanabi.model.Salary;
import hanabi.model.Staff;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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

            createSalary(session, staff1, 5000000.0, 2.0);
            createSalary(session, staff2, 4500000.0, 1.5);
            createSalary(session, staff3, 4800000.0, 2.5);

            createAverage(session, staff1, 85);
            createAverage(session, staff2, 92);
            createAverage(session, staff3, 78);

            Product p1 = createProduct(session, "Matcha Ice Blended", "Iced", 35000.0, 15000.0);
            Product p2 = createProduct(session, "Americano", "Hot", 28000.0, 12000.0);
            Product p3 = createProduct(session, "Croissants", "Bakery", 25000.0, 10000.0);
            Product p4 = createProduct(session, "Ice Latte", "Iced", 25000.0, 12000.0);
            Product p5 = createProduct(session, "Ice Black Coffee", "Iced", 35000.0, 15000.0);
            Product p6 = createProduct(session, "Caramel Machito", "Hot", 40000.0, 18000.0);
            Product p7 = createProduct(session, "Tiramisu", "Bakery", 45000.0, 20000.0);
            Product p8 = createProduct(session, "Lemon Tea", "Iced", 19000.0, 8000.0);
            Product p9 = createProduct(session, "Orange", "Iced", 23000.0, 10000.0);
            Product p10 = createProduct(session, "Espresso", "Hot", 20000.0, 8000.0);
            Product p11 = createProduct(session, "Hot Chocolate", "Hot", 35000.0, 15000.0);
            Product p12 = createProduct(session, "Cappuccino", "Hot", 32000.0, 12000.0);
            Product p13 = createProduct(session, "Hot Matcha Latte", "Hot", 35000.0, 15000.0);
            Product p14 = createProduct(session, "Earl Grey Tea", "Hot", 25000.0, 10000.0);
            Product p15 = createProduct(session, "Iced Mocha", "Iced", 38000.0, 15000.0);
            Product p16 = createProduct(session, "Peach Tea", "Iced", 25000.0, 10000.0);
            Product p17 = createProduct(session, "Cold Brew", "Iced", 35000.0, 15000.0);
            Product p18 = createProduct(session, "Mango Smoothie", "Iced", 40000.0, 18000.0);
            Product p19 = createProduct(session, "Strawberry Macchiato", "Iced", 42000.0, 18000.0);
            Product p20 = createProduct(session, "Cheesecake", "Bakery", 40000.0, 18000.0);
            Product p21 = createProduct(session, "Choco Cookie", "Bakery", 15000.0, 5000.0);
            Product p22 = createProduct(session, "Red Velvet", "Bakery", 45000.0, 20000.0);
            Product p23 = createProduct(session, "Macaron (Set 3)", "Bakery", 30000.0, 12000.0);
            Product p24 = createProduct(session, "Blueberry Muffin", "Bakery", 25000.0, 10000.0);

            Product[] products = {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23, p24};
            Staff[] staffs = {staff1, staff2, staff3};
            ThreadLocalRandom rng = ThreadLocalRandom.current();

            LocalDate today = LocalDate.now();
            int daysInMonth = today.lengthOfMonth();
            int orderCount = 0;

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = today.withDayOfMonth(day);
                if (date.isAfter(today)) break;

                int ordersToday = rng.nextInt(1, 5);

                for (int o = 0; o < ordersToday; o++) {
                    Staff staff = staffs[rng.nextInt(staffs.length)];
                    int itemCount = rng.nextInt(1, 4);
                    int total = 0;
                    UUID orderId = UUID.randomUUID();
                    UUID invoiceId = UUID.randomUUID();

                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(invoiceId);
                    invoice.setStaff(staff);
                    invoice.setInvoiceDate(date);
                    invoice.setStatus(true);

                    Order order = new Order();
                    order.setOrderId(orderId);
                    order.setInvoice(invoice);
                    order.setStaff(staff);
                    order.setOrderDate(date);

                    boolean[] used = new boolean[products.length];
                    for (int i = 0; i < itemCount; i++) {
                        int pi;
                        do { pi = rng.nextInt(products.length); } while (used[pi]);
                        used[pi] = true;
                        int qty = rng.nextInt(1, 4);
                        total += (int) Math.round(products[pi].getPrice() * qty);
                        OrderDetail detail = new OrderDetail();
                        detail.setOrderId(orderId);
                        detail.setProductId(products[pi].getProductId());
                        detail.setQuantity(qty);
                        session.persist(detail);
                    }

                    order.setTotal(total);
                    invoice.setTotal(total);
                    session.persist(invoice);
                    session.persist(order);
                    orderCount++;
                }
            }

            tx.commit();
            System.out.println("DataInitializer: mock data created (" + orderCount + " orders across " + daysInMonth + " days)");
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

    private static void createAverage(Session session, Staff staff, int score) {
        Average avg = new Average();
        avg.setStaff(staff);
        avg.setAverageScore(score);
        session.persist(avg);
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
