package hanabi.dao;

import hanabi.model.Order;
import hanabi.util.HibernateUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class OrderDAO extends BaseDAO<Order, UUID> {

    public OrderDAO() {
        super(Order.class);
    }

    public List<Order> findByStaffId(UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Order WHERE staff.staffId = :sid ORDER BY orderDate DESC", Order.class)
                    .setParameter("sid", staffId)
                    .list();
        }
    }

    public List<Order> findByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Order WHERE orderDate = :d ORDER BY orderDate DESC", Order.class)
                    .setParameter("d", date)
                    .list();
        }
    }

    public List<Order> findRecent(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Order ORDER BY orderDate DESC, orderId DESC", Order.class)
                    .setMaxResults(limit)
                    .list();
        }
    }

    public long countByStaffId(UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(o) FROM Order o WHERE o.staff.staffId = :sid", Long.class)
                    .setParameter("sid", staffId)
                    .getSingleResult();
        }
    }

    public long countAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(o) FROM Order o", Long.class)
                    .getSingleResult();
        }
    }

    public long countToday() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(o) FROM Order o WHERE o.orderDate = :today", Long.class)
                    .setParameter("today", LocalDate.now())
                    .getSingleResult();
        }
    }
}
