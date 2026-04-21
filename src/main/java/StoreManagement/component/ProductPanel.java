package StoreManagement.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ProductPanel extends JPanel {

//    private ManageFrame frame;
    private JPanel gridPanel;
    private JPanel cartPanel; 
    private JLabel lblTotalMoney; 

    private JTextField txtSearch;
    
    private Map<Integer, CartItemPanel> cartMap = new HashMap<>();

    private final String BANK_ID = "970422";       
    private final String ACCOUNT_NO = "33669917012007"; 
    private final String TEMPLATE = "nmq";     
    private final String ACCOUNT_NAME = "NGO MINH QUAN"; 

    private final Color PRIMARY_COLOR = new Color(32, 178, 170); 
    private final Color PRICE_COLOR = new Color(220, 20, 60);    

    public ProductPanel() {
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(new Color(245, 245, 245)); 
        this.setBorder(new EmptyBorder(15, 15, 15, 15)); 

      JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(new Color(245, 245, 245));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitleCenter = new JLabel("Danh sách thực đơn");
        lblTitleCenter.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(lblTitleCenter, BorderLayout.WEST);

        // Khối tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnSearch = new JButton("Tìm");
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        
        searchPanel.add(new JLabel("Món: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        centerContainer.add(headerPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 15, 15)); 
        gridPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        this.add(centerContainer, BorderLayout.CENTER);


        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(340, 0)); 
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // Giỏ hàng
        JLabel lblTitleBill = new JLabel("Giỏ hàng", SwingConstants.CENTER);
        lblTitleBill.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitleBill.setForeground(PRIMARY_COLOR);
        lblTitleBill.setBorder(new EmptyBorder(10, 0, 10, 0));
        rightPanel.add(lblTitleBill, BorderLayout.NORTH);

        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS)); 
        cartPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollCart = new JScrollPane(cartPanel);
        scrollCart.setBorder(null);
        rightPanel.add(scrollCart, BorderLayout.CENTER);

        JPanel bottomBill = new JPanel(new GridLayout(2, 1, 10, 10));
        bottomBill.setBackground(Color.WHITE);
        bottomBill.setBorder(new EmptyBorder(10, 10, 10, 10));

        lblTotalMoney = new JLabel("Tổng: 0 đ", SwingConstants.RIGHT);
        lblTotalMoney.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalMoney.setForeground(PRICE_COLOR);
        bottomBill.add(lblTotalMoney);

        JButton btnCheckout = new JButton("Thanh toán");
        btnCheckout.setBackground(PRIMARY_COLOR); 
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCheckout.setFocusPainted(false);
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.setPreferredSize(new Dimension(0, 50));
        bottomBill.add(btnCheckout);
        
        rightPanel.add(bottomBill, BorderLayout.SOUTH);
        this.add(rightPanel, BorderLayout.EAST);


        btnSearch.addActionListener(e -> loadProductsFromDB(txtSearch.getText().trim()));
        txtSearch.addActionListener(e -> loadProductsFromDB(txtSearch.getText().trim()));

        loadProductsFromDB(""); 
        
        btnCheckout.addActionListener(e -> processCheckout());
    }

    private void loadProductsFromDB(String keyword) {
        gridPanel.removeAll(); 
//        DATABBASE db = new DATABBASE();
        try (Connection conn = null) {
            if (conn == null) return;
            
            String sql = "SELECT * FROM Products WHERE product_name LIKE ?"; 
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setString(1, "%" + keyword + "%");
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    gridPanel.add(new ProductCard(
//                        rs.getInt("product_id"),
//                        rs.getString("product_name"),
//                        rs.getDouble("price")
//                    ));
//                }
//            }
            gridPanel.revalidate(); 
            gridPanel.repaint();
        } catch (Exception e) { e.printStackTrace(); }
    }


    private ImageIcon getImageByProductName(String name) {
        String imagePath = "/StoreManagement/assets/default.png";

        if (name.contains("Cafe Đen")) {
            imagePath = "/StoreManagement/assets/img/cafe_den.png";
        } 
        else if (name.contains("Cafe Sữa")) {
            imagePath = "/StoreManagement/assets/img/cafe_sua.png";
        }
        else if (name.contains("Bạc Xỉu")) {
            imagePath = "/StoreManagement/assets/img/bac_xiu.png";
        }
        else if (name.contains("Trà Đào")) {
            imagePath = "/StoreManagement/assets/img/tra_dao.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Chỉnh kích thước ảnh tại đây (100x80)
        return new ImageIcon(img);

    }


    class ProductCard extends JPanel {
        private int id;
        private String name;
        private double price;

        public ProductCard(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;

            this.setLayout(new BorderLayout());
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(160, 180));
            this.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
            this.setBorder(new LineBorder(new Color(220, 220, 220), 1));


            JLabel lblIcon = new JLabel("", SwingConstants.CENTER);
            
            ImageIcon icon = getImageByProductName(name); 
            lblIcon.setIcon(icon);
            lblIcon.setBorder(new EmptyBorder(10, 0, 5, 0));
            this.add(lblIcon, BorderLayout.CENTER);


            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(new Color(250, 250, 250)); 
            infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            JLabel lblName = new JLabel(name, SwingConstants.CENTER);
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
            
            JLabel lblPrice = new JLabel(formatMoney(price), SwingConstants.CENTER);
            lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblPrice.setForeground(PRICE_COLOR);

            infoPanel.add(lblName);
            infoPanel.add(lblPrice);
            this.add(infoPanel, BorderLayout.SOUTH);

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addToCart(id, name, price);
                }
            });
        }
    }

    
    private void addToCart(int id, String name, double price) {
        if (cartMap.containsKey(id)) {
            CartItemPanel item = cartMap.get(id);
            item.increaseQuantity();
        } else {
            CartItemPanel item = new CartItemPanel(id, name, price);
            cartMap.put(id, item);
            cartPanel.add(item);
            cartPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            cartPanel.revalidate(); cartPanel.repaint();
        }
        updateTotalMoney();
    }

    private void updateTotalMoney() {
        double total = 0;
        for (CartItemPanel item : cartMap.values()) total += item.getTotalPrice();
        lblTotalMoney.setText("Tổng: " + formatMoney(total));
    }

    private String formatMoney(double money) {
        DecimalFormat df = new DecimalFormat("###,### đ");
        return df.format(money);
    }

    private void processCheckout() {
        if (cartMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        double totalAmount = 0;
        for (CartItemPanel item : cartMap.values()) totalAmount += item.getTotalPrice();
        

//        DATABBASE db = new DATABBASE();
        try (Connection conn = null) {
            if (conn != null) {
                conn.setAutoCommit(false);
                String userSql = "SELECT user_id FROM users WHERE username = ?";
                int userId = 1; 
//                try(PreparedStatement psUser = conn.prepareStatement(userSql)){
//                     psUser.setString(1, frame.getLoggedInUser());
//                     ResultSet rs = psUser.executeQuery();
//                     if(rs.next()) userId = rs.getInt(1);
//                }
//                String orderSql = "INSERT INTO Orders (user_id, total_amount) VALUES (?, ?)";
//                PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
//                psOrder.setInt(1, userId);
//                psOrder.setDouble(2, totalAmount);
//                psOrder.executeUpdate();
//                int orderId = 0;
//                ResultSet rsKeys = psOrder.getGeneratedKeys();
//                if (rsKeys.next())
//                    orderId = rsKeys.getInt(1);
//
//                String detailSql = "INSERT INTO OrderDetails (order_id, product_id, quantity) VALUES (?, ?, ?)";
//                PreparedStatement psDetail = conn.prepareStatement(detailSql);
//                for (CartItemPanel item : cartMap.values()) {
//                    psDetail.setInt(1, orderId);
//                    psDetail.setInt(2, item.getProductId());
//                    psDetail.setInt(3, item.getQuantity());
//                    psDetail.addBatch();
//                }
//                psDetail.executeBatch();
                conn.commit();
//                openVietQRLink((int)totalAmount, "DonHang" + orderId);
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        cartMap.clear();
        cartPanel.removeAll();
        cartPanel.repaint();
        updateTotalMoney();
    }

    private void openVietQRLink(int amount, String addInfo) {
        try {
            String encodedName = URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String encodedInfo = URLEncoder.encode(addInfo, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String urlString = String.format(
                "https://api.vietqr.io/image/%s-%s-%s.jpg?accountName=%s&amount=%d&addInfo=%s",
                BANK_ID, ACCOUNT_NO, TEMPLATE, encodedName, amount, encodedInfo
            );
            Desktop.getDesktop().browse(new URI(urlString));

        } catch (Exception e) { e.printStackTrace(); }
    }

    class CartItemPanel extends JPanel {
        private int productId;
        private int quantity = 1;
        private double price;
        private JLabel lblQty;
        public CartItemPanel(int id, String name, double price) {
            this.productId = id; 
            this.price = price;
            this.setLayout(new BorderLayout()); 
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)), new EmptyBorder(8, 5, 8, 5)));
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
            JLabel lblName = new JLabel(name); 
            lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            this.add(lblName, BorderLayout.CENTER);

            JPanel ctrlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            ctrlPanel.setBackground(Color.WHITE);

            JButton btnMinus = new JButton("-"); 
            btnMinus.setBackground(new Color(240,240,240)); 
            btnMinus.setBorder(null); 
            btnMinus.setPreferredSize(new Dimension(24,24));

            lblQty = new JLabel(" 1 "); 
            lblQty.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JButton btnPlus = new JButton("+"); 
            btnPlus.setBackground(new Color(240,240,240)); 
            btnPlus.setBorder(null); 
            btnPlus.setPreferredSize(new Dimension(24,24));

            ctrlPanel.add(btnMinus); 
            ctrlPanel.add(lblQty); 
            ctrlPanel.add(btnPlus);

            this.add(ctrlPanel, BorderLayout.EAST);

            btnPlus.addActionListener(e -> increaseQuantity());
            btnMinus.addActionListener(e -> decreaseQuantity());
        }
        public void increaseQuantity() { 
            quantity++; 
            lblQty.setText(" " + quantity + " "); 
            updateTotalMoney(); 
        }

        public void decreaseQuantity() {
            if (quantity > 1) { 
                quantity--; 
                lblQty.setText(" " + quantity + " "); 
                updateTotalMoney(); 
            }
            else { 
                cartMap.remove(productId); 
                cartPanel.remove(this); 
                cartPanel.revalidate(); 
                cartPanel.repaint(); 
                updateTotalMoney(); 
            }
        }
        
        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return price * quantity; }
    }

    static void main() {
//        JFrame n = new JFrame();
//        n.add(new ProductPanel(n));
//        n.setVisible(true);
    }
}