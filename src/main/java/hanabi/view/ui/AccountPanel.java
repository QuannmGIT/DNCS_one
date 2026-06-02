package hanabi.view.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.components.SalaryTablePanel;
import hanabi.model.Staff;
import hanabi.model.User;
import hanabi.service.AccountService;
import hanabi.service.CreateUser;
import hanabi.util.FontLoader;
import java.util.List;
import java.util.UUID;

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

    private final AccountService accountService = new AccountService();
    private JLabel labelUsername;
    private JLabel labelFullname;
    private JLabel labelEmail;
    private JLabel labelOrdersValue;
    private JLabel labelPointsValue;
    private JLabel brand, icon, title;
    private JButton btnChangePass, btnAddUser, btnTerminate;
    private SalaryTablePanel salaryPanel;
    private JPanel panelTotalOrders, panelPoints, topWrapper, header, titleUnderline, titleWrapper,
            infoCard, avatar, textPanel, row0, row1, row2, btnPanel, statsPanel, salarySubPanel;
    private GridBagConstraints gbc;
    private GridBagConstraints tp;
    private GridBagConstraints cg;

    private GridBagConstraints bg;
    private JLabel salaryLb;

    public AccountPanel() {
        loadFont();
        initComponents();
        initlayout();
    }

    public void loadUser() {
        User u = hanabi.Main.authService.getCurrentUser();
        if (u == null)
            return;

        if (!u.isAdmin()) {
            btnTerminate.setVisible(false);
            btnAddUser.setVisible(false);
            salaryPanel.setVisible(false);
            panelPoints.setVisible(true);
        } else {
            btnTerminate.setVisible(true);
            btnAddUser.setVisible(true);
            salaryPanel.setVisible(true);
            panelPoints.setVisible(true);
        }

        labelUsername.setText(u.getStaffName() != null ? u.getStaffName() : "");
        labelFullname.setText(u.getFullName() != null ? u.getFullName() : "");
        labelEmail.setText(u.getEmail() != null ? u.getEmail() : "");

        labelOrdersValue.setText("...");
        labelPointsValue.setText("...");
        salaryPanel.clearData();

        UUID staffId = u.getStaffId();
        new SwingWorker<Void, Void>() {
            private long orders;
            private Integer points;
            private List<Object[]> salaryData;

            private Double salaryTotal;

            @Override
            protected Void doInBackground() {
                orders = accountService.getTotalOrders(staffId);
                // points = accountService.getPoints(staffId);
                salaryData = accountService.getSalaryData();
                salaryTotal = accountService.getSalaryTotal(staffId);
                return null;
            }

            @Override
            protected void done() {
                labelOrdersValue.setText(String.valueOf(orders));
                labelPointsValue.setText(points != null ? String.valueOf(points) : "0");
                salaryLb.setText(salaryTotal != null ? formatSalary(salaryTotal) : "0 VND");
                salaryPanel.loadSalaryData(salaryData);
            }
        }.execute();
    }

    private void loadFont() {
        try {
            amaticFont = FontLoader.load(
                    "/hanabi/assets/Fonts/AmaticSC-Regular.ttf", 48f);
        } catch (Exception e) {
            amaticFont = new Font("Segoe UI", Font.BOLD, 42);
        }
    }

    private void initComponents() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(new EmptyBorder(18, 30, 20, 30));
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

        HEADER();
        TITLE();
        INFOCARD();
        STATS();
        BUTTONS();

    }

    private void initlayout() {
        // ─── LAYOUT ─────────────────────────────────────────────
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(header, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 12, 18, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(titleWrapper, gbc);

        // --- WRAPPER: INFO CARD + BUTTONS ---
        topWrapper = new JPanel(new GridBagLayout());
        topWrapper.setOpaque(false);
        GridBagConstraints tw = new GridBagConstraints();
        tw.gridx = 0;
        tw.gridy = 0;
        tw.anchor = GridBagConstraints.WEST;
        tw.insets = new Insets(0, 0, 0, 30);
        topWrapper.add(infoCard, tw);
        tw.gridx = 1;
        tw.insets = new Insets(0, 0, 0, 0);
        tw.fill = GridBagConstraints.VERTICAL;
        tw.weighty = 1.0;
        topWrapper.add(btnPanel, tw);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 12, 20, 0);
        gbc.anchor = GridBagConstraints.WEST;
        add(topWrapper, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 12, 12);
        gbc.anchor = GridBagConstraints.CENTER;
        add(statsPanel, gbc);

        salaryPanel = new SalaryTablePanel();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 12, 8, 12);
        gbc.anchor = GridBagConstraints.CENTER;
        add(salaryPanel, gbc);
    }

    private void HEADER() {
        // ─── HEADER ─────────────────────────────────────────────
        header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        header.setOpaque(false);

        brand = new JLabel("HANABI CAFE");
        brand.setFont(amaticFont);
        brand.setForeground(DARK_BROWN);

        icon = new JLabel();
        try {
            ImageIcon img = new ImageIcon(AccountPanel.class.getResource(
                    "/hanabi/assets/icon/HanabiCafe.png"));
            icon.setIcon(new ImageIcon(img.getImage().getScaledInstance(52, 42, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }
        header.add(brand);
        header.add(icon);
    }

    private void TITLE() {
        // ─── TITLE ──────────────────────────────────────────────
        title = new JLabel("Account Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(DARK_BROWN);

        titleUnderline = new JPanel();
        titleUnderline.setBackground(DARK_BROWN);
        titleUnderline.setPreferredSize(new Dimension(180, 3));
        titleUnderline.setMaximumSize(new Dimension(180, 3));

        titleWrapper = new JPanel(new BorderLayout(0, 6));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title, BorderLayout.NORTH);
        titleWrapper.add(titleUnderline, BorderLayout.SOUTH);
    }

    private void INFOCARD() {
        // ─── INFO CARD ──────────────────────────────────────────
        infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(INFO_CARD_BG);
        infoCard.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; border: 1,1,1,1, #5A463D");

        avatar = new JPanel(new GridBagLayout());
        avatar.setBackground(AVATAR_BG);
        avatar.setPreferredSize(new Dimension(130, 130));
        avatar.putClientProperty(FlatClientProperties.STYLE,
                "arc:22;");
        // borderWidth:0; focusWidth:0
        JLabel avatarIcon = new JLabel(
                new FlatSVGIcon("hanabi/assets/icon/AccountIconLight.svg", 70, 70));
        avatar.add(avatarIcon);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 18);
        Color labelColor = new Color(120, 95, 82);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(labelFont);
        lblUser.setForeground(labelColor);
        labelUsername = new JLabel("username...");
        labelUsername.setFont(valueFont);
        labelUsername.setForeground(TEXT_MENU);

        JLabel lblFull = new JLabel("Fullname:");
        lblFull.setFont(labelFont);
        lblFull.setForeground(labelColor);
        labelFullname = new JLabel("fullname...");
        labelFullname.setFont(valueFont);
        labelFullname.setForeground(TEXT_MENU);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);
        labelEmail = new JLabel("email...");
        labelEmail.setFont(valueFont);
        labelEmail.setForeground(TEXT_MENU);

        textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);
        tp = new GridBagConstraints();
        tp.gridx = 0;
        tp.gridy = 0;
        tp.anchor = GridBagConstraints.WEST;
        tp.insets = new Insets(0, 0, 10, 0);
        row0 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row0.setOpaque(false);
        row0.add(lblUser);
        row0.add(labelUsername);
        textPanel.add(row0, tp);
        tp.gridy = 1;
        tp.insets = new Insets(0, 0, 10, 0);
        row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row1.setOpaque(false);
        row1.add(lblFull);
        row1.add(labelFullname);
        textPanel.add(row1, tp);
        tp.gridy = 2;
        tp.insets = new Insets(0, 0, 0, 0);
        row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row2.setOpaque(false);
        row2.add(lblEmail);
        row2.add(labelEmail);
        textPanel.add(row2, tp);

        cg = new GridBagConstraints();
        cg.gridx = 0;
        cg.gridy = 0;
        cg.gridheight = 1;
        cg.insets = new Insets(20, 18, 20, 10);
        cg.anchor = GridBagConstraints.CENTER;
        infoCard.add(avatar, cg);

        cg.gridx = 1;
        cg.gridy = 0;
        cg.insets = new Insets(20, 0, 20, 24);
        cg.anchor = GridBagConstraints.WEST;
        cg.fill = GridBagConstraints.NONE;
        infoCard.add(textPanel, cg);
    }

    private void BUTTONS() {
        // ─── BUTTONS ────────────────────────────────────────────
        btnChangePass = createBtn("Change Password");

        btnChangePass.addActionListener(e -> {
            String newPass = JOptionPane.showInputDialog(this,
                    "Enter new password:", "Change Password", JOptionPane.PLAIN_MESSAGE);
            ChangePassword(newPass);
        });
        btnAddUser = createBtn("Add User");
        btnTerminate = createBtn("Terminate");

        btnAddUser.addActionListener(e -> {
            CreateUser.init();
            loadUser();
        });

        btnTerminate.addActionListener(e -> {
            TerminateUser();
        });

        btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        bg = new GridBagConstraints();
        bg.gridx = 0;
        bg.gridy = 0;
        bg.fill = GridBagConstraints.HORIZONTAL;
        bg.weightx = 1.0;
        bg.insets = new Insets(0, 0, 10, 0);
        btnPanel.add(btnChangePass, bg);
        bg.gridy = 1;
        btnPanel.add(btnAddUser, bg);
        bg.gridy = 2;
        bg.insets = new Insets(0, 0, 0, 0);
        btnPanel.add(btnTerminate, bg);
    }

    private void STATS() {
        // ─── STATS ──────────────────────────────────────────────
        labelOrdersValue = new JLabel("0");
        panelTotalOrders = createStatPanel(
                new FlatSVGIcon("hanabi/assets/icon/TotalOrderIcon.svg", 28, 28),
                "Total Orders", labelOrdersValue);

        labelPointsValue = new JLabel("0");
        panelPoints = createStatPanel(
                new FlatSVGIcon("hanabi/assets/icon/PointIcon.svg", 28, 28),
                "Points", labelPointsValue);
        salaryLb = new JLabel("0");
        salarySubPanel = createStatPanel(new FlatSVGIcon("hanabi/assets/icon/salary.svg", 28, 28), "salary", salaryLb);

        statsPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(panelTotalOrders);
        statsPanel.add(panelPoints);
        statsPanel.add(salarySubPanel);
    }

    private JButton createBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(DARK_BROWN);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:10; borderWidth:0; focusWidth:0; innerFocusWidth:0;" +
                        "pressedBackground:#5C3D2E");
        btn.putClientProperty("JButton.hoverBackground", new Color(110, 85, 75));
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
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(new Color(120, 95, 82));
        titleRow.add(iconLbl);
        titleRow.add(titleLbl);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        valueLabel.setForeground(DARK_BROWN);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(18, 10, 6, 10);
        g.anchor = GridBagConstraints.CENTER;
        panel.add(titleRow, g);

        g.gridy = 1;
        g.insets = new Insets(6, 10, 22, 10);
        panel.add(valueLabel, g);

        return panel;
    }

    private String formatSalary(Double amount) {
        if (amount == null)
            return "0 VND";
        long vnd = amount.longValue();
        if (vnd >= 1_000_000_000_000L) {
            return String.format("%.1fT VND", Math.floor(vnd / 1_000_000_000_000.0));
        } else if (vnd >= 1_000_000_000) {
            return String.format("%.1fB VND", Math.floor(vnd / 1_000_000_000.0));
        } else if (vnd >= 1_000_000) {
            return String.format("%.1fM VND", Math.floor(vnd / 1_000_000.0));
        } else {
            return String.format("%.1fK VND", Math.floor(vnd / 1_000.0));
        }
    }

    private void ChangePassword(String newPass) {
        if (newPass != null && !newPass.trim().isEmpty()) {
            User u = hanabi.Main.authService.getCurrentUser();
            if (u != null) {
                accountService.changePassword(u.getStaffId(), newPass.trim());
                JOptionPane.showMessageDialog(this,
                        "Password changed successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void TerminateUser() {
        String name = JOptionPane.showInputDialog(this,
                "Enter username to terminate:", "Terminate Account", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            Staff s = accountService.getStaffByName(name.trim());
            if (s != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to terminate \"" + name.trim()
                                + "\"?\nThis action cannot be undone.",
                        "Terminate Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    accountService.terminateStaff(s.getStaffId());
                    JOptionPane.showMessageDialog(this,
                            "Account \"" + name.trim() + "\" has been terminated.",
                            "Terminated", JOptionPane.INFORMATION_MESSAGE);
                    loadUser();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "User \"" + name.trim() + "\" not found!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}