package hanabi.model;

import java.time.LocalDate;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;

public class Invoice {
    @BsonId
    private UUID invoiceId;
    private UUID staffId;
    private LocalDate invoiceDate;
    private Integer total;
    private Boolean status;

    public Invoice() {}

    public UUID getInvoiceId() { return invoiceId; }
    public void setInvoiceId(UUID invoiceId) { this.invoiceId = invoiceId; }
    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}