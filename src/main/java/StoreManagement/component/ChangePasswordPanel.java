package StoreManagement.component;

import StoreManagement.Utility.RoundedBorder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import StoreManagement.Utility.DATABBASE;
import StoreManagement.page.MainPage;

public class ChangePasswordPanel extends JPanel {

    private ManageFrame frame;
    private String username; 

    public ChangePasswordPanel(String username) {
        this.username = username;
        
        this.setLayout(null);
        this.setBackground(Color.WHITE);


        JLabel lblTitle = new JLabel("Đổi Mật Khẩu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBounds(40, 20, 300, 40);
        this.add(lblTitle);


        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(40, 80, 550, 350);
        formPanel.setBorder(new LineBorder(Color.BLACK, 1));
        this.add(formPanel);


        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        //Mật khẩu cũ
        JLabel lblOldPass = new JLabel("Mật khẩu cũ:");
        lblOldPass.setFont(labelFont);
        lblOldPass.setBounds(50, 50, 150, 30);
        formPanel.add(lblOldPass);

        JPasswordField txtOldPass = new JPasswordField();
        txtOldPass.setFont(textFont);
        txtOldPass.setBounds(200, 50, 250, 35);
        txtOldPass.setBorder(new RoundedBorder(10));
        formPanel.add(txtOldPass);

        //Mật khẩu mới
        JLabel lblNewPass = new JLabel("Mật khẩu mới:");
        lblNewPass.setFont(labelFont);
        lblNewPass.setBounds(50, 110, 150, 30);
        formPanel.add(lblNewPass);

        JPasswordField txtNewPass = new JPasswordField();
        txtNewPass.setFont(textFont);
        txtNewPass.setBounds(200, 110, 250, 35);
        txtNewPass.setBorder(new RoundedBorder(10));
        formPanel.add(txtNewPass);

        //Nhập lại mật khẩu mới
        JLabel lblConfirmPass = new JLabel("Nhập lại mới:");
        lblConfirmPass.setFont(labelFont);
        lblConfirmPass.setBounds(50, 170, 150, 30);
        formPanel.add(lblConfirmPass);

        JPasswordField txtConfirmPass = new JPasswordField();
        txtConfirmPass.setFont(textFont);
        txtConfirmPass.setBounds(200, 170, 250, 35);
        txtConfirmPass.setBorder(new RoundedBorder(10));
        formPanel.add(txtConfirmPass);

        //Nút Xác nhận thay đổi
        JButton btnConfirm = new JButton("Xác nhận thay đổi");
        btnConfirm.setBackground(new Color(32, 178, 170));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnConfirm.setBounds(125, 250, 300, 45);
        btnConfirm.setFocusable(false);
        formPanel.add(btnConfirm);


        JButton btnBack = new JButton("Hủy bỏ");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(Color.BLACK);
        btnBack.setBounds(620, 80, 150, 50);
        btnBack.setFocusable(false);
        this.add(btnBack);


        btnBack.addActionListener(e -> MainPage.getInstance().showPersonPanel());

        btnConfirm.addActionListener(e -> {
            String oldPass = new String(txtOldPass.getPassword()).trim();
            String newPass = new String(txtNewPass.getPassword()).trim();
            String confirmPass = new String(txtConfirmPass.getPassword()).trim();


            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không trùng khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (oldPass.equals(newPass)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu mới không được giống mật khẩu cũ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }


            changePassword(oldPass, newPass);
        });
    }

    private void changePassword(String oldPass, String newPass) {
//        DATABBASE db = new DATABBASE();
        try (Connection conn = null;)
         {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!");
                return;
            }


            String checkSql = "";
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, this.username);
                psCheck.setString(2, oldPass);
                
                if (!psCheck.executeQuery().next()) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu cũ không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                psUpdate.setString(1, newPass);
                psUpdate.setString(2, this.username);
                
                int row = psUpdate.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                    MainPage.getInstance().showPersonPanel();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage());
        }
    }
}