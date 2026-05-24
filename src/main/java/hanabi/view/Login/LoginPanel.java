package hanabi.view.Login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.Main;
import hanabi.components.PopUp;
import hanabi.model.User;
import hanabi.service.AuthService;
import java.util.Optional;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

public class LoginPanel extends JPanel {

        public LoginPanel() {
                this.loginFrame = null;
        }

        public void init(JFrame loginFrame) {
                this.loginFrame = loginFrame;
                setBackground(Color.WHITE);
                setLayout(new MigLayout("wrap, fillx, insets 20 35 25 35", ""));
                initComponents();
                initLayout();
                applyCustomStyles();
                addProps();
        }

        private void initComponents() {

                loginImageLabel = new JLabel();
                titleLabel = new JLabel("Please login to access the Dashboard");
                welcomeTitle = new JLabel("Welcome to Cafe Management system!");
                accountLabel = new JLabel("Account");
                accountTextField = new JTextField();
                passwordLabel = new JLabel("Password");
                passwordTextField = new JPasswordField();
                LoginButt = new JButton("Login");
                LoginButt.addActionListener(this::LoginButtActionPerformed);
        }

        private void initLayout() {
                add(loginImageLabel, "center, gapbottom 12");
                add(titleLabel, "center, gapbottom 4");
                add(welcomeTitle, "center, gapbottom 24");
                add(accountLabel, "gapy 8");
                add(accountTextField, "h 38!, w 350!");
                add(passwordLabel, "gapy 8");
                add(passwordTextField, "h 38!, w 350!");
                add(createUserAgreement(), "center");
                add(LoginButt, "h 42!, w 350!, gapy 8 25, center");
        }

        private void applyCustomStyles() {

                titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                titleLabel.setForeground(Color.decode("#9E9E9E"));
                welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
                welcomeTitle.setForeground(Color.decode("#212121"));
                accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                accountLabel.setForeground(Color.decode("#424242"));
                passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                passwordLabel.setForeground(Color.decode("#424242"));

                LoginButt.setFont(new Font("Segoe UI", Font.BOLD, 14));
                LoginButt.setForeground(Color.WHITE);
                LoginButt.setBackground(Color.decode("#5D4037"));
                LoginButt.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // loading SVG User Icon
                try {
                        FlatSVGIcon svgIcon = new FlatSVGIcon(
                                        "hanabi/assets/icon/user.svg", 64, 64);
                        loginImageLabel.setIcon(svgIcon);
                        loginImageLabel.setText("");
                } catch (Exception e) {
                        loginImageLabel.setText("👤");
                        loginImageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
                }

                // load icon checkbox
                try {
                        URL uncheckUrl = getClass().getResource("/hanabi/assets/icon/CheckIcon.png");
                        URL checkedUrl = getClass().getResource("/hanabi/assets/icon/CheckedIcon.png");

                        if (uncheckUrl != null && checkedUrl != null) {
                                ImageIcon normalIcon = new ImageIcon(uncheckUrl);
                                ImageIcon selectedIcon = new ImageIcon(checkedUrl);

                                Image imgNormal = normalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                                Image imgSelected = selectedIcon.getImage().getScaledInstance(16, 16,
                                                Image.SCALE_SMOOTH);

                                UACCheckBox.setIcon(new ImageIcon(imgNormal));
                                UACCheckBox.setSelectedIcon(new ImageIcon(imgSelected));
                        }
                        UACCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        UACCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        UACCheckBox.setOpaque(false);
                } catch (Exception e) {
                        System.err.println("Not found Checkbox image: " + e.getMessage());
                }
        }

