package StoreManagement.component;

import StoreManagement.model.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDisplayComponent extends JPanel {
    private Product product;
    private JButton addToCartButton;
    private ActionListener cartActionListener;

    public ProductDisplayComponent(Product product) {
        this.product = product;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(product.getName());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(101, 67, 33));
        
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(createImagePanel());
        contentPanel.add(createInfoPanel());
        
        return contentPanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loadImage(imageLabel);
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private void loadImage(JLabel imageLabel) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(product.getImagePath()));
            Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel.setText("[Image not found: " + product.getImagePath() + "]");
            imageLabel.setForeground(Color.RED);
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        }
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        infoPanel.add(createDescriptionArea());
        infoPanel.add(Box.createVerticalStrut(30));
        infoPanel.add(createPriceLabel());
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(createAddToCartButton());
        infoPanel.add(Box.createVerticalGlue());

        return infoPanel;
    }

    private JTextArea createDescriptionArea() {
        JTextArea descriptionArea = new JTextArea(product.getDescription());
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionArea.setForeground(new Color(80, 80, 80));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        return descriptionArea;
    }

    private JLabel createPriceLabel() {
        JLabel priceLabel = new JLabel("Giá : " + product.getFormattedPrice());
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        return priceLabel;
    }

    private JButton createAddToCartButton() {
        addToCartButton = new JButton("Thêm vào giỏ");
        addToCartButton.setBackground(new Color(179, 40, 45));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addToCartButton.setFocusPainted(false);
        addToCartButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addToCartButton.setMaximumSize(new Dimension(150, 40));

        addToCartButton.addActionListener(e -> {
            if (cartActionListener != null) {
                cartActionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ADD_TO_CART"));
            }
        });

        return addToCartButton;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        refreshDisplay();
    }

    public void setCartActionListener(ActionListener listener) {
        this.cartActionListener = listener;
    }

    public void refreshDisplay() {
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }
}
