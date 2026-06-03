package hanabi.model;

import java.util.UUID;

public class Product {
    private UUID productId;
    private String productName;
    private String category;
    private Double price;
    private Double cost;
    private String image;
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
