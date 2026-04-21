package StoreManagement.model;

import StoreManagement.component.ProductDisplayComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProductModelComponent extends JPanel {
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 250;
    
    private Product product;
    private ProductDisplayComponent displayComponent;
    private boolean showTitle;
    private Color titleColor;
    private Font titleFont;

    public ProductModelComponent(Product product) {
        this(product, true, new Color(179, 40, 45));
    }

    public ProductModelComponent(Product product, boolean showTitle, Color titleColor) {
        this.product = product;
        this.showTitle = showTitle;
        this.titleColor = titleColor;
        this.titleFont = new Font("Segoe UI", Font.BOLD, 36);
        
        initializeUI();
    }

    public ProductModelComponent(String imagePath, String productName, String price, String description) {
        this(new Product(productName, parsePrice(price), description, imagePath));
    }

    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        displayComponent = new ProductDisplayComponent(product);
        add(displayComponent, BorderLayout.CENTER);
        
        if (showTitle) {
            add(createTitlePanel(), BorderLayout.NORTH);
        }
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(product.getName());
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(titleColor);
        
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private static double parsePrice(String priceString) {
        try {
            String cleanedPrice = priceString.replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleanedPrice);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        refreshDisplay();
    }

    public ProductDisplayComponent getDisplayComponent() {
        return displayComponent;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        refreshDisplay();
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
        refreshDisplay();
    }

    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        refreshDisplay();
    }

    public void refreshDisplay() {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }

    public void setCartActionListener(ActionListener listener) {
        if (displayComponent != null) {
            displayComponent.setCartActionListener(listener);
        }
    }
}