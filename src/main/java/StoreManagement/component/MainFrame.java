package StoreManagement.component;

import StoreManagement.login.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel rightContainer; 
    private LoginPanel loginPanel;

    public static void main(String[] args) {

    }
    public MainFrame() {
        this.setTitle("Hanabi Cafe");
        this.setSize(1000, 600);
        ImageIcon icon = new ImageIcon(getClass().getResource("/ImageFile/HanabiIcon.png"));
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel allJPanel = new JPanel();
        this.add(allJPanel);
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
        CardLayout cl = (CardLayout) rightContainer.getLayout();
        cl.show(rightContainer, "SIGNIN");
    }

    // vao menu


}