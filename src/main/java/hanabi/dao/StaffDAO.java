package hanabi.dao;

import hanabi.model.Staff;
import hanabi.util.PasswordUtil;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class StaffDAO extends BaseDAO<Staff> {

    @Override
    protected String tableName() { return "staff"; }

    @Override
    protected String idColumn() { return "staff_id"; }

    private final RowMapper<Staff> mapper = this::mapRow;

    @Override
    protected RowMapper<Staff> rowMapper() { return mapper; }

    private Staff mapRow(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setStaffId(UUID.fromString(rs.getString("staff_id")));
        s.setStaffName(rs.getString("staff_name"));
        s.setEmail(rs.getString("email"));
        s.setPassword(rs.getString("password"));
        s.setFullName(rs.getString("full_name"));
        s.setRole(rs.getString("role"));
        s.setStatus(rs.getBoolean("status"));
        if (rs.wasNull()) s.setStatus(null);
        return s;
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
        Staff s = SupabaseUtil.querySingle(
                "SELECT * FROM staff WHERE staff_name = ?",
                rowMapper(), staffName);
        return Optional.ofNullable(s);
    }

    public Optional<Staff> findAdmin() {
        Staff s = SupabaseUtil.querySingle(
                "SELECT * FROM staff WHERE role = ? AND status = true",
                rowMapper(), "admin");
        return Optional.ofNullable(s);
    }

    public void save(Staff staff) {
        SupabaseUtil.update(
                "INSERT INTO staff (staff_id, staff_name, email, password, full_name, role, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                staff.getStaffId(), staff.getStaffName(), staff.getEmail(),
                staff.getPassword(), staff.getFullName(), staff.getRole(), staff.getStatus());
    }

    public void update(Staff staff, UUID id) {
        SupabaseUtil.update(
                "UPDATE staff SET staff_name=?, email=?, password=?, full_name=?, role=?, status=? WHERE staff_id=?::uuid",
                staff.getStaffName(), staff.getEmail(), staff.getPassword(),
                staff.getFullName(), staff.getRole(), staff.getStatus(), id);
    }
}
