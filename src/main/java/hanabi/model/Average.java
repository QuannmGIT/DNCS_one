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
@Table(name = "average")
public class Average {
    @Id
    @Column(name = "staff_id", columnDefinition = "BINARY(16)")
    private UUID staffId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", columnDefinition = "BINARY(16)")
    private Staff staff;

    @Column(name = "average_score", columnDefinition = "int")
    private Integer averageScore;

    public Average() {}

    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public Integer getAverageScore() { return averageScore; }
    public void setAverageScore(Integer averageScore) { this.averageScore = averageScore; }
}
