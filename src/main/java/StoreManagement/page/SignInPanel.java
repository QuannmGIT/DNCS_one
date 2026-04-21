package StoreManagement.page;

import StoreManagement.Utility.RoundedBorder;
import StoreManagement.customClass.PasswordStrengthStatus;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static StoreManagement.App.logout;

public class SignInPanel extends JPanel {
    private static SignInPanel instance;
    private JLabel lblTitle, lblName, lblUser, lblEmail, lblPass, lblConfirmPassword;
    private JTextField txtName, txtUser, txtEmail;
    private JPasswordField txtPass, txtConfirmPassword;
    private JButton btnRegister, btnBack;
    public SignInPanel() {
        init();
        initComponent();
        addProps();
        instance = this;
    }

    private void init(){
        setLayout(new MigLayout(
                "fill, insets -20",
                "[center]",
                "[center]"
        ));
    }

    private void initComponent(){
        JPanel panel = new JPanel(
                new MigLayout(
                        "wrap, fillx, insets 35 45 30 45",
                        "[fill,360]"
                )
        );
        lblTitle = new JLabel("                 Sign Up");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));


        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Họ và tên
        lblName = new JLabel("Full name:");
        lblName.setFont(labelFont);


        txtName = new JTextField();
        txtName.setFont(textFont);

        // Tài khoản
        lblUser = new JLabel("Username:");
        lblUser.setFont(labelFont);


        txtUser = new JTextField();
        txtUser.setFont(textFont);

        // Email
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);

        txtEmail = new JTextField();
        txtEmail.setFont(textFont);

        // Passwordq
        lblPass = new JLabel("Password:");
        lblPass.setFont(labelFont);

        // Confirm password
        lblConfirmPassword = new JLabel("Password:");
        lblConfirmPassword.setFont(labelFont);


        txtPass = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        PasswordStrengthStatus passwordStrengthStatus = new PasswordStrengthStatus();
        txtPass.setFont(textFont);

        passwordStrengthStatus.initPasswordField(txtPass);




        // register button
        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(32, 178, 170));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.setFocusable(false);

        // Nút quay lại
//        btnBack = new JButton("Go back");
//        btnBack.setBackground(Color.WHITE);
//        btnBack.setForeground(Color.BLACK);
//        btnBack.setFocusable(false);
//        panel.add(btnBack);

        panel.add(lblTitle);
        panel.add(lblName, "gapy 8");
        panel.add(txtName);
        panel.add(lblUser, "gapy 8");
        panel.add(txtUser);
        panel.add(lblEmail, "gapy 8");
        panel.add(txtEmail);
        panel.add(lblPass, "gapy 8");
        panel.add(txtPass);
        panel.add(passwordStrengthStatus);
        panel.add(lblConfirmPassword, "gapy 8");
        panel.add(txtConfirmPassword);
        panel.add(btnRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 20");
        add(panel);
    }

    private void addProps(){
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your full name");
        txtUser.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email please");
        txtPass.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password please");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password please");

        txtPass.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );
    }

    private Component createLoginLabel() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 0, 0
                )
        );
        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "background:null"
        );

        JButton cmdRegister = new JButton(
                "<html>" +
                        "<a href=\"#\">Login now</a>" +
                        "</html>"
        );
        cmdRegister.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {

            logout();
        });
        JLabel label = new JLabel("Already have an admin account ?");
        label.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }
    public static SignInPanel getInstance(){
        return instance;
    }
}