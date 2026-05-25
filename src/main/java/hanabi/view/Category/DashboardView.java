package hanabi.view.Category;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import raven.crazypanel.CrazyPanel;

public class DashboardView extends CrazyPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);

    private static final String PANEL_MENU = "menu";
    private static final String PANEL_ACCOUNTS = "accounts";
    private static final String PANEL_REVENUE = "revenue";

    private MenuItemsPanel menuItemsPanel;
    private AccountPanel accountPanel;
    private RevenuePanel revenuePanel;

    public DashboardView() {
        setLayout(new BorderLayout());
        init();
    }

    public void refreshData() {
        menuItemsPanel.loadMenuItems();
        accountPanel.loadUser();
        revenuePanel.loadData();
    }

    private void init() {
        CardLayout cardLayout = new CardLayout();
        JPanel centerPanel = new JPanel(cardLayout);
        centerPanel.setBackground(Color.WHITE);

        menuItemsPanel = new MenuItemsPanel();
        accountPanel = new AccountPanel();

        revenuePanel = new RevenuePanel();
        revenuePanel.setBackground(Color.WHITE);

        centerPanel.add(menuItemsPanel, PANEL_MENU);
        centerPanel.add(accountPanel, PANEL_ACCOUNTS);
        centerPanel.add(revenuePanel, PANEL_REVENUE);

        CategoryPanel categoryPanel = new CategoryPanel(page -> {
            String target;
            switch (page) {
                case CategoryPanel.PAGE_MENU_ITEMS:
                    target = PANEL_MENU;
                    refreshData();
                    break;
                case CategoryPanel.PAGE_ACCOUNTS:
                    target = PANEL_ACCOUNTS;
                    refreshData();
                    break;
                case CategoryPanel.PAGE_REVENUE:
                    target = PANEL_REVENUE;
                    refreshData();
                    break;
                default:
                    return;
            }
            cardLayout.show(centerPanel, target);
        });
        categoryPanel.setActivePage(CategoryPanel.PAGE_MENU_ITEMS);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(LIGHT_BG);
        content.add(categoryPanel, BorderLayout.WEST);
        content.add(centerPanel, BorderLayout.CENTER);

        add(content);
        // setSize(1320, 800);
        setVisible(true);

    }

    public static void main(String[] args) {
        // try {
        // UIManager.setLookAndFeel(new FlatLightLaf());
        // } catch (Exception e) {
        // System.err.println("Failed to set FlatLaf: " + e.getMessage());
        // }

        // SwingUtilities.invokeLater(() -> {
        // JFrame frame = new JFrame("HANABI CAFE");
        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // frame.setResizable(false);

        // });
    }
}
