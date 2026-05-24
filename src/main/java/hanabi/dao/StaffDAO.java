package hanabi.dao;

import hanabi.model.Staff;
import hanabi.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.hibernate.Session;

public class StaffDAO extends BaseDAO<Staff, java.util.UUID> {

    public StaffDAO() {
        super(Staff.class);
    }

    public Optional<Staff> authenticate(String staffName, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Staff staff = session.createQuery(
                    "FROM Staff WHERE staffName = :name AND password = :pass", Staff.class)
                    .setParameter("name", staffName)
                    .setParameter("pass", password)
                    .getSingleResult();
            return Optional.ofNullable(staff);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Staff> findByStaffName(String staffName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Staff staff = session.createQuery(
                    "FROM Staff WHERE staffName = :name", Staff.class)
                    .setParameter("name", staffName)
                    .getSingleResult();
            return Optional.ofNullable(staff);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
