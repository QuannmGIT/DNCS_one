package hanabi.service;

import java.awt.Component;
import java.awt.HeadlessException;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.model.Staff;
import hanabi.util.PasswordUtil;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

public class CreateUser extends JPanel {

        private JLabel lbEmail, Fname, lbSalary;
        private JTextField txtEmail, txtName, txtSalary;
        private JLabel lbPassword;
        private JPasswordField txtPassword;
        private final JButton cmdCreate;
        private JCheckBox staffBox, adminBox;
        private static JFrame f;
        private final AccountService accountService = new AccountService();

        public static void main(String[] args) {
                FlatLightLaf.setup();
                CreateUser.init();
        }

        public CreateUser() {
                setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 380", "[fill]"));

                add(new JLabel(new FlatSVGIcon("hanabi/assets/icon/AccountIcon.svg")), "center");
                add(new JSeparator(), "gapy 15 15");

                Fname = new JLabel("Full name");
                Fname.putClientProperty(FlatClientProperties.STYLE, "" +
                                "font:bold;");
                add(Fname, "gapy 10 n");
                txtName = new JTextField();
                txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " Ngo Minh Quan");
                add(txtName);

                lbEmail = new JLabel("Email");
                lbEmail.putClientProperty(FlatClientProperties.STYLE, "" +
                                "font:bold;");
                add(lbEmail, "split 2, gapy 10 n");
                add(optionalTag(), "right, gapx 200");

                txtEmail = new JTextField();
                txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "example@mail.com");
                add(txtEmail);

                lbPassword = new JLabel("Create a password");
                lbPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                                "font:bold;");
                add(lbPassword, "gapy 10 n");

                txtPassword = new JPasswordField();
                txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "at least 8 characters");
                add(txtPassword);

                lbSalary = new JLabel("Salary");
                lbSalary.putClientProperty(FlatClientProperties.STYLE, "" +
                                "font:bold;");
                add(lbSalary, "split 2, gapy 10 n");

                txtSalary = new JTextField();
                txtSalary.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "100000");
                add(txtSalary);

                add(role(), "center");

                cmdCreate = new JButton("Create") {
                        @Override
                        public boolean isDefaultButton() {
                                return true;
                        }
                };
                cmdCreate.putClientProperty(FlatClientProperties.STYLE, "" +
                                "foreground:#FFFFFF;");
                add(cmdCreate);
                // event
                cmdCreate.addActionListener(actionEvent -> {
                        form();
                });
                setVisible(true);
        }

        public static void init() {
                f = new JFrame();
                f.setTitle("Create new staff");
                f.add(new CreateUser());
                f.setSize(350, 430);
                f.setResizable(false);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
        }

        private Component role() {
                JPanel optPanel = new JPanel(new MigLayout("wrap 2, fillx, insets 5"));

                staffBox = new JCheckBox("Staff");
                adminBox = new JCheckBox("Admin");

                optPanel.add(staffBox, "split 2");
                optPanel.add(adminBox, "gapx 25");

                staffBox.setSelected(true);

                if (staffBox.isSelected() == true) {
                        adminBox.setSelected(false);
                        this.repaint();
                } else if (adminBox.isSelected() == true) {
                        staffBox.setSelected(false);
                        this.repaint();
                }
                return optPanel;
        }

        private Component optionalTag() {
                JLabel otpLabel = new JLabel("Optional");
                otpLabel.putClientProperty(FlatClientProperties.STYLE, "" +
                                "font:italic; foreground:#929493;");

                return otpLabel;
        }

        private void form() {
                String Name = txtName.getText();
                String email = txtEmail.getText();
                try {
                        double salary = 0;
                        String salaryText = txtSalary.getText().trim();
                        if (!salaryText.isEmpty()) {
                                salary = Double.parseDouble(salaryText);
                        }
                        if (Name != null && !Name.trim().isEmpty()) {
                                Staff s = new Staff();
                                s.setStaffId(java.util.UUID.randomUUID());
                                s.setStaffName(Name.trim());
                                s.setFullName(Name.trim());
                                String rawPass = new String(txtPassword.getPassword());
                                String salt = PasswordUtil.generateSalt();
                                s.setPassword(salt + ":" + PasswordUtil.hash(rawPass, salt));
                                s.setRole(staffBox.isSelected() ? "staff" : "admin");
                                s.setStatus(true);

                                if (email != null && !email.trim().isEmpty()) {
                                        s.setEmail(email);
                                }

                                accountService.addStaff(s, salary);
                                JOptionPane.showMessageDialog(this,
                                                "User \"" + Name.trim()
                                                                + "\" added successfully!",
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                                f.dispose();

                        }
                } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                                        "Salary must be a valid number!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                } catch (HeadlessException e) {
                        System.err.println(e.getMessage());
                }

        }
}