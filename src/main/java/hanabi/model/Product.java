package hanabi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id", columnDefinition = "BINARY(16)")
    private UUID productId;

    @Column(name = "product_name", nullable = false, unique = true, length = 50)
    private String productName;

    @Column(length = 100)
    private String category;

    @Column(columnDefinition = "decimal")
    private Double price;

    @Column(columnDefinition = "decimal")
    private Double cost;

    @Column(length = 255)
    private String image;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean status;

    public Product() {}

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
