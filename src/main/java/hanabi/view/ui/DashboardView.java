package hanabi.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import raven.crazypanel.CrazyPanel;

public class DashboardView extends CrazyPanel {

    private static final Color LIGHT_BG = new Color(250, 248, 245);

    private static final String PANEL_MENU = "menu";
    private static final String PANEL_ACCOUNTS = "accounts";
    private static final String PANEL_REVENUE = "revenue";
    private static final String PANEL_ORDERS = "orders";
    private static final String PANEL_CHAT = "chat";

    private MenuItemsPanel menuItemsPanel;
    private AccountPanel accountPanel;
    private RevenuePanel revenuePanel;
    private OrdersPanel ordersPanel;
    private ChatPanel chatPanel;
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
        menuItemsPanel.loadMenuItems();
        accountPanel.loadUser();
        revenuePanel.loadData();
    }

    private void init() {
        menuItemsPanel = new MenuItemsPanel();
        accountPanel = new AccountPanel();

        revenuePanel = new RevenuePanel();
        revenuePanel.setBackground(Color.WHITE);

        ordersPanel = new OrdersPanel();

        chatPanel = new ChatPanel();

        centerPanel.add(menuItemsPanel, PANEL_MENU);
        centerPanel.add(accountPanel, PANEL_ACCOUNTS);
        centerPanel.add(revenuePanel, PANEL_REVENUE);
        centerPanel.add(ordersPanel, PANEL_ORDERS);
        centerPanel.add(chatPanel, PANEL_CHAT);

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
                case CategoryPanel.PAGE_ORDERS:
                    target = PANEL_ORDERS;
                    ordersPanel.loadData();
                    break;
                case CategoryPanel.PAGE_CHAT:
                    target = PANEL_CHAT;
                    chatPanel.loadContacts();
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
        setVisible(true);
    }


}