        private void addProps() {
                accountTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                                "Please fill username or email");
                accountTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                                "arc:10;" +
                                "borderWidth:1;" +
                                "borderColor:#E0E0E0;" +
                                "focusWidth:1;" +
                                "innerFocusWidth:0");
                passwordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Please fill your password");
                passwordTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                                "showRevealButton:true;" +
                                "arc:10;" +
                                "borderWidth:1;" +
                                "borderColor:#E0E0E0;" +
                                "focusWidth:1;" +
                                "innerFocusWidth:0");

                LoginButt.putClientProperty(FlatClientProperties.STYLE, "" +
                                "arc:10;" +
                                "borderWidth:0;" +
                                "focusWidth:0;" +
                                "innerFocusWidth:0");
        }

        private Component createUserAgreement() {
                JPanel panel = new JPanel(
                                new FlowLayout(
                                                FlowLayout.LEFT, 2, 0));
                panel.putClientProperty(
                                FlatClientProperties.STYLE,
                                "" + "background:null");
                UACCheckBox = new JCheckBox("I agree to our");
                UACCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JButton Trigger_UA_dialog = new JButton(
                                "<html>" +
                                                "<a href=\"#\">User Agreement</a>" +
                                                "</html>");
                Trigger_UA_dialog.putClientProperty(
                                FlatClientProperties.STYLE,
                                "" + "border:0,0,0,0");

                Trigger_UA_dialog.setContentAreaFilled(false);
                Trigger_UA_dialog.setCursor(new Cursor(Cursor.HAND_CURSOR));
                Trigger_UA_dialog.addActionListener(e -> showUserAgreement());

                panel.add(UACCheckBox);
                panel.add(Trigger_UA_dialog);

                return panel;
        }

        private Component showUserAgreement() {
                PopUp popUp = new PopUp(Main.getFrame(), "", 760, 600);
                popUp.setLayout(new MigLayout(
                                "fill, insets 20 20 20 20",
                                "[center]",
                                "[center]"));

                JPanel panel = new JPanel(new MigLayout(
                                "fill, insets 30",
                                "[center]",
                                "[center]"));

                panel.putClientProperty(FlatClientProperties.STYLE, "" +
                                "arc: 20;" +
                                "[light]background:darken(@background,3%);" +
                                "[dark]background:lighten(@background,3%);");

                JLabel lb = new JLabel("User Agreement");

                lb.setFont(new Font("JetBrainsMono", Font.BOLD, 20));

                String content = "\n" +
                                "Copyright (c) [2025] [MinhCreatorVN]\n" +
                                "\n" +
                                "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                                "of this software and associated documentation files (the \"Software\"), to deal\n" +
                                "in the Software without restriction, including without limitation the rights\n" +
                                "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                                "copies of the Software, and to permit persons to whom the Software is\n" +
                                "furnished to do so, subject to the following conditions:\n" +
                                "\n" +
                                "The above copyright notice and this permission notice shall be included in all\n" +
                                "copies or substantial portions of the Software.\n" +
                                "\n" +
                                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                                "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                                "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                                "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                                "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                                "SOFTWARE.";
                JTextArea textArea = new JTextArea(content);
                textArea.setEditable(false);
                textArea.setFont(new Font("JetBrainsMono", Font.BOLD, 13));
                textArea.setFocusable(false);

                panel.add(lb, "wrap");
                panel.add(textArea, "center, wrap");

                JButton close = new JButton("Close");
                close.setSize(50, 50);
                close.putClientProperty(FlatClientProperties.STYLE, "" +
                                "borderWidth: 0;" +
                                "disabledBorderColor: @background;");
                close.addActionListener(_ -> popUp.dispose());
                panel.add(close, "center");
                popUp.add(panel);
                return popUp;
        }

        private void LoginButtActionPerformed(java.awt.event.ActionEvent evt) {
                String username = accountTextField.getText().trim();
                String pass = new String(passwordTextField.getPassword()).trim();

                if (username.isEmpty() || pass.isEmpty()) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER , "Please fill all fields!");                                        
                        return;
                }

                if (!UACCheckBox.isSelected()) {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                                        "Please agree to the terms!");
                        return;
                }

                AuthService auth = Main.authService;
                Optional<User> result = auth.login(username, pass);
                if (result.isPresent()) {
                        Main.login();
                } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                                        "Invalid username or password!");
                }
        }

        private JFrame loginFrame;
        private JLabel accountLabel, loginImageLabel, passwordLabel, titleLabel, welcomeTitle;
        private JTextField accountTextField;
        private JButton LoginButt;
        private JCheckBox UACCheckBox;
        private JPasswordField passwordTextField;
}