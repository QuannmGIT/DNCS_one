package StoreManagement.component;

import StoreManagement.page.SignInPanel;

import javax.swing.*;
import java.awt.*;

public class ManageFrame extends JPanel {
    private String loggedInUser;
    private JPanel bodyPanel;    

    public ManageFrame(String username){
        this.loggedInUser = username; 
        this.setSize(1200, 600);
    }

    public String getLoggedInUser() {
        return this.loggedInUser;
    }


}