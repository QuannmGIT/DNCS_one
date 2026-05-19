package com.hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.hanabi.util.FontLoader;
import com.hanabi.util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;

public class RevenuePanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(90, 70, 61);

    private Font amaticFont;

    public RevenuePanel() {
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
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 15));
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // 1. Header (Brand)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Main Content Wrapper
        JPanel contentWrapper = new JPanel(new GridBagLayout());
        contentWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // 2.1 Stat Cards
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 15, 0);
        contentWrapper.add(createStatsPanel(), gbc);

        // 2.2 Chart Section
        gbc.gridy = 1;
        gbc.weighty = 0.6; 
        contentWrapper.add(createChartSection(), gbc);

        // 2.3 Bottom Section (Orders & Top Products)
        gbc.gridy = 2;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentWrapper.add(createBottomSection(), gbc);

        add(contentWrapper, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        headerPanel.setOpaque(false);
        
        JLabel brandName = new JLabel("HANABI CAFE");
        brandName.setFont(amaticFont);
        brandName.setForeground(DARK_BROWN);
        
        JLabel brandIcon = new JLabel();
        try {
            ImageIcon img = new ImageIcon(AccountPanel.class.getResource(
                "/com/hanabi/resources/StoreManagement/assets/icon/HanabiCafe.png"));
            brandIcon.setIcon(new ImageIcon(img.getImage().getScaledInstance(52, 42, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {}

        headerPanel.add(brandName);
        headerPanel.add(brandIcon);
        return headerPanel;
    }

    // ==========================================
    // STATS SECTION
    // ==========================================
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatCard("Today", "1,500,000đ", "Total Revenue", "Today.svg"));
        statsPanel.add(createStatCard("Orders", "32", "Total Orders", "Orders.svg"));
        statsPanel.add(createStatCard("Product", "Matcha Ice Blended", "Best Seller", "MenuIcon.svg"));
        statsPanel.add(createStatCard("Rating", "4.5", "Average Rating", "Rating.svg"));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, String subtitle, String iconPath) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BG_COLOR);
        card.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 15, 2, 15);

        // Title Row
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);
        JLabel iconLbl = new JLabel();
        try {
            iconLbl.setIcon(new FlatSVGIcon("com/hanabi/resources/StoreManagement/assets/icon/" + iconPath, 18, 18));
        } catch (Exception ignored) {}
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(DARK_BROWN);
        titlePanel.add(iconLbl);
        titlePanel.add(titleLbl);

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(titlePanel, gbc);

        // Value Row
        JLabel valueLbl = new JLabel(value, SwingConstants.CENTER);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLbl.setForeground(DARK_BROWN);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 15, 5, 15);
        card.add(valueLbl, gbc);

        // Subtitle Row
        JLabel subLbl = new JLabel(subtitle, SwingConstants.CENTER);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLbl.setForeground(TEXT_MENU);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 15, 10, 15);
        card.add(subLbl, gbc);

        return card;
    }

    // ==========================================
    // CHART SECTION
    // ==========================================
    private JPanel createChartSection() {
        JPanel chartSection = new JPanel(new BorderLayout(0, 10));
        chartSection.setBackground(BG_COLOR);
        chartSection.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        chartSection.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Chart Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Revenue Chart");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(DARK_BROWN);
        headerPanel.add(title, BorderLayout.WEST);

        // Filter Buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        filterPanel.setOpaque(false);
        filterPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        filterPanel.setBorder(new EmptyBorder(3, 5, 3, 5));
        
        for (JButton btn : createFilterGroup("Today", "This Week", "This Month", "Year")) {
            filterPanel.add(btn);
        }
        
        headerPanel.add(filterPanel, BorderLayout.EAST);

        chartSection.add(headerPanel, BorderLayout.NORTH);

        // Biểu đồ Custom (Mock data)
        chartSection.add(new CustomChartPanel(), BorderLayout.CENTER);

        return chartSection;
    }

    private JButton createFilterButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:20");
        
        if (active) {
            btn.setBackground(DARK_BROWN);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(DARK_BROWN);
        }
        btn.setPreferredSize(new Dimension(110, 30));
        return btn;
    }

    private JButton[] createFilterGroup(String... labels) {
        JButton[] btns = new JButton[labels.length];
        for (int i = 0; i < labels.length; i++) {
            btns[i] = createFilterButton(labels[i], i == 0);
        }
        for (JButton btn : btns) {
            btn.addActionListener(e -> {
                for (JButton b : btns) {
                    b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    b.setBackground(Color.WHITE);
                    b.setForeground(DARK_BROWN);
                }
                JButton src = (JButton) e.getSource();
                src.setFont(new Font("Segoe UI", Font.BOLD, 12));
                src.setBackground(DARK_BROWN);
                src.setForeground(Color.WHITE);
            });
        }
        return btns;
    }

    // ==========================================
    // BOTTOM SECTION
    // ==========================================
    private JPanel createBottomSection() {
        JPanel bottomSection = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomSection.setOpaque(false);

        // --- Recent Orders ---
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(BG_COLOR);
        recentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        recentPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel recentHeader = new JPanel(new BorderLayout());
        recentHeader.setOpaque(false);
        JLabel recentTitle = new JLabel("Recent Orders");
        recentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentTitle.setForeground(DARK_BROWN);

        recentHeader.add(recentTitle, BorderLayout.WEST);
        recentPanel.add(recentHeader, BorderLayout.NORTH);

        // Table header
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel headerRow = new JPanel(new GridLayout(1, 4, 0, 0));
        headerRow.setOpaque(false);
        headerRow.add(createTableLabel("Order ID"));
        headerRow.add(createTableLabel("Time"));
        headerRow.add(createTableLabel("Day"));
        headerRow.add(createTableLabel("Total"));
        tablePanel.add(headerRow);

        // Separator
        JPanel sep = new JPanel();
        sep.setBackground(new Color(211, 181, 147));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        tablePanel.add(Box.createVerticalStrut(6));
        tablePanel.add(sep);
        tablePanel.add(Box.createVerticalStrut(4));

        // Data rows
        String[][] orderData = {
            {"#1024", "14:32", "Mon", "185,000đ"},
            {"#1023", "13:15", "Mon", "320,000đ"},
            {"#1022", "11:50", "Mon", "95,000đ"},
            {"#1021", "10:20", "Mon", "245,000đ"}
        };
        for (int i = 0; i < orderData.length; i++) {
            JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
            row.setBackground(i % 2 == 0 ? Color.WHITE : new Color(250, 247, 244));
            row.setBorder(new EmptyBorder(6, 0, 6, 0));
            for (String val : orderData[i]) {
                JLabel cell = new JLabel(val);
                cell.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                cell.setForeground(DARK_BROWN);
                row.add(cell);
            }
            tablePanel.add(row);
        }
        recentPanel.add(tablePanel, BorderLayout.CENTER);

        // --- Top Selling Products ---
        JPanel topSellingPanel = new JPanel(new BorderLayout());
        topSellingPanel.setBackground(BG_COLOR);
        topSellingPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        topSellingPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        JLabel topTitle = new JLabel("Top Selling Products");
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topTitle.setForeground(DARK_BROWN);
        topHeader.add(topTitle, BorderLayout.WEST);
        topSellingPanel.add(topHeader, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Separator
        JPanel sep2 = new JPanel();
        sep2.setBackground(new Color(211, 181, 147));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        listPanel.add(sep2);
        listPanel.add(Box.createVerticalStrut(4));

        String[][] productData = {
            {"1", "Matcha Ice Blended", "156"},
            {"2", "Iced Coffee", "132"},
            {"3", "Machiato coffee", "98"},
            {"4", "Americano", "74"}
        };
        Color[] rankColors = {
            new Color(212, 175, 55),
            new Color(176, 176, 176),
            new Color(166, 124, 82),
            DARK_BROWN
        };
        for (int i = 0; i < productData.length; i++) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(i % 2 == 0 ? Color.WHITE : new Color(250, 247, 244));
            row.setBorder(new EmptyBorder(6, 8, 6, 8));

            JLabel rankLbl = new JLabel(productData[i][0]);
            rankLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            rankLbl.setForeground(i < 3 ? rankColors[i] : DARK_BROWN);
            rankLbl.setPreferredSize(new Dimension(20, 20));
            row.add(rankLbl, BorderLayout.WEST);

            JLabel nameLbl = new JLabel(productData[i][1]);
            nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nameLbl.setForeground(DARK_BROWN);
            row.add(nameLbl, BorderLayout.CENTER);

            JLabel qtyLbl = new JLabel(productData[i][2] + " sold");
            qtyLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            qtyLbl.setForeground(TEXT_MENU);
            row.add(qtyLbl, BorderLayout.EAST);

            listPanel.add(row);
        }

        // Vertical Filter
        JPanel verticalFilter = new JPanel(new GridLayout(4, 1, 3, 3));
        verticalFilter.setOpaque(false);
        verticalFilter.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        verticalFilter.setBorder(new EmptyBorder(5, 5, 5, 5));
        for (JButton btn : createFilterGroup("Today", "This Week", "This Month", "Year")) {
            verticalFilter.add(btn);
        }

        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setOpaque(false);
        filterWrapper.setBorder(new EmptyBorder(0, 10, 0, 0));
        filterWrapper.add(verticalFilter, BorderLayout.NORTH);
        topSellingPanel.add(filterWrapper, BorderLayout.EAST);

        topSellingPanel.add(listPanel, BorderLayout.CENTER);

        bottomSection.add(recentPanel);
        bottomSection.add(topSellingPanel);

        return bottomSection;
    }

    private JLabel createTableLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(TEXT_MENU);
        return lbl;
    }

    private JLabel createListLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(DARK_BROWN);
        return lbl;
    }

    // ==========================================
    // CUSTOM CHART PANEL (Vẽ Line Chart với Gradient)
    // ==========================================
    private class CustomChartPanel extends JPanel {
        // Dữ liệu giả lập trục Y (đã chuẩn hóa 0.0 -> 1.0)
        private final double[] dataPoints = {0.2, 0.3, 0.4, 0.5, 0.2, 0.25, 0.5, 0.2, 0.6, 0.9, 0.5, 0.8, 0.6, 0.8, 0.9, 1.0, 0.5, 0.7, 0.9};
        private final String[] xLabels = {"1", "3", "4", "7", "9", "11", "13", "15", "17", "19", "21", "23", "25", "27", "29", "31"};

        public CustomChartPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int paddingX = 30;
            int paddingY = 30;

            int chartWidth = width - 2 * paddingX;
            int chartHeight = height - 2 * paddingY;

            // 1. Vẽ lưới mờ (Grid lines) ngang
            g2.setColor(new Color(240, 235, 230));
            String[] yLabels = {"0", "500K", "1M", "1.5M", "2M"};
            int ySteps = yLabels.length - 1;
            for (int i = 0; i <= ySteps; i++) {
                int y = height - paddingY - (int) ((double) i / ySteps * chartHeight * 0.8);
                g2.drawLine(paddingX, y, width - paddingX, y);
                // Y-axis labels
                g2.setColor(DARK_BROWN);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(yLabels[i], paddingX - fm.stringWidth(yLabels[i]) - 8, y + fm.getHeight() / 3);
                g2.setColor(new Color(240, 235, 230));
            }

            g2.setColor(DARK_BROWN);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            int stepX = chartWidth / (xLabels.length - 1);
            for (int i = 0; i < xLabels.length; i++) {
                int x = paddingX + (i * stepX);
                int y = height - paddingY + fm.getHeight() + 8;
                g2.drawString(xLabels[i], x - fm.stringWidth(xLabels[i]) / 2, y);
            }

            int[] xCoords = new int[dataPoints.length];
            int[] yCoords = new int[dataPoints.length];
            int dataStepX = chartWidth / (dataPoints.length - 1);

            for (int i = 0; i < dataPoints.length; i++) {
                xCoords[i] = paddingX + (i * dataStepX);
                yCoords[i] = height - paddingY - (int) (dataPoints[i] * chartHeight * 0.8);
            }

            Path2D.Float path = new Path2D.Float();
            path.moveTo(xCoords[0], height - paddingY);
            path.lineTo(xCoords[0], yCoords[0]);
            for (int i = 1; i < dataPoints.length; i++) {
                path.lineTo(xCoords[i], yCoords[i]);
            }
            path.lineTo(xCoords[dataPoints.length - 1], height - paddingY);
            path.closePath();

            GradientPaint gp = new GradientPaint(
                0, paddingY, new Color(139, 100, 78, 100),
                0, height - paddingY, new Color(255, 255, 255, 0)
            );
            g2.setPaint(gp);
            g2.fill(path);

            // 5. Vẽ Line chính
            g2.setColor(new Color(139, 100, 78));
            g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < dataPoints.length - 1; i++) {
                g2.drawLine(xCoords[i], yCoords[i], xCoords[i + 1], yCoords[i + 1]);
            }

            int radius = 6;
            g2.setColor(new Color(139, 100, 78));
            for (int i = 0; i < dataPoints.length; i++) {
                g2.fillOval(xCoords[i] - radius, yCoords[i] - radius, radius * 2, radius * 2);
            }
            g2.setColor(Color.WHITE);
            for (int i = 0; i < dataPoints.length; i++) {
                g2.fillOval(xCoords[i] - radius / 2, yCoords[i] - radius / 2, radius, radius);
            }
        }
    }

    // Demo Run
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Revenue Panel - HANABI CAFE");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(new RevenuePanel());
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}