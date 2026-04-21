package StoreManagement.Product;

import StoreManagement.model.Product;
import StoreManagement.component.ProductDisplayComponent;

import javax.swing.*;
import java.awt.*;

public class Latte extends JPanel {
    private Product latteProduct;
    private ProductDisplayComponent displayComponent;

    public Latte() {
        initializeProduct();
        initializeUI();
    }

    private void initializeProduct() {
        latteProduct = new Product(
            "LATTE",
            65000.0,
            "Ly cà phê sữa ngọt ngào đến khó quên! Với một chút nhẹ nhàng hơn so với Cappuccino, " +
            "Latte của chúng tôi bắt đầu với cà phê espresso, sau đó thêm sữa tươi và bọt sữa một cách đầy nghệ thuật. " +
            "Bạn có thể tùy thích lựa chọn uống nóng hoặc dùng chung với đá.",
                "/StoreManagement/assets/Latte.png"
        );
        latteProduct.setCategory("Coffee");
        latteProduct.addTaste("Classic");
        latteProduct.addTaste("Vanilla");
        latteProduct.addTaste("Caramel");
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        displayComponent = new ProductDisplayComponent(latteProduct);
        add(displayComponent, BorderLayout.CENTER);
    }

    public Product getProduct() {
        return latteProduct;
    }

    public ProductDisplayComponent getDisplayComponent() {
        return displayComponent;
    }

    public void refreshDisplay() {
        if (displayComponent != null) {
            remove(displayComponent);
            displayComponent = new ProductDisplayComponent(latteProduct);
            add(displayComponent, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }
}