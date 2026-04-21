package StoreManagement.DB;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "average")
public class Average {

    @Id
    @Column(name = "staff_id", columnDefinition = "binary(16)")
    private UUID staffId;

    @Column(name = "average_score")
    private Integer averageScore;

    @OneToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    private Staff staff;

    public Average() {
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "Average{" +
                "staffId=" + staffId +
                ", averageScore=" + averageScore +
                '}';
    }
}