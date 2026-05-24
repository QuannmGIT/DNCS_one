package hanabi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @Column(name = "invoice_id", columnDefinition = "BINARY(16)")
    private UUID invoiceId;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", nullable = false, columnDefinition = "BINARY(16)")
    private Staff staff;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(columnDefinition = "int")
    private Integer total;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean status;

    public Invoice() {}

    public UUID getInvoiceId() { return invoiceId; }
    public void setInvoiceId(UUID invoiceId) { this.invoiceId = invoiceId; }
    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }
    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
