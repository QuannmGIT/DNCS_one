package hanabi.service;

import hanabi.dao.StaffDAO;
import hanabi.dao.UserDAO;
import hanabi.model.Staff;
import hanabi.model.User;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final StaffDAO staffDAO = new StaffDAO();
    private User currentUser;

    public Optional<User> login(String username, String password) {
        Optional<User> user = userDAO.login(username, password);
        user.ifPresent(u -> this.currentUser = u);
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Staff getCurrentStaff() {
        if (currentUser == null) return null;
        return staffDAO.findById(currentUser.getStaffId());
    }
}
