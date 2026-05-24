package hanabi.util;

import hanabi.model.Salary;
import hanabi.model.Staff;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DataInitalizer {

    public static void initialize(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            Long count = session.createQuery(
                    "SELECT COUNT(s) FROM Staff s WHERE s.role = :role", Long.class)
                    .setParameter("role", "admin")
                    .getSingleResult();

            if (count != null && count > 0) return;

            Staff admin = new Staff();
            admin.setStaffId(UUID.randomUUID());
            admin.setStaffName("admin");
            admin.setPassword("admin");
            admin.setFullName("Administrator");
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
            System.out.println("DataInitalizer: default admin account created (admin/admin)");
        } catch (Exception e) {
            System.err.println("DataInitalizer: " + e.getMessage());
        }
    }
}
