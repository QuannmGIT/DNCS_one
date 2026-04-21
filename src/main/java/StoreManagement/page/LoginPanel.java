package StoreManagement.page;

import StoreManagement.component.BannerPanel;
import StoreManagement.component.PopUp;
import StoreManagement.App;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;

import static StoreManagement.App.app;

public class LoginPanel extends JPanel {
    private JLabel loginImageLabel, titleLabel, WelcomeTitle, AccountLabel, PasswordLabel;
    private JButton LoginBtn;
    private JTextField AccountTextField;
    private JPasswordField PasswordTextField;
    public static LoginPanel loginPanel;
    private JCheckBox UserAgreement;
    private JButton cmdRegister;
    private JPanel Container;
    private JPanel rightContainer;

    public LoginPanel() {
        init();
        initComponent();
        addProperty();
}

    private void init(){
        setLayout(new MigLayout(
                "wrap, fillx, insets 35 45 30 45",
                "fill,250:280"
        ));

    }
    private void initComponent(){
        setLayout(new BorderLayout());
        BannerPanel banner = new BannerPanel();
        add(banner, BorderLayout.WEST);
        add(RightContainer(), BorderLayout.CENTER);
    }

    private Component RightContainer(){
        JPanel RContainer = new JPanel();
        RContainer.setLayout(new MigLayout(
                "wrap, fillx, insets 35 45 30 45",
                "fill,250:280"
        ));

        //Account
        AccountLabel = new JLabel("Account:");
        AccountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        AccountTextField = new JTextField();
        AccountTextField.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        //Password
        PasswordLabel = new JLabel("Password:");
        PasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        PasswordTextField = new JPasswordField();
        PasswordTextField.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        RContainer.add(loginLabel());
        RContainer.add(titleLabel());
        RContainer.add(Welcome());
        RContainer.add(AccountLabel, "gapy 8");
        RContainer.add(AccountTextField);
        RContainer.add(PasswordLabel, "gapy 8");
        RContainer.add(PasswordTextField);
        RContainer.add(UADialogInit() , "gapx 0");
        RContainer.add(LoginBtn());
        RContainer.add(createSignupLabel(), "gapy 8");
        return RContainer;
    }

    private JLabel loginLabel(){
        FlatSVGIcon icon = new FlatSVGIcon("StoreManagement/assets/icon/user.svg", 60, 60);
        loginImageLabel = new JLabel(icon);
        loginImageLabel.setBounds(140, 20, 100, 100);
        return loginImageLabel;
    }

    private JLabel titleLabel(){
        titleLabel = new JLabel("            Please login to access the Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return titleLabel;
    }

    private JLabel Welcome(){
        WelcomeTitle = new JLabel("Welcome to Cafe Management system!");
        WelcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        WelcomeTitle.setForeground(Color.BLACK);
//        WelcomeTitle.setBounds(70, 130, 300, 40);
        return WelcomeTitle;
    }
    private Component UADialogInit(){
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 10, 0
                )
        );
        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "background:null"
        );

        UserAgreement = new JCheckBox("I agree to our");
        UserAgreement.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton Trigger_UA_dialog = new JButton(
                "<html>" +
                        "<a href=\"#\">User Agreement</a>" +
                        "</html>"
        );
        Trigger_UA_dialog.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );

        Trigger_UA_dialog.setContentAreaFilled(false);
        Trigger_UA_dialog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Trigger_UA_dialog.addActionListener(e -> UA_dialog());

        panel.add(UserAgreement);
        panel.add(Trigger_UA_dialog);
        return panel;
    }

    private Component UA_dialog() {
        PopUp popUp = new PopUp(App.getInstance(), "", 760, 600);
        popUp.setLayout(new MigLayout(
                "fill, insets 20 20 20 20",
                "[center]",
                "[center]"
        ));

        JPanel panel = new JPanel();

        panel.setLayout(new MigLayout(
                "fill, insets 30",
                "[center]",
                "[center]"
        ));

        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);"
        );
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
                "disabledBorderColor: @background;"
        );
        close.addActionListener(_ -> popUp.dispose());
        panel.add(close, "center");
        popUp.add(panel);
        return popUp;
    }

    private Component LoginBtn(){
        //Login button
        LoginBtn = new JButton("Login");
        LoginBtn.setFocusable(false);
        LoginBtn.setFont(new Font("HelveticaNeue", Font.BOLD, 13));
        LoginBtn.setBounds(70, 400, 250, 40);
        LoginBtn.addActionListener(e -> {
            String user = AccountTextField.getText().trim();
            String pass = new String(PasswordTextField.getPassword()).trim();
            Notifications notice = Notifications.getInstance();

            if (user.equals("Please fill username or email")) user = "";
            if (pass.equals("123456789")) pass = "";

            if (user.isEmpty() || pass.isEmpty()) {
                notice.show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Please fill all fields!");
                return;
            }

            if (!UserAgreement.isSelected()) {
                notice.show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Please agree to the terms!");

                return;
            }
            app.login();

        });
        return LoginBtn;
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 0, 0
                )
        );
        panel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "background:null"
        );

        cmdRegister = new JButton(
                "<html>" +
                        "<a href=\"#\">Sign up</a>" +
                        "</html>"
        );
        cmdRegister.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "border:0,0,0,0"
        );
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> App.signup());


        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );

        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }

    private void addProperty(){
        // style for panel
        this.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 20;" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,3%);"
        );
        AccountTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Please fill username or email");

        PasswordTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Please fill your password");
        PasswordTextField.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true"
        );

        LoginBtn.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground: lighten(@foreground, 10%);" +
                "[dark]foreground: darken(@foreground, 10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "selectedBackground: darken(@background, 30%)"
        );
        LoginBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "");

        LoginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel.putClientProperty(
                FlatClientProperties.STYLE,
                "" + "[light]foreground: lighten(@foreground, 30%);"
                        + "[dark]foreground: darken(@foreground, 30%);"
        );
    }

    public static LoginPanel getInstance(){
        if (loginPanel != null) {
            return loginPanel;
        }
        return new LoginPanel();
    }
}