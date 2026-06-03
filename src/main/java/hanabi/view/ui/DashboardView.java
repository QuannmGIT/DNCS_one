package hanabi.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import hanabi.Main;
import hanabi.service.CreateTenantForm;

public class DashboardView extends JPanel {

    private static final Color LIGHT_BG = new Color(250, 248, 245);

    private static final String PANEL_MENU = "menu";
    private static final String PANEL_ACCOUNTS = "accounts";
    private static final String PANEL_REVENUE = "revenue";
    private static final String PANEL_ORDERS = "orders";
    private static final String PANEL_CHAT = "chat";
    private static final String PANEL_CREATE_CAFE = "createCafe";

    private MenuItemsPanel menuItemsPanel;
    private AccountPanel accountPanel;
    private RevenuePanel revenuePanel;
    private OrdersPanel ordersPanel;
    private ChatPanel chatPanel;
    private CreateCafePanel createCafePanel;
    private CategoryPanel categoryPanel;
    private final JPanel centerPanel;
    private final CardLayout cardLayout;

    public DashboardView() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.setBackground(Color.WHITE);
        init();
    }

    public void refreshData() {
        if (Main.authService.isDevUser()) return;
        menuItemsPanel.loadMenuItems();
        accountPanel.loadUser();
        revenuePanel.loadData();
    }

    public void refreshSidebar() {
        if (categoryPanel != null) {
            categoryPanel.refreshForUser();
        }
        if (Main.authService.isDevUser()) {
            cardLayout.show(centerPanel, PANEL_CREATE_CAFE);
            createCafePanel.refresh();
        } else {
            cardLayout.show(centerPanel, PANEL_MENU);
            refreshData();
        }
    }

    private void init() {
        menuItemsPanel = new MenuItemsPanel();
        accountPanel = new AccountPanel();

        revenuePanel = new RevenuePanel();
        revenuePanel.setBackground(Color.WHITE);

        ordersPanel = new OrdersPanel();

        chatPanel = new ChatPanel();

        createCafePanel = new CreateCafePanel(() -> {
            CreateTenantForm.show(() -> {
                createCafePanel.refresh();
            });
        });

        centerPanel.add(menuItemsPanel, PANEL_MENU);
        centerPanel.add(accountPanel, PANEL_ACCOUNTS);
        centerPanel.add(revenuePanel, PANEL_REVENUE);
        centerPanel.add(ordersPanel, PANEL_ORDERS);
        centerPanel.add(chatPanel, PANEL_CHAT);
        centerPanel.add(createCafePanel, PANEL_CREATE_CAFE);

        categoryPanel = new CategoryPanel(page -> {
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
                case CategoryPanel.PAGE_ORDERS:
                    target = PANEL_ORDERS;
                    ordersPanel.loadData();
                    break;
                case CategoryPanel.PAGE_CHAT:
                    target = PANEL_CHAT;
                    chatPanel.loadContacts();
                    break;
                case CategoryPanel.PAGE_CREATE_CAFE:
                    target = PANEL_CREATE_CAFE;
                    createCafePanel.refresh();
                    break;
                default:
                    return;
            }
            cardLayout.show(centerPanel, target);
        });

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(LIGHT_BG);
        content.add(categoryPanel, BorderLayout.WEST);
        content.add(centerPanel, BorderLayout.CENTER);

        add(content);
        setVisible(true);
    }

}
