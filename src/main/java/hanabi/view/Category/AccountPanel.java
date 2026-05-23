package hanabi.view.Category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.util.FontLoader;

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
                    "/hanabi/assets/icon/HanabiCafe.png"));
            icon.setIcon(new ImageIcon(img.getImage().getScaledInstance(52, 42, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {
        }
        header.add(brand);
        header.add(icon);

        // ─── TITLE ──────────────────────────────────────────────
        JLabel title = new JLabel("Account Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(DARK_BROWN);

        JPanel titleUnderline = new JPanel();
        titleUnderline.setBackground(DARK_BROWN);
        titleUnderline.setPreferredSize(new Dimension(180, 3));
        titleUnderline.setMaximumSize(new Dimension(180, 3));

        JPanel titleWrapper = new JPanel(new BorderLayout(0, 6));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title, BorderLayout.NORTH);
        titleWrapper.add(titleUnderline, BorderLayout.SOUTH);

        // ─── INFO CARD ──────────────────────────────────────────
        JPanel infoCard = new JPanel(new GridBagLayout());
        infoCard.setBackground(INFO_CARD_BG);
        infoCard.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; border: 1,1,1,1, #5A463D");

        JPanel avatar = new JPanel(new GridBagLayout());
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

        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setOpaque(false);
        GridBagConstraints tp = new GridBagConstraints();
        tp.gridx = 0;
        tp.gridy = 0;
        tp.anchor = GridBagConstraints.WEST;
        tp.insets = new Insets(0, 0, 10, 0);
        JPanel row0 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row0.setOpaque(false);
        row0.add(lblUser);
        row0.add(labelUsername);
        textPanel.add(row0, tp);
        tp.gridy = 1;
        tp.insets = new Insets(0, 0, 10, 0);
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row1.setOpaque(false);
        row1.add(lblFull);
        row1.add(labelFullname);
        textPanel.add(row1, tp);
        tp.gridy = 2;
        tp.insets = new Insets(0, 0, 0, 0);
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row2.setOpaque(false);
        row2.add(lblEmail);
        row2.add(labelEmail);
        textPanel.add(row2, tp);

        GridBagConstraints cg = new GridBagConstraints();
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

        // ─── BUTTONS ────────────────────────────────────────────
        JButton btnChangePass = createBtn("Change Password");
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

        // ─── STATS ──────────────────────────────────────────────
        labelOrdersValue = new JLabel("0");
        JPanel panelTotalOrders = createStatPanel(
                new FlatSVGIcon("hanabi/assets/icon/TotalOrderIcon.svg", 28, 28),
                "Total Orders", labelOrdersValue);

        labelPointsValue = new JLabel("0");
        JPanel panelPoints = createStatPanel(
                new FlatSVGIcon("hanabi/assets/icon/PointIcon.svg", 28, 28),
                "Points", labelPointsValue);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(panelTotalOrders);
        statsPanel.add(panelPoints);

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
        JPanel topWrapper = new JPanel(new GridBagLayout());
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

        JPanel salaryPanel = createSalaryTable();
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

    private JPanel createSalaryTable() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // Title
        JLabel title = new JLabel("Bảng lương nhân viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(DARK_BROWN);

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.add(title, BorderLayout.WEST);
        JLabel countLabel = new JLabel("6 nhân viên");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        countLabel.setForeground(new Color(160, 140, 125));
        titleRow.add(countLabel, BorderLayout.EAST);

        // Data
        String[] cols = { "STT", "Họ tên", "Chức vụ", "Lương cơ bản", "Phụ cấp", "Tổng lương" };
        Object[][] data = {
                { 1, "Nguyễn Văn An", "Barista", "8.000.000đ", "1.200.000đ", "9.200.000đ" },
                { 2, "Trần Thị Bình", "Phục vụ", "6.500.000đ", "800.000đ", "7.300.000đ" },
                { 3, "Lê Hoàng Cường", "Đầu bếp", "10.000.000đ", "1.500.000đ", "11.500.000đ" },
                { 4, "Phạm Minh Đức", "Quản lý", "12.000.000đ", "2.000.000đ", "14.000.000đ" },
                { 5, "Hoàng Thị Em", "Phục vụ", "6.500.000đ", "800.000đ", "7.300.000đ" },
                { 6, "Võ Văn Phúc", "Barista", "8.000.000đ", "1.200.000đ", "9.200.000đ" },
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(34);
        table.setGridColor(new Color(230, 220, 210));
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_MENU);
        table.setSelectionBackground(new Color(191, 161, 127));
        table.setSelectionForeground(Color.WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.WHITE);
        header.setBackground(DARK_BROWN);
        header.setPreferredSize(new Dimension(0, 40));
        header.setResizingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // Center-align all cells
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        // Right-align currency columns (index 3,4,5)
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i >= 3) {
                table.getColumnModel().getColumn(i).setCellRenderer(right);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(center);
            }
        }

        // Alternating row colors
        DefaultTableCellRenderer stripe = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 250, 248));
                }
                return c;
            }
        };
        stripe.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < 3; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(stripe);
        }
        DefaultTableCellRenderer stripeRight = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(252, 250, 248));
                }
                return c;
            }
        };
        stripeRight.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i = 3; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(stripeRight);
        }

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);

        // Scroll pane
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 195, 180), 1),
                BorderFactory.createEmptyBorder(2, 0, 2, 0)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel() {
            {
                setBackground(DARK_BROWN);
            }
        });
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        panel.add(titleRow, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {
        }

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