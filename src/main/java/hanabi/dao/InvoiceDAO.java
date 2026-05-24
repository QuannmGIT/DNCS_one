package hanabi.dao;

import hanabi.model.Invoice;
import hanabi.util.HibernateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class InvoiceDAO extends BaseDAO<Invoice, UUID> {

    public InvoiceDAO() {
        super(Invoice.class);
    }

    public List<Invoice> findByStaffId(UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Invoice WHERE staff.staffId = :sid ORDER BY invoiceDate DESC", Invoice.class)
                    .setParameter("sid", staffId)
                    .list();
        }
    }

    public List<Invoice> findByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Invoice WHERE invoiceDate = :d", Invoice.class)
                    .setParameter("d", date)
                    .list();
        }
    }

    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Invoice WHERE invoiceDate BETWEEN :start AND :end ORDER BY invoiceDate", Invoice.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .list();
        }
    }

    public long totalRevenueToday() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long result = session.createQuery(
                    "SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.invoiceDate = :today AND i.status = true", Long.class)
                    .setParameter("today", LocalDate.now())
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    public long totalRevenueByDateRange(LocalDate start, LocalDate end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long result = session.createQuery(
                    "SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.invoiceDate BETWEEN :start AND :end AND i.status = true", Long.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }
}
