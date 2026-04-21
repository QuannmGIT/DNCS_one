package StoreManagement.DB;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", columnDefinition = "binary(16)")
    private UUID orderId;

    @Column(name = "invoice_id", columnDefinition = "binary(16)", unique = true, nullable = false)
    private UUID invoiceId;

    @Column(name = "user_id", columnDefinition = "binary(16)", unique = true, nullable = false)
    private UUID userId;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "total")
    private Integer total;

    @OneToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    public Order() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", invoiceId=" + invoiceId +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", total=" + total +
                '}';
    }
}