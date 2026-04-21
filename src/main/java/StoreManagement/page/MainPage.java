package StoreManagement.page;

import StoreManagement.component.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JPanel {
    private static MainPage instance;
    private JPanel bodyPanel;

    public MainPage() {
        init();
        initComponent();
    }

    private void init(){
        setLayout(new MigLayout(
                "fill, insets 1 1 1 1",
                "fill,250:280"
        ));

        instance = this;

    }

    private void initComponent(){
        JPanel mainContainer = new JPanel(new BorderLayout());

        MenuPanel menuPanel = new MenuPanel();

        mainContainer.add(menuPanel, BorderLayout.WEST);

        bodyPanel = new JPanel(new BorderLayout());
        mainContainer.add(bodyPanel, BorderLayout.CENTER);

        showPersonPanel();

        this.add(mainContainer);
    }

    public void showSignInPanel() {
        bodyPanel.removeAll();

        SignInPanel signIn = new SignInPanel();

        bodyPanel.add(signIn, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public void showPersonPanel() {
        bodyPanel.removeAll();

        PersonPanel person = new PersonPanel("");

        bodyPanel.add(person, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public void showSalePanel() {
        bodyPanel.removeAll();


        SalePanel salePanel = new SalePanel();

        bodyPanel.add(salePanel, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public void showChangePasswordPanel() {
        bodyPanel.removeAll();


        ChangePasswordPanel changePass = new ChangePasswordPanel("");

        bodyPanel.add(changePass, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public void showProductPanel() {
        bodyPanel.removeAll();


        ProductPanel productPanel = new ProductPanel();

        bodyPanel.add(productPanel, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    public static MainPage getInstance(){
        return instance;
    }
}