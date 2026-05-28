package hanabi.dao;

import hanabi.model.Salary;
import hanabi.util.HibernateUtil;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class SalaryDAO extends BaseDAO<Salary, UUID> {

    public SalaryDAO() {
        super(Salary.class);
    }

    public List<Object[]> findAllWithStaff() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT s.staffId, s.fullName, s.role, " +
                    "sa.baseSalary, sa.commissionRate " +
                    "FROM Staff s LEFT JOIN s.salary sa " +
                    "ORDER BY s.staffId", Object[].class)
                    .list();
        }
    }

    public List<Object[]> findAllWithStaffAndTotals() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT s.staffId, s.fullName, s.role, " +
                    "sa.baseSalary, sa.commissionRate, " +
                    "(COALESCE(sa.baseSalary, 0) + COALESCE(sa.baseSalary, 0) * COALESCE(sa.commissionRate, 0) / 100) " +
                    "FROM Staff s LEFT JOIN s.salary sa " +
                    "ORDER BY s.staffId", Object[].class)
                    .list();
        }
    }

    public Double getTotalByStaffId(UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT (COALESCE(sa.baseSalary, 0) + COALESCE(sa.baseSalary, 0) * COALESCE(sa.commissionRate, 0) / 100) " +
                    "FROM Salary sa WHERE sa.staffId = :staffId", Double.class)
                    .setParameter("staffId", staffId)
                    .uniqueResult();
        }
    }
}
