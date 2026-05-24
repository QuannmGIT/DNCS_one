package hanabi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders_details")
@IdClass(OrderDetail.OrderDetailId.class)
public class OrderDetail {

    public static class OrderDetailId implements Serializable {
        private UUID orderId;
        private UUID productId;

        public OrderDetailId() {}

        public OrderDetailId(UUID orderId, UUID productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        public UUID getOrderId() { return orderId; }
        public void setOrderId(UUID orderId) { this.orderId = orderId; }
        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderDetailId that)) return false;
            return Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId);
        }

        @Override
        public int hashCode() { return Objects.hash(orderId, productId); }
    }

    @Id
    @Column(name = "order_id", columnDefinition = "BINARY(16)")
    private UUID orderId;

    @Id
    @Column(name = "product_id", columnDefinition = "BINARY(16)")
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false, columnDefinition = "BINARY(16)")
    private Product product;

    @Column(columnDefinition = "int")
    private Integer quantity;

    public OrderDetail() {}

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
