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
                    "sa.baseSalary, " +
                    "COALESCE((SELECT SUM(i.total) FROM Invoice i " +
                    "          WHERE i.staff.staffId = s.staffId AND i.status = true " +
                    "          AND YEAR(i.invoiceDate) = YEAR(CURRENT_DATE) " +
                    "          AND MONTH(i.invoiceDate) = MONTH(CURRENT_DATE)), 0), " +
                    "(COALESCE(sa.baseSalary, 0) + 0.1 * COALESCE(" +
                    "    (SELECT SUM(i.total) FROM Invoice i " +
                    "     WHERE i.staff.staffId = s.staffId AND i.status = true " +
                    "     AND YEAR(i.invoiceDate) = YEAR(CURRENT_DATE) " +
                    "     AND MONTH(i.invoiceDate) = MONTH(CURRENT_DATE)), 0)) " +
                    "FROM Staff s LEFT JOIN s.salary sa " +
                    "ORDER BY s.staffId", Object[].class)
                    .list();
        }
    }

    public Double getTotalByStaffId(UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT (COALESCE(sa.baseSalary, 0) + 0.1 * COALESCE(" +
                    "    (SELECT SUM(i.total) FROM Invoice i " +
                    "     WHERE i.staff.staffId = sa.staffId AND i.status = true " +
                    "     AND YEAR(i.invoiceDate) = YEAR(CURRENT_DATE) " +
                    "     AND MONTH(i.invoiceDate) = MONTH(CURRENT_DATE)), 0)) " +
                    "FROM Salary sa WHERE sa.staffId = :staffId", Double.class)
                    .setParameter("staffId", staffId)
                    .uniqueResult();
        }
    }
}
