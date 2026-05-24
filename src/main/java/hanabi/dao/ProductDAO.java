package hanabi.dao;

import hanabi.model.Product;
import hanabi.util.HibernateUtil;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class ProductDAO extends BaseDAO<Product, UUID> {

    public ProductDAO() {
        super(Product.class);
    }

    public List<Product> findByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Product WHERE category = :cat", Product.class)
                    .setParameter("cat", category)
                    .list();
        }
    }

    public List<Product> findAvailable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Product WHERE status = true", Product.class)
                    .list();
        }
    }
}
