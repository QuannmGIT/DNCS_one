package StoreManagement.DB;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "salarys")
public class Salary {

    @Id
    @Column(name = "staff_id", columnDefinition = "binary(16)")
    private UUID staffId;

    @Column(name = "baseSalary", precision = 19, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "CommissionRate", precision = 19, scale = 2)
    private BigDecimal commissionRate;

    @OneToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    private Staff staff;

    public Salary() {
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "staffId=" + staffId +
                ", baseSalary=" + baseSalary +
                ", commissionRate=" + commissionRate +
                '}';
    }
}