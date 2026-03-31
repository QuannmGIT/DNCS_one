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
            //FlatSVGIcon ic = new FlatSVGIcon("ImageFile/HanabiIcon.svg");
            ImageIcon icon = new ImageIcon(getClass().getResource("/ImageFile/HanabiIcon.png"));
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
        CardLayout cl = (CardLayout) rightContainer.getLayout();
        cl.show(rightContainer, "SIGNIN");
    }


}