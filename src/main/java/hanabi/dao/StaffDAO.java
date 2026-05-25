package hanabi.dao;

import hanabi.model.Staff;
import hanabi.util.HibernateUtil;
import hanabi.util.PasswordUtil;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.hibernate.Session;

public class StaffDAO extends BaseDAO<Staff, java.util.UUID> {

    public StaffDAO() {
        super(Staff.class);
    }

    public Optional<Staff> authenticate(String staffName, String password) {
        Optional<Staff> result = findByStaffName(staffName);
        if (result.isEmpty()) return Optional.empty();
        Staff staff = result.get();
        String stored = staff.getPassword();
        if (stored == null) return Optional.empty();
        if (stored.contains(":")) {
            String[] parts = stored.split(":", 2);
            if (PasswordUtil.verify(password, parts[0], parts[1])) {
                return Optional.of(staff);
            }
        } else if (stored.equals(password)) {
            String salt = PasswordUtil.generateSalt();
            staff.setPassword(salt + ":" + PasswordUtil.hash(password, salt));
            update(staff);
            return Optional.of(staff);
        }
        return Optional.empty();
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
