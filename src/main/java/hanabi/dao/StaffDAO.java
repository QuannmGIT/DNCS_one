package hanabi.dao;

import com.mongodb.client.model.Filters;
import hanabi.model.Staff;
import hanabi.util.PasswordUtil;
import java.util.Optional;
import java.util.UUID;

public class StaffDAO extends BaseDAO<Staff> {

    public StaffDAO() {
        super(Staff.class, "staff");
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
            update(staff, staff.getStaffId());
            return Optional.of(staff);
        }
        return Optional.empty();
    }

    public Optional<Staff> findByStaffName(String staffName) {
        Staff staff = getCollection().find(Filters.eq("staffName", staffName)).first();
        return Optional.ofNullable(staff);
    }

    public Optional<Staff> findAdmin() {
        Staff admin = getCollection().find(
                Filters.and(Filters.eq("role", "admin"), Filters.eq("status", true))
        ).first();
        return Optional.ofNullable(admin);
    }
}