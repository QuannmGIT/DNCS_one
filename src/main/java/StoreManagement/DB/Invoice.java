package StoreManagement.DB;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @Column(name = "invoice_id", columnDefinition = "binary(16)")
    private UUID invoiceId;

    @Column(name = "staff_id", columnDefinition = "binary(16)", unique = true, nullable = false)
    private UUID staffId;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "total")
    private Integer total;

    @Column(name = "status")
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id", insertable = false, updatable = false)
    private Staff staff;

    public Invoice() {
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public boolean isPaid() {
        return status != null && status;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", staffId=" + staffId +
                ", invoiceDate=" + invoiceDate +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}