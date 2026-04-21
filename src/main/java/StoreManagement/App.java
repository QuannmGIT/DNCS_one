package StoreManagement;

import StoreManagement.component.BannerPanel;
import StoreManagement.page.LoginPanel;
import StoreManagement.page.MainPage;
import StoreManagement.page.SignInPanel;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class App extends JFrame {
    private JPanel rightContainer;
    private final SignInPanel signInForm;
    private final LoginPanel loginForm;
    private MainPage MainPage;
    private JPanel Container;
    public static App app;

    public App() {
        init();
        loginForm = new LoginPanel();
        signInForm = new SignInPanel();
        MainPage = new MainPage();
        setContentPane(loginForm);

        app = this;
    }

    public void init(){
        FlatLaf.registerCustomDefaultsSource("StoreManagement.themes");
        FlatMacLightLaf.setup();
        setTitle("Cafe Management system");
        setSize(1000, 600);
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/StoreManagement/assets/img/HanabiIcon.png"))); //deprecated icon
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        Notifications.getInstance().setJFrame(this);

        // Initialize Hibernate
        SessionFactory sessionFactory = new Configuration().configure("backend/hibernate.cfg.xml").buildSessionFactory();

    }

    public void initComponent(){
//        Container = new JPanel();
//        Container.setLayout(new BorderLayout());
//        add(Container);
//
//        BannerPanel banner = new BannerPanel();
//        Container.add(banner, BorderLayout.WEST);
//
//        rightContainer = new JPanel();
//        rightContainer.setLayout(new CardLayout());
//        rightContainer.add(loginForm, "LOGIN");
//
//        Container.add(rightContainer, BorderLayout.CENTER);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.showForm(component);
    }

    public static void login(){
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.MainPage);
        app.MainPage.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.MainPage);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginForm);
        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void signup() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.signInForm);
        app.signInForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.signInForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static App getInstance(){
       if (app != null){
        return app;
       }
        return new App();
    }

    static void main() {
        java.awt.EventQueue.invokeLater(() -> {
            app = new App();
            app.setVisible(true);
        });
    }
}