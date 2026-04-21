package StoreManagement.DB;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @Column(name = "product_id", columnDefinition = "binary(16)")
    private UUID productId;

    @Column(name = "product_name", length = 50, unique = true, nullable = false)
    private String productName;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "cost", precision = 19, scale = 2)
    private BigDecimal cost;

    @Column(name = "status")
    private Boolean status = true;

    public ProductEntity() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status != null && status;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}