package StoreManagement.component;

import StoreManagement.login.LoginPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel rightContainer; 
    private LoginPanel loginPanel;

    public static void main(String[] args) {

    }
    public MainFrame() {
        setTitle("Hanabi Cafe");
        setSize(1000, 600);
        try {
            FlatSVGIcon icon = new FlatSVGIcon("/assets/img/HanabiIcon.svg");
//            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/img/HanabiIcon.png")); deprecated icon
           setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel allJPanel = new JPanel();
        add(allJPanel);
        allJPanel.setLayout(new BorderLayout());
        allJPanel.setBackground(Color.WHITE);

        BannerPanel banner = new BannerPanel();
        allJPanel.add(banner, BorderLayout.WEST);

        rightContainer = new JPanel();
        rightContainer.setLayout(new CardLayout()); 

        loginPanel = new LoginPanel(this);

        rightContainer.add(loginPanel, "LOGIN");

        allJPanel.add(rightContainer, BorderLayout.CENTER);
    }

    public void showSignInPanel() {
        CardLayout cardLayoutConfig = (CardLayout) rightContainer.getLayout();
        cardLayoutConfig.show(rightContainer, "SIGNIN");
    }

}