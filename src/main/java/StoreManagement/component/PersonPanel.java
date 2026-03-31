package StoreManagement.component;

import StoreManagement.Utility.dbConnect;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonPanel extends JPanel {
    private JLabel lblFullName, lblEmail, lblUsername;
    private JLabel lblTotalOrder, lblPoints;
    
    private JButton btnCreateAcc; 
    private JButton btnFireStaff;

    private String currentUsername;

    private ManageFrame manageFrame;

    public PersonPanel(ManageFrame frame, String username) {
        this.manageFrame = frame;
        this.currentUsername = username;

        this.setLayout(null);
        this.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thông tin tài khoản");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setBounds(40, 20, 300, 40);
        this.add(lblTitle);


        JPanel profilePanel = new JPanel(null);
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBounds(40, 80, 550, 350);
        profilePanel.setBorder(new LineBorder(Color.BLACK, 1));
        this.add(profilePanel);


        JLabel lblAvatar = new JLabel();
        lblAvatar.setBorder(new LineBorder(Color.GRAY));
        lblAvatar.setBounds(30, 30, 100, 100);
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/img/PersonIcon.png"));
        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        lblAvatar.setIcon(new ImageIcon(img));

        profilePanel.add(lblAvatar);


        lblFullName = new JLabel("Đang tải...");
        lblFullName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFullName.setBounds(150, 30, 350, 30);
        profilePanel.add(lblFullName);

        lblEmail = new JLabel("...");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblEmail.setBounds(150, 70, 350, 25);
        profilePanel.add(lblEmail);

        lblUsername = new JLabel("...");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsername.setForeground(new Color(32, 178, 170));
        lblUsername.setBounds(150, 100, 350, 25);
        profilePanel.add(lblUsername);


        JSeparator sep = new JSeparator();
        sep.setBounds(0, 160, 550, 10);
        sep.setForeground(Color.BLACK);
        profilePanel.add(sep);


        JPanel boxOrder = createStatsBox("Tổng đơn hàng", "0");
        boxOrder.setBounds(30, 190, 200, 100);
        lblTotalOrder = (JLabel) boxOrder.getComponent(1);
        profilePanel.add(boxOrder);

        JPanel Point = createStatsBox("Điểm", "0");
        Point.setBounds(280, 190, 200, 100);
        lblPoints = (JLabel) Point.getComponent(1);
        profilePanel.add(Point);


        JButton btnChangePass = new JButton("Đổi mật khẩu");
        btnChangePass.setBackground(Color.WHITE);
        btnChangePass.setBounds(620, 80, 150, 50);
        btnChangePass.setFocusable(false);
        this.add(btnChangePass);

        btnChangePass.addActionListener(e -> {
        this.manageFrame.showChangePasswordPanel();
        });


        btnCreateAcc = new JButton("Tạo tài khoản");
        btnCreateAcc.setBackground(Color.WHITE);
        btnCreateAcc.setBounds(620, 150, 150, 50);
        btnCreateAcc.setFocusable(false);
        btnCreateAcc.setVisible(false); 
        
        this.add(btnCreateAcc);


        btnCreateAcc.addActionListener(e -> {
            this.manageFrame.showSignInPanel(); 
        });


        btnFireStaff = new JButton("Đuổi việc");
        btnFireStaff.setBackground(new Color(255, 69, 0)); 
        btnFireStaff.setForeground(Color.WHITE);
        btnFireStaff.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFireStaff.setBounds(620, 220, 150, 50);
        btnFireStaff.setFocusable(false);
        btnFireStaff.setVisible(false); 
        this.add(btnFireStaff);
        btnFireStaff.addActionListener(e -> {
        String staffUser = JOptionPane.showInputDialog(this, "Nhập tài khoản nhân viên muốn cho nghỉ việc:");
        
        if (staffUser != null && !staffUser.trim().isEmpty()) {
            if (staffUser.equals(currentUsername)) {
                JOptionPane.showMessageDialog(this, "Bạn không thể tự đuổi việc chính mình!");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Xác nhận cho tài khoản '" + staffUser + "' nghỉ việc?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                updateStaffStatus(staffUser, 0); 
            }
        }
    });
        loadDataFromDatabase();
    }

    private JPanel createStatsBox(String title, String value) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new LineBorder(Color.BLACK, 1));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(t); panel.add(v);
        return panel;
    }

    private void updateStaffStatus(String username, int status) {
        dbConnect db = new dbConnect();
        try (Connection conn = db.getConnection()) {
        if (conn == null) return;


        String sql = "UPDATE users SET status = ? WHERE username = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, username);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái nghỉ việc cho: " + username);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản nhân viên này!");
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thực hiện: " + e.getMessage());
        }
    }

    private void loadDataFromDatabase() {
        dbConnect db = new dbConnect();
        try (Connection conn = db.getConnection()) {
            if (conn == null) return;

            String sql = "SELECT u.full_name, u.email, u.username, u.role, " + 
                         "IFNULL(a.Sum_order, 0) as Sum_order, " +
                         "IFNULL(a.Point, 0) as Point " +
                         "FROM users u " +
                         "LEFT JOIN average a ON u.user_id = a.user_id " +
                         "WHERE u.username = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, this.currentUsername);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        lblFullName.setText(rs.getString("full_name"));
                        lblEmail.setText(rs.getString("email"));
                        lblUsername.setText(rs.getString("username"));
                        lblTotalOrder.setText(String.valueOf(rs.getInt("Sum_order")));
                        lblPoints.setText(String.valueOf(rs.getInt("Point")));


                        String role = rs.getString("role");
                      

                        if (role != null && role.equalsIgnoreCase("admin")) {
                            btnCreateAcc.setVisible(true);
                            btnFireStaff.setVisible(true);
                        } else {
                            btnCreateAcc.setVisible(false);
                            btnFireStaff.setVisible(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblFullName.setText("Lỗi kết nối");
        }
    }
}