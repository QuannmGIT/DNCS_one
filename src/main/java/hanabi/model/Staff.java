package hanabi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @Column(name = "staff_id", columnDefinition = "BINARY(16)")
    private UUID staffId;

    @Column(name = "staff_name", nullable = false, unique = true, length = 50)
    private String staffName;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @OneToOne(mappedBy = "staff", fetch = FetchType.LAZY)
    private Salary salary;

    @Column(columnDefinition = "enum('admin','staff') default 'staff'")
    private String role;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean status;

    public Staff() {}

    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Salary getSalary() { return salary; }
    public void setSalary(Salary salary) { this.salary = salary; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
