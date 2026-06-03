package hanabi.service;

import hanabi.dao.StaffDAO;
import hanabi.dao.TenantDAO;
import hanabi.dao.UserDAO;
import hanabi.model.Staff;
import hanabi.model.Tenant;
import hanabi.model.User;
import hanabi.util.TenantContext;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private static final String DEV_USERNAME = "dev";
    private static final String DEV_PASSWORD = "dev123";

    private final TenantDAO tenantDAO = new TenantDAO();
    private final StaffDAO staffDAO = new StaffDAO();
    private final UserDAO userDAO = new UserDAO();
    private User currentUser;
    private Tenant currentTenant;

    public Optional<User> login(String tenantName, String username, String password) {
        Optional<Tenant> tenantOpt = tenantDAO.findByTenantName(tenantName);
        if (tenantOpt.isEmpty()) return Optional.empty();
        Tenant tenant = tenantOpt.get();
        if (tenant.getStatus() == null || !tenant.getStatus()) return Optional.empty();

        TenantContext.setCurrentTenantId(tenant.getTenantId());
        currentTenant = tenant;

        if (tenant.getTenantName().equals(username)) {
            Optional<Tenant> authResult = tenantDAO.authenticate(tenantName, password);
            if (authResult.isPresent()) {
                User user = new User(authResult.get());
                this.currentUser = user;
                return Optional.of(user);
            }
        }

        Optional<User> userResult = userDAO.login(username, password);
        if (userResult.isPresent()) {
            this.currentUser = userResult.get();
            return userResult;
        }

        TenantContext.clear();
        currentTenant = null;
        return Optional.empty();
    }

    public Optional<User> devLogin() {
        User devUser = new User();
        devUser.setStaffId(UUID.nameUUIDFromBytes("dev".getBytes()));
        devUser.setStaffName("dev");
        devUser.setFullName("Developer");
        devUser.setRole("dev");
        devUser.setStatus(true);
        this.currentUser = devUser;
        this.currentTenant = null;
        TenantContext.clear();
        return Optional.of(devUser);
    }

    public boolean isDevUser() {
        return currentUser != null && "dev".equals(currentUser.getRole());
    }

    public void logout() {
        this.currentUser = null;
        this.currentTenant = null;
        TenantContext.clear();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Tenant getCurrentTenant() {
        return currentTenant;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public Staff getCurrentStaff() {
        if (currentUser == null) return null;
        return staffDAO.findById(currentUser.getStaffId());
    }
}
