package StoreManagement.model;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private int amount;
    private double price;
    private String name;
    private String description;
    private String imagePath;
    private String category;
    private List<String> addMoreTaste;
    private boolean available;

    public Product() {
        this.id = 0;
        this.amount = 0;
        this.price = 0.0;
        this.name = "";
        this.description = "";
        this.imagePath = "";
        this.category = "";
        this.addMoreTaste = new ArrayList<>();
        this.available = true;
    }

    public Product(int id, int amount, double price, String name, String description, 
                   String imagePath, String category, List<String> addMoreTaste) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.category = category;
        this.addMoreTaste = addMoreTaste != null ? addMoreTaste : new ArrayList<>();
        this.available = true;
    }

    public Product(String name, double price, String description, String imagePath) {
        this(0, 0, price, name, description, imagePath, "Coffee", new ArrayList<>());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getAddMoreTaste() {
        return addMoreTaste;
    }

    public void setAddMoreTaste(List<String> addMoreTaste) {
        this.addMoreTaste = addMoreTaste;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getFormattedPrice() {
        return String.format("%,.0f VNĐ", price);
    }

    public void addTaste(String taste) {
        if (addMoreTaste == null) {
            addMoreTaste = new ArrayList<>();
        }
        addMoreTaste.add(taste);
    }

    public void removeTaste(String taste) {
        if (addMoreTaste != null) {
            addMoreTaste.remove(taste);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", available=" + available +
                '}';
    }
}
