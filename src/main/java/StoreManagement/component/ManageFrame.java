package StoreManagement.component;

import StoreManagement.login.SignInPanel;

import javax.swing.*;
import java.awt.*;

public class ManageFrame extends JFrame {
    private String loggedInUser;
    private JPanel bodyPanel;    

    public ManageFrame(String username){
        this.loggedInUser = username; 
        this.setTitle("Hanabi Cafe - Xin chào " + username);
        this.setSize(1200, 600);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/img/HanabiIcon.png"));
        this.setIconImage(icon.getImage());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new BorderLayout());

        MenuPanel menuPanel = new MenuPanel(this);   
        
        mainContainer.add(menuPanel, BorderLayout.WEST);

        bodyPanel = new JPanel(new BorderLayout());
        mainContainer.add(bodyPanel, BorderLayout.CENTER);

        showPersonPanel();

        this.add(mainContainer);
    }

    public String getLoggedInUser() {
        return this.loggedInUser;
    }

    public void showSignInPanel() {
        bodyPanel.removeAll();
        
        SignInPanel signIn = new SignInPanel(this);
        
        bodyPanel.add(signIn, BorderLayout.CENTER);
        bodyPanel.revalidate(); 
        bodyPanel.repaint();
    }

    public void showPersonPanel() {
        bodyPanel.removeAll(); 
        
        PersonPanel person = new PersonPanel(this, this.loggedInUser);
        
        bodyPanel.add(person, BorderLayout.CENTER); 
        bodyPanel.revalidate(); 
        bodyPanel.repaint();
    }
    
    public void showSalePanel() {
    bodyPanel.removeAll();
    

    SalePanel salePanel = new SalePanel(this);
    
    bodyPanel.add(salePanel, BorderLayout.CENTER);
    bodyPanel.revalidate();
    bodyPanel.repaint();
    }
    
    public void showChangePasswordPanel() {
    bodyPanel.removeAll();
    

    ChangePasswordPanel changePass = new ChangePasswordPanel(this, this.loggedInUser);
    
    bodyPanel.add(changePass, BorderLayout.CENTER);
    bodyPanel.revalidate();
    bodyPanel.repaint();
    }

    public void showProductPanel() {
    bodyPanel.removeAll();
    

    ProductPanel productPanel = new ProductPanel(this);
    
    bodyPanel.add(productPanel, BorderLayout.CENTER);
    bodyPanel.revalidate();
    bodyPanel.repaint();
    }
}