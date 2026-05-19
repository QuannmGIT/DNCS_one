package com.hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.hanabi.util.FontLoader;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccountPanel extends JPanel {

    public static final int PANEL_WIDTH = 900;
    public static final int PANEL_HEIGHT = 700;

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);
    private static final Color CARD_BG = new Color(253, 251, 249);
    private static final Color INFO_CARD_BG = new Color(211, 181, 147);
    private static final Color STATS_BG = new Color(248, 248, 248);
    private static final Color AVATAR_BG = new Color(239, 223, 204);

    private Font amaticFont;

    private JLabel labelUsername;
    private JLabel labelFullname;
    private JLabel labelEmail;
    private JLabel labelOrdersValue;
    private JLabel labelPointsValue;

    public AccountPanel() {
        loadFont();
        initComponents();
    }

    private void loadFont() {
        try {
            amaticFont = FontLoader.load(
                "/com/hanabi/resources/StoreManagement/assets/Fonts/AmaticSC-Regular.ttf", 48f);
        } catch (Exception e) {
            amaticFont = new Font("Segoe UI", Font.BOLD, 42);
        }
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // ─── HEADER ─────────────────────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        header.setOpaque(false);

        JLabel brand = new JLabel("HANABI CAFE");
        brand.setFont(amaticFont);
        brand.setForeground(DARK_BROWN);

        JLabel icon = new JLabel();
        try {
            ImageIcon img = new ImageIcon(AccountPanel.class.getResource(
                "/com/hanabi/resources/StoreManagement/assets/icon/HanabiCafe.png"));
            icon.setIcon(new ImageIcon(img.getImage().getScaledInstance(52, 42, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {}
        header.add(brand);
        header.add(icon);

        // ─── TITLE ──────────────────────────────────────────────
        JLabel title = new JLabel("Account Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(DARK_BROWN);

        // ─── INFO CARD ──────────────────────────────────────────
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(INFO_CARD_BG);
        infoCard.putClientProperty(FlatClientProperties.STYLE,
            "arc:20; border: 1,1,1,1, #5A463D");

        JPanel avatar = new JPanel(new GridBagLayout());
        avatar.setBackground(AVATAR_BG);
        avatar.setPreferredSize(new Dimension(130, 130));
        avatar.putClientProperty(FlatClientProperties.STYLE,
            "arc:22; borderWidth:0; focusWidth:0");
        JLabel avatarIcon = new JLabel(
            new FlatSVGIcon("com/hanabi/resources/StoreManagement/assets/icon/AccountIconLight.svg", 70, 70));
        avatar.add(avatarIcon);

        labelUsername = new JLabel("Username...");
        labelUsername.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelUsername.setForeground(TEXT_MENU);

        labelFullname = new JLabel("Fullname...");
        labelFullname.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelFullname.setForeground(TEXT_MENU);

        labelEmail = new JLabel("Email...");
        labelEmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        labelEmail.setForeground(TEXT_MENU);

        GridBagConstraints cg = new GridBagConstraints();
        cg.gridx = 0; cg.gridy = 0; cg.gridheight = 3;
        cg.insets = new Insets(24, 24, 24, 18);
        cg.anchor = GridBagConstraints.CENTER;
        infoCard.add(avatar, cg);

        cg.gridx = 1; cg.gridy = 0; cg.gridheight = 1;
        cg.insets = new Insets(28, 5, 6, 36);
        cg.anchor = GridBagConstraints.WEST;
        infoCard.add(labelUsername, cg);

        cg.gridy = 1;
        cg.insets = new Insets(6, 5, 6, 36);
        infoCard.add(labelFullname, cg);

        cg.gridy = 2;
        cg.insets = new Insets(6, 5, 28, 36);
        infoCard.add(labelEmail, cg);

        // ─── BUTTONS ────────────────────────────────────────────
        JButton btnChangePwd = createBtn("Change Password");
        JButton btnAddUser = createBtn("Add User");
        JButton btnTerminate = createBtn("Terminate");

        btnAddUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this,
                "Enter new username:", "Add User", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "User \"" + name.trim() + "\" added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnTerminate.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to terminate this account?\nThis action cannot be undone.",
                "Terminate Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "Account has been terminated.",
                    "Terminated", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        GridBagConstraints bg = new GridBagConstraints();
        bg.gridx = 0; bg.gridy = 0;
        bg.fill = GridBagConstraints.HORIZONTAL;
        bg.insets = new Insets(0, 0, 14, 0);
        btnPanel.add(btnChangePwd, bg);
        bg.gridy = 1;
        btnPanel.add(btnAddUser, bg);
        bg.gridy = 2;
        bg.insets = new Insets(0, 0, 0, 0);
        btnPanel.add(btnTerminate, bg);

        // ─── STATS ──────────────────────────────────────────────
        labelOrdersValue = new JLabel("0");
        JPanel panelTotalOrders = createStatPanel(
            new FlatSVGIcon("com/hanabi/resources/StoreManagement/assets/icon/TotalOrderIcon.svg", 28, 28),
            "Total Orders", labelOrdersValue);

        labelPointsValue = new JLabel("0");
        JPanel panelPoints = createStatPanel(
            new FlatSVGIcon("com/hanabi/resources/StoreManagement/assets/icon/PointIcon.svg", 28, 28),
            "Points", labelPointsValue);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(panelTotalOrders);
        statsPanel.add(panelPoints);

        // ─── LAYOUT ─────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(header, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 12, 18, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(title, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 12, 20, 18);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(infoCard, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 12);
        gbc.anchor = GridBagConstraints.NORTH;
        add(btnPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 20, 12);
        gbc.anchor = GridBagConstraints.CENTER;
        add(statsPanel, gbc);
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(170, 48));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(INFO_CARD_BG);
        btn.setForeground(TEXT_MENU);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
            "arc:16; borderWidth:0; focusWidth:0; innerFocusWidth:0;" +
            "pressedBackground:#A88A68");
        btn.putClientProperty("JButton.hoverBackground", new Color(191, 161, 127));
        return btn;
    }

    private JPanel createStatPanel(Icon icon, String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(STATS_BG);
        panel.putClientProperty(FlatClientProperties.STYLE,
            "arc:16; border: 1,1,1,1, #5A463D");

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        titleRow.setOpaque(false);
        JLabel iconLbl = new JLabel(icon);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLbl.setForeground(DARK_BROWN);
        titleRow.add(iconLbl);
        titleRow.add(titleLbl);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(DARK_BROWN);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.insets = new Insets(22, 10, 8, 10);
        g.anchor = GridBagConstraints.CENTER;
        panel.add(titleRow, g);

        g.gridy = 1;
        g.insets = new Insets(8, 10, 26, 10);
        panel.add(valueLabel, g);

        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Account Panel Demo");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().add(new AccountPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
