package hanabi.model;

import java.time.LocalDate;
import java.util.UUID;

public class Order {
    private UUID orderId;
    private UUID invoiceId;
    private UUID staffId;
    private Boolean status;
    private LocalDate orderDate;
    private Integer total;

    public Order() {}

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public UUID getInvoiceId() { return invoiceId; }
    public void setInvoiceId(UUID invoiceId) { this.invoiceId = invoiceId; }
    public UUID getStaffId() { return staffId; }
    public void setStaffId(UUID staffId) { this.staffId = staffId; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
