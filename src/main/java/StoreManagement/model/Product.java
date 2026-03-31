package StoreManagement.model;

import javax.swing.*;
import java.util.ArrayList;

public class Product {
    private int amount;
    private double price;
    private String name;
    private String description;
    private ImageIcon image;
    private String category;
    private ArrayList<Object> add_more_taste;

    public Product() {
        amount = 0;
        price = 0.0;
        name = "";
        description = "";
        image = null;
        category = "";
        add_more_taste = new ArrayList<>();
    }

    public Product(int amount,
                   double price,
                   String name,
                   String description,
                   ImageIcon image,
                   String category,
                   ArrayList<Object> add_more_taste
    ) {
        this.amount = amount;
        this.price = price;
        this.name = name;
        this.description = description;
        this.image = image;
        this.category = category;
        this.add_more_taste = add_more_taste;
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

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Object> getAdd_more_taste() {
        return add_more_taste;
    }

    public void setAdd_more_taste(ArrayList<Object> add_more_taste) {
        this.add_more_taste = add_more_taste;
    }
}