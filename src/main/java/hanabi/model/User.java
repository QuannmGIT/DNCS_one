package hanabi.model;

import java.util.UUID;

public class User {
    private UUID staffId;
    private String staffName;
    private String fullName;
    private String email;
    private String role;
    private Boolean status;

    public User() {}

    public User(Staff staff) {
        this.staffId = staff.getStaffId();
        this.staffName = staff.getStaffName();
        this.fullName = staff.getFullName();
        this.email = staff.getEmail();
        this.role = staff.getRole();
        this.status = staff.getStatus();
    }

    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public boolean isAdmin() { return "admin".equals(role); }
    public boolean isActive() { return status != null && status; }
}
