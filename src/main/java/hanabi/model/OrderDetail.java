package hanabi.model;

import java.util.Objects;
import java.util.UUID;

public class OrderDetail {

    public static class OrderDetailId {
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

    private UUID orderId;
    private UUID productId;
    private Integer quantity;

    public OrderDetail() {}

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
