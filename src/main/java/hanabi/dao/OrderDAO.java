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

    public List<Order> findFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId, int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM Order o WHERE 1=1");
            if (fromDate != null) hql.append(" AND o.orderDate >= :fromDate");
            if (toDate != null) hql.append(" AND o.orderDate <= :toDate");
            if (staffId != null) hql.append(" AND o.staff.staffId = :staffId");
            hql.append(" ORDER BY o.orderDate DESC, o.orderId DESC");

            var query = session.createQuery(hql.toString(), Order.class);
            if (fromDate != null) query.setParameter("fromDate", fromDate);
            if (toDate != null) query.setParameter("toDate", toDate);
            if (staffId != null) query.setParameter("staffId", staffId);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.list();
        }
    }

    public long countFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("SELECT COUNT(o) FROM Order o WHERE 1=1");
            if (fromDate != null) hql.append(" AND o.orderDate >= :fromDate");
            if (toDate != null) hql.append(" AND o.orderDate <= :toDate");
            if (staffId != null) hql.append(" AND o.staff.staffId = :staffId");

            var query = session.createQuery(hql.toString(), Long.class);
            if (fromDate != null) query.setParameter("fromDate", fromDate);
            if (toDate != null) query.setParameter("toDate", toDate);
            if (staffId != null) query.setParameter("staffId", staffId);
            return query.getSingleResult();
        }
    }
}
