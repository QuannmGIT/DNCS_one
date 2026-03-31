package StoreManagement.login;

import StoreManagement.component.MainFrame;
import StoreManagement.component.ManageFrame;
import StoreManagement.Utility.RoundedBorder;
import StoreManagement.Utility.dbConnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.setBackground(Color.WHITE);
        this.setLayout(null);

        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/assets/img/LoginIcon.png"));
        Image loginImage = loginIcon.getImage();
        Image newLoginImage = loginImage.getScaledInstance(70, 60, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledLoginIcon = new ImageIcon(newLoginImage);
        JLabel loginImageLabel = new JLabel(scaledLoginIcon);
        loginImageLabel.setBounds(150, 20, 100, 100);
        this.add(loginImageLabel);

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBounds(125, 100, 200, 40); 
        this.add(titleLabel);

        JLabel titleLabel2 = new JLabel("Chào mừng bạn đến với Hanabi Cafe");
        titleLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleLabel2.setForeground(Color.BLACK);
        titleLabel2.setBounds(70, 130, 300, 40); 
        this.add(titleLabel2);


        //Taikhoan
        JLabel AccountLabel = new JLabel("Tài Khoản:");
        AccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        AccountLabel.setBounds(147,180, 150, 30);
        this.add(AccountLabel);
       
    
        JTextField AccountTextField = new JTextField("Nhập tài khoản hoặc email");
        AccountTextField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        AccountTextField.setBounds(70, 215, 250, 35);
        AccountTextField.setForeground(Color.GRAY);
        AccountTextField.setBorder(new RoundedBorder(10));
        this.add(AccountTextField);


        AccountTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (AccountTextField.getText().equals("Nhập tài khoản hoặc email")) {
                    AccountTextField.setText("");
                    AccountTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (AccountTextField.getText().isEmpty()) {
                    AccountTextField.setText("Nhập tài khoản hoặc email");
                    AccountTextField.setForeground(Color.GRAY);
                }
            }
        });


        //Matkhau
        JLabel PasswordLabel = new JLabel("Mật Khẩu:");
        PasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        PasswordLabel.setBounds(147,270, 150, 30);
        this.add(PasswordLabel);


        JPasswordField PasswordTextField = new JPasswordField("123456789");
        PasswordTextField.setFont(new Font("Segoe UI", Font.BOLD, 15));
        PasswordTextField.setBounds(70, 305, 250, 35);
        PasswordTextField.setForeground(Color.GRAY);
        PasswordTextField.setBorder(new RoundedBorder(10));
        this.add(PasswordTextField);


        PasswordTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String password = new String(PasswordTextField.getPassword());
                if (password.equals("123456789")) {
                    PasswordTextField.setText("");
                    PasswordTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String password = new String(PasswordTextField.getPassword());
                if (password.isEmpty()) {
                    PasswordTextField.setText("123456789");
                    PasswordTextField.setForeground(Color.GRAY);
                }
            }
        });

        //Checkbox
        JCheckBox CheckBox = new JCheckBox("Tôi Đồng Ý với điều khoản");
        CheckBox.setFocusable(false);
        CheckBox.setFont(new Font("HelveticaNeue", Font.BOLD, 13));        
        CheckBox.setBackground(Color.WHITE);
        CheckBox.setBounds(70, 360, 250, 25);
        this.add(CheckBox);


        ImageIcon checkIcon = new ImageIcon(getClass().getResource("/assets/img/CheckIcon.png"));
        Image checkImage = checkIcon.getImage();
        Image newCheckImage = checkImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledCheckIcon = new ImageIcon(newCheckImage);
        CheckBox.setIcon(scaledCheckIcon);

        
        ImageIcon checkedIcon = new ImageIcon(getClass().getResource("/assets/img/CheckedIcon.png"));
        Image checkedImage = checkedIcon.getImage();
        Image newCheckedImage = checkedImage.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledCheckedIcon = new ImageIcon(newCheckedImage);
        CheckBox.setSelectedIcon(scaledCheckedIcon);


        //Nut Dang Nhap
        JButton LoginBtn = new JButton("Đăng Nhập");
        LoginBtn.setFocusable(false);
        LoginBtn.setBackground(Color.WHITE);
        LoginBtn.setFont(new Font("HelveticaNeue", Font.BOLD, 13));
        LoginBtn.setBounds(70, 400, 250, 40);
        LoginBtn.setBorder(new RoundedBorder(20));
        this.add(LoginBtn);
        

        LoginBtn.addActionListener(e -> {
            String user = AccountTextField.getText().trim();
            String pass = new String(PasswordTextField.getPassword()).trim();


            if (user.equals("Nhập tài khoản hoặc email"))  user = "";
            if (pass.equals("123456789")) pass = "";

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!CheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Vui lòng đồng ý điều khoản!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Make connection to DB
            dbConnect db = new dbConnect();
            try (Connection conn = db.getConnection()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Không thể kết nối Database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, user);
                    ps.setString(2, user);
                    ps.setString(3, pass);


                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String userDB = rs.getString("username"); 
                            new ManageFrame(userDB).setVisible(true);
                            this.mainFrame.dispose(); 
                        } else {
                            JOptionPane.showMessageDialog(this, "Sai username hoặc password!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

}
}