package StoreManagement.component;

import StoreManagement.App;
import StoreManagement.page.MainPage;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MenuPanel extends JPanel {
    private JButton btnSale; 
    private String currentUsername;

    public MenuPanel() {
        this.currentUsername = "";
        this.setPreferredSize(new Dimension(200, 600)); 
        this.setBackground(new Color(230, 230, 230)); 
        this.setLayout(null); 
        
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

        JLabel lblTitle = new JLabel("Danh mục");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(20, 30, 150, 40);
        this.add(lblTitle);

        // Mặt hàng
        JButton btnProducts = new JButton("Mặt hàng");
        btnProducts.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnProducts.setBackground(Color.WHITE);
        btnProducts.setBounds(10, 100, 180, 40);
        btnProducts.setFocusPainted(false);
        this.add(btnProducts);

        // Tài khoản
        JButton btnAccount = new JButton("Tài khoản");
        btnAccount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAccount.setBackground(Color.WHITE);
        btnAccount.setBounds(10, 160, 180, 40);
        btnAccount.setFocusPainted(false);
        this.add(btnAccount);

        // Doanh thu (admin)
        btnSale = new JButton("Doanh thu");
        btnSale.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSale.setBackground(Color.WHITE);
        btnSale.setBounds(10, 220, 180, 40);
        btnSale.setFocusPainted(false);
        btnSale.setVisible(false);
        this.add(btnSale);

        // Đăng xuất
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setBounds(10, 500, 180, 40);
        btnLogout.setFocusPainted(false);
        this.add(btnLogout);
        

        btnProducts.addActionListener(e -> {
            MainPage.getInstance().showProductPanel();
        });

        btnAccount.addActionListener(e -> {
            MainPage.getInstance().showPersonPanel();
        });

        btnSale.addActionListener(e -> {
//            MainPage.getInstance().showSalePanel();
        });

        btnLogout.addActionListener(e -> {
            try {
                App.getInstance().logout();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        loadDataFromDatabase();
    }
    private void loadDataFromDatabase() {
//        DATABBASE db = new DATABBASE();
        try (Connection conn = null) {
            if (conn == null) return;            

            String sql = "SELECT role FROM users WHERE username = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, this.currentUsername);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");

                        if (role.equalsIgnoreCase("admin")) {
                            btnSale.setVisible(true);
                        } else {
                            btnSale.setVisible(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}