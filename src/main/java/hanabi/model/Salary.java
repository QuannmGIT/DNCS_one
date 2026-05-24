package hanabi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "salarys")
public class Salary {
    @Id
    @Column(name = "staff_id", columnDefinition = "BINARY(16)")
    private UUID staffId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", columnDefinition = "BINARY(16)")
    private Staff staff;

    @Column(columnDefinition = "decimal")
    private Double baseSalary;

    @Column(columnDefinition = "decimal")
    private Double commissionRate;

    public Salary() {}

    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }
    public Double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(Double commissionRate) { this.commissionRate = commissionRate; }
}
