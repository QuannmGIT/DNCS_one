package StoreManagement.model;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductModelComponent extends JPanel {
    private static final int WIDTH = 250;
    private static final int HEIGHT = 250;
    private String imagePath;
    private String productName;
    private String price;
    private String description;
    

    public ProductModelComponent(String imagePath, String productName, String price, String description) {
        this.imagePath = imagePath;
        this.productName = productName;
        this.price = price;
        this.description = description;
        init();
    }

    public void init(){
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setVisible(true);
        initUI();
    }

    public void initUI() {
        title("Segoe UI", new Color(179, 40, 45));
        content();
    }

    public void title(String FontName, Color foreground){
        JLabel lblTitle = new JLabel(productName);
        lblTitle.setFont(new Font(FontName, Font.BOLD, 36));
        lblTitle.setForeground(foreground); // Màu nâu cà phê
        add(lblTitle, BorderLayout.NORTH);
    }

    public void content(){
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(imagePane(imagePath));
        contentPanel.add(infoPane());
        add(contentPanel, BorderLayout.CENTER);
    }

    public JComponent imagePane(String imagePath) {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1)); // Viền mỏng quanh ảnh

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);

        // Load img (safe process if image not found on database)
        byte[] imageData = new imageModel(imagePath, WIDTH, HEIGHT).getImageData();
//        ImageIcon originalIcon = new imageModel(imagePath, "png" ,WIDTH, HEIGHT).loadImageFromDatabase(imageData);
        ImageIcon originalIcon = new imageModel(imagePath, WIDTH, HEIGHT).loadImageFromPath();

        if (originalIcon == null) {
            originalIcon = new imageModel(imagePath, WIDTH, HEIGHT).loadImageFromPath();
        }
       
        try {
            // safety check if image not found
            Image img = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImage.setText("[missing image path or image not found on database]");
            lblImage.setForeground(Color.RED);
        }
        imagePanel.add(lblImage, BorderLayout.CENTER);
        return imagePanel;
    }

    public JComponent infoPane(){
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Description
        JTextArea txtDesc = new JTextArea(description);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDesc.setForeground(new Color(80, 80, 80)); // Màu xám đậm
        txtDesc.setLineWrap(true);       // auto wrap
        txtDesc.setWrapStyleWord(true);  // wrap by word
        txtDesc.setEditable(false);      // cannot editable
        txtDesc.setFocusable(false);     // cannot mouse focus
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price
        JLabel lblPrice = new JLabel(price);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPrice.setForeground(Color.BLACK);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        // add components to right column
        infoPanel.add(txtDesc);
        infoPanel.add(Box.createVerticalStrut(30)); // Khoảng cách
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(20)); // Khoảng cách
        infoPanel.add(buttonPane());

        // push content to top
        infoPanel.add(Box.createVerticalGlue());
        return infoPanel;
    }

    public JComponent buttonPane(){
        // order button
        JButton btnOrder = new JButton("add purchase");
        btnOrder.setBackground(new Color(179, 40, 45)); // Màu đỏ Highlands
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrder.setFocusPainted(false);
        btnOrder.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOrder.setMaximumSize(new Dimension(150, 40));

        return btnOrder;
    }


















    //
//    // pretest panel
//    static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setLayout(new BorderLayout());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 600);
//        frame.add(new ProductModelComponent("src/main/resources/assets/img/cafe_den.png", "test", "xsv", "sfbsh"));
//        frame.setVisible(true);
//    }
}