package com.hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardView {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);

    private static final String PANEL_MENU = "menu";
    private static final String PANEL_ACCOUNTS = "accounts";
    private static final String PANEL_REVENUE = "revenue";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to set FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("HANABI CAFE");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);

            CardLayout cardLayout = new CardLayout();
            JPanel centerPanel = new JPanel(cardLayout);
            centerPanel.setBackground(Color.WHITE);

            MenuItemsPanel menuItemsPanel = new MenuItemsPanel();
            AccountPanel accountPanel = new AccountPanel();

            RevenuePanel revenuePanel = new RevenuePanel();
            revenuePanel.setBackground(Color.WHITE);

            centerPanel.add(menuItemsPanel, PANEL_MENU);
            centerPanel.add(accountPanel, PANEL_ACCOUNTS);
            centerPanel.add(revenuePanel, PANEL_REVENUE);

            CategoryPanel categoryPanel = new CategoryPanel(page -> {
                String target;
                switch (page) {
                    case CategoryPanel.PAGE_MENU_ITEMS: target = PANEL_MENU; break;
                    case CategoryPanel.PAGE_ACCOUNTS: target = PANEL_ACCOUNTS; break;
                    case CategoryPanel.PAGE_REVENUE: target = PANEL_REVENUE; break;
                    default: return;
                }
                cardLayout.show(centerPanel, target);
            });
            categoryPanel.setActivePage(CategoryPanel.PAGE_MENU_ITEMS);

            JPanel content = new JPanel(new BorderLayout(0, 0));
            content.setBackground(LIGHT_BG);
            content.add(categoryPanel, BorderLayout.WEST);
            content.add(centerPanel, BorderLayout.CENTER);

            frame.getContentPane().add(content);
            frame.setSize(1320, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
