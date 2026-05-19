package com.hanabi.view.Login;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends javax.swing.JPanel {

    private JFrame loginFrame;

    public LoginPanel() {
        this(null);
    }

    public LoginPanel(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        initComponents();
        applyCustomStyles();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginImageLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        welcomeTitle = new javax.swing.JLabel();
        accountLabel = new javax.swing.JLabel();
        accountTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JPasswordField(); 
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        loginImageLabel.setText("Image");

        titleLabel.setText("Please login to access the Dashboard");

        welcomeTitle.setText("Welcome to Cafe Management system!");

        accountLabel.setText("Account:");

        accountTextField.setText("");

        passwordLabel.setText("Password:");

        passwordTextField.setText(""); 

        jCheckBox1.setText("I agree to our User Agreement");
        jCheckBox1.addActionListener(this::jCheckBox1ActionPerformed);

        jButton1.setText("Login");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        int formW = 260;
        int mg = 35;

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGap(mg, mg, mg)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loginImageLabel, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(welcomeTitle, javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(accountLabel)
                    .addComponent(accountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, formW, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, formW, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox1)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, formW, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(mg, mg, mg)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(loginImageLabel)
                .addGap(12, 12, 12)
                .addComponent(titleLabel)
                .addGap(4, 4, 4)
                .addComponent(welcomeTitle)
                .addGap(24, 24, 24)
                .addComponent(accountLabel)
                .addGap(4, 4, 4)
                .addComponent(accountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(passwordLabel)
                .addGap(4, 4, 4)
                .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jCheckBox1)
                .addGap(16, 16, 16)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void applyCustomStyles() {
        this.setBackground(Color.WHITE);

        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(0x9E9E9E));
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeTitle.setForeground(new Color(0x212121));
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        accountLabel.setForeground(new Color(0x424242));
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(new Color(0x424242));

        accountTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Please fill username or email");
        accountTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10;" +
                "borderWidth:1;" +
                "borderColor:#E0E0E0;" +
                "focusWidth:1;" +
                "innerFocusWidth:0"
        );
        passwordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Please fill your password");
        passwordTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true;" +
                "arc:10;" +
                "borderWidth:1;" +
                "borderColor:#E0E0E0;" +
                "focusWidth:1;" +
                "innerFocusWidth:0"
        );

        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButton1.setForeground(Color.WHITE);
        jButton1.setBackground(new Color(0x5D4037));
        jButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton1.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0"
        );

        // Nạp SVG User Icon
        try {
            FlatSVGIcon svgIcon = new FlatSVGIcon("com/hanabi/resources/StoreManagement/assets/icon/user.svg", 64, 64);
            loginImageLabel.setIcon(svgIcon);
            loginImageLabel.setText("");
        } catch (Exception e) {
            loginImageLabel.setText("👤");
            loginImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        }

        // Nạp icon checkbox
        try {
            java.net.URL uncheckUrl = getClass().getResource("/com/hanabi/resources/StoreManagement/assets/icon/CheckIcon.png");
            java.net.URL checkedUrl = getClass().getResource("/com/hanabi/resources/StoreManagement/assets/icon/CheckedIcon.png");

            if (uncheckUrl != null && checkedUrl != null) {
                ImageIcon normalIcon = new ImageIcon(uncheckUrl);
                ImageIcon selectedIcon = new ImageIcon(checkedUrl);

                Image imgNormal = normalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                Image imgSelected = selectedIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);

                jCheckBox1.setIcon(new ImageIcon(imgNormal));
                jCheckBox1.setSelectedIcon(new ImageIcon(imgSelected));
            }
            jCheckBox1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            jCheckBox1.setCursor(new Cursor(Cursor.HAND_CURSOR));
            jCheckBox1.setOpaque(false);
        } catch (Exception e) {
            System.err.println("Không tìm thấy tệp tin hình ảnh Checkbox: " + e.getMessage());
        }
    }
    
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
         // Xử lý logic phụ khi nhấn hộp kiểm nếu cần thiết
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String user = accountTextField.getText().trim();
        String pass = new String(passwordTextField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all fields!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!jCheckBox1.isSelected()) {
            JOptionPane.showMessageDialog(this,
                "Please agree to the terms!",
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Login successful! Welcome " + user + "!",
            "Success", JOptionPane.INFORMATION_MESSAGE);

        if (loginFrame != null) {
            loginFrame.dispose();
        }

        java.awt.EventQueue.invokeLater(() ->
            com.hanabi.view.Category.DashboardView.main(new String[0])
        );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountLabel;
    private javax.swing.JTextField accountTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel loginImageLabel;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordTextField; // Đã cập nhật thành công kiểu JPasswordField
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel welcomeTitle;
    // End of variables declaration//GEN-END:variables
}