package StoreManagement.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Latte extends JPanel {

    public Latte() {
        // --- Cấu hình Panel chính ---
        this.setLayout(new BorderLayout(20, 20)); // Khoảng cách giữa các phần
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(30, 30, 30, 30)); // Căn lề 4 phía

        // --- 1. TIÊU ĐỀ (NORTH) ---
        JLabel lblTitle = new JLabel("LATTE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(new Color(101, 67, 33)); // Màu nâu cà phê
        this.add(lblTitle, BorderLayout.NORTH);


        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        contentPanel.setBackground(Color.WHITE);

        // --- Cột Trái: Hình ảnh ---
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1)); // Viền mỏng quanh ảnh

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);

        // Load ảnh (Xử lý an toàn nếu thiếu ảnh)
        try {
            // Đảm bảo bạn đã có file Latte.png trong thư mục ImageFile
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/ImageFile/Latte.png"));
            Image img = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImage.setText("[Thiếu ảnh Latte.png]");
            lblImage.setForeground(Color.RED);
        }
        imagePanel.add(lblImage, BorderLayout.CENTER);
        contentPanel.add(imagePanel);

        // --- Cột Phải: Mô tả và Giá ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Đoạn văn mô tả
        JTextArea txtDesc = new JTextArea(
            "Ly cà phê sữa ngọt ngào đến khó quên! Với một chút nhẹ nhàng hơn so với Cappuccino, " +
            "Latte của chúng tôi bắt đầu với cà phê espresso, sau đó thêm sữa tươi và bọt sữa một cách đầy nghệ thuật. " +
            "Bạn có thể tùy thích lựa chọn uống nóng hoặc dùng chung với đá."
        );
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDesc.setForeground(new Color(80, 80, 80)); // Màu xám đậm
        txtDesc.setLineWrap(true);       // Tự xuống dòng
        txtDesc.setWrapStyleWord(true);  // Ngắt theo từ
        txtDesc.setEditable(false);      // Không cho sửa
        txtDesc.setFocusable(false);     // Không nhận focus chuột
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Giá tiền
        JLabel lblPrice = new JLabel("Giá : 65,000 VNĐ");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPrice.setForeground(Color.BLACK);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Nút thêm vào giỏ hàng (Option thêm cho đẹp)
        JButton btnOrder = new JButton("Thêm vào giỏ");
        btnOrder.setBackground(new Color(179, 40, 45)); // Màu đỏ Highlands
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOrder.setFocusPainted(false);
        btnOrder.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOrder.setMaximumSize(new Dimension(150, 40));

        // Thêm các thành phần vào cột phải
        infoPanel.add(txtDesc);
        infoPanel.add(Box.createVerticalStrut(30)); // Khoảng cách
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(20)); // Khoảng cách
        infoPanel.add(btnOrder);
        
        // Đẩy nội dung lên trên cùng
        infoPanel.add(Box.createVerticalGlue());

        contentPanel.add(infoPanel);

        this.add(contentPanel, BorderLayout.CENTER);
    }
}