package hanabi.dao;

import hanabi.model.Average;
import hanabi.util.HibernateUtil;
import java.util.UUID;
import org.hibernate.Session;

public class AverageDAO extends BaseDAO<Average, UUID> {

    public AverageDAO() {
        super(Average.class);
    }

    public Double getAverageScore() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery(
                    "SELECT COALESCE(AVG(a.averageScore), 0) FROM Average a", Double.class)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }
}
