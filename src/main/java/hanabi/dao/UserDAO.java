package hanabi.dao;

import hanabi.model.Staff;
import hanabi.model.User;
import hanabi.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import java.util.Optional;
import org.hibernate.Session;

public class UserDAO {

    private final StaffDAO staffDAO = new StaffDAO();

    public Optional<User> login(String staffName, String password) {
        Optional<Staff> result = staffDAO.authenticate(staffName, password);
        return result.map(staff -> {
            if (staff.getStatus() == null || !staff.getStatus()) {
                return null;
            }
            return new User(staff);
        });
    }

    public User findById(java.util.UUID staffId) {
        Staff staff = staffDAO.findById(staffId);
        return staff == null ? null : new User(staff);
    }
}
