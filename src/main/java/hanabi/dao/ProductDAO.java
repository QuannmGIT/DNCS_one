package hanabi.dao;

import hanabi.model.Product;
import hanabi.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
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

    public Optional<Product> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product product = session.createQuery(
                    "FROM Product WHERE productName = :name", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.ofNullable(product);
        } catch (NoResultException e) {
            return Optional.empty();
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
