package hanabi.model;

import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;

public class Salary {
    @BsonId
    private UUID staffId;
    private Double baseSalary;
    private Double commissionRate;

    public Salary() {}

    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }
    public Double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(Double commissionRate) { this.commissionRate = commissionRate; }
}