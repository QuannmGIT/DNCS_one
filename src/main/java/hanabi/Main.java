package hanabi;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import hanabi.components.MainForm;
import hanabi.view.Auth.Banner;
import hanabi.view.Auth.LoginPanel;
import hanabi.view.ui.DashboardView;
import hanabi.service.AuthService;
import hanabi.util.TenantContext;

public class Main extends JFrame {
    private static Main app;
    public static final AuthService authService = new AuthService();
    private final MainForm mainForm;
    private static Banner banner;
    private static LoginPanel loginPanel;
    private final DashboardView Dash;
    private final JPanel loginPane;

    public Main() {
        app = this;
        init();
        mainForm = new MainForm();
        banner = new Banner();
        loginPanel = new LoginPanel();
        loginPane = new JPanel(new BorderLayout());
        Dash = new DashboardView();
        loginPanel.init(this);
        loginPane.add(banner, BorderLayout.WEST);
        loginPane.add(loginPanel, BorderLayout.CENTER);

        setContentPane(loginPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static Main getInstance() {
        return app;
    }

    private void init() {
        FlatLightLaf.setup();
        setTitle("HANABI CAFE - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
    }

    public static JFrame getFrame() {
        return app;
    }

    // show form
    public static void showForm(java.awt.Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }

    public static void login() {
        FlatAnimatedLafChange.showSnapshot();
        app.Dash.refreshSidebar();
        app.Dash.refreshData();
        app.setContentPane(app.Dash);
        app.setTitle(authService.isDevUser() ? "HANABI CAFE - Developer Mode" : "HANABI CAFE");
        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.Dash.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.Dash);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        authService.logout();
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginPane);
        app.setExtendedState(JFrame.NORMAL);
        app.setTitle("HANABI CAFE - Login");
        app.loginPane.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginPane);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

}
