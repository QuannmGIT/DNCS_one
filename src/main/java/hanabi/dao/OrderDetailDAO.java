package hanabi.dao;

import hanabi.model.OrderDetail;
import hanabi.util.HibernateUtil;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class OrderDetailDAO extends BaseDAO<OrderDetail, OrderDetail.OrderDetailId> {

    public OrderDetailDAO() {
        super(OrderDetail.class);
    }

    public List<OrderDetail> findByOrderId(UUID orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM OrderDetail WHERE orderId = :oid", OrderDetail.class)
                    .setParameter("oid", orderId)
                    .list();
        }
    }

    public List<Object[]> findTopSelling(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT od.product.productName, SUM(od.quantity) " +
                    "FROM OrderDetail od GROUP BY od.product.productName " +
                    "ORDER BY SUM(od.quantity) DESC", Object[].class)
                    .setMaxResults(limit)
                    .list();
        }
    }
}
