package hanabi.view.Category;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.model.Order;
import hanabi.service.RevenueService;
import hanabi.util.FontLoader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class RevenuePanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(90, 70, 61);

    private Font amaticFont;
    private final RevenueService revenueService = new RevenueService();
    private String currentFilter = "Today";

    private JLabel todayValue;
    private JLabel ordersValue;
    private JLabel productValue;
    private JLabel ratingValue;
    private JPanel bottomSection;
    private JPanel chartSection;
    private CustomChartPanel chartPanel;

    public RevenuePanel() {
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
        gbc.ipadx = 2;
        contentWrapper.add(createChartSection(), gbc);

        // 2.3 Bottom Section (Orders & Top Products)
        gbc.gridy = 2;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(0, 0, 0, 0);
        bottomSection = createBottomSection();
        contentWrapper.add(bottomSection, gbc);

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
                "/hanabi/assets/icon/HanabiCafe.png"));
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

        todayValue = new JLabel("...");
        ordersValue = new JLabel("...");
        productValue = new JLabel("...");
        ratingValue = new JLabel("...");
        statsPanel.add(createStatCard("Today", todayValue, "Total Revenue", "Today.svg"));
        statsPanel.add(createStatCard("Orders", ordersValue, "Total Orders", "Orders.svg"));
        statsPanel.add(createStatCard("Product", productValue, "Best Seller", "MenuIcon.svg"));
        statsPanel.add(createStatCard("Rating", ratingValue, "Average Rating", "Rating.svg"));

        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLbl, String subtitle, String iconPath) {
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
            iconLbl.setIcon(new FlatSVGIcon("hanabi/assets/icon/" + iconPath, 18, 18));
        } catch (Exception ignored) {}
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(DARK_BROWN);
        titlePanel.add(iconLbl);
        titlePanel.add(titleLbl);

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(titlePanel, gbc);

        // Value Row
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLbl.setForeground(DARK_BROWN);
        valueLbl.setHorizontalAlignment(SwingConstants.CENTER);
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
        chartSection.putClientProperty(FlatClientProperties.STYLE, "arc:20;");
        chartSection.setBorder(BorderFactory.createEmptyBorder());

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
        // filterPanel.setBorder(new EmptyBorder(3, 5, 3, 5));
        
        for (JButton btn : createFilterGroup("Today", "This Week", "This Month", "Year")) {
            filterPanel.add(btn);
        }
        
        headerPanel.add(filterPanel, BorderLayout.EAST);

        chartSection.add(headerPanel, BorderLayout.NORTH);

        chartPanel = new CustomChartPanel();
        chartSection.add(chartPanel, BorderLayout.CENTER);

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
                loadChartData(src.getText());
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
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            tablePanel.add(row);
        }
        tablePanel.add(Box.createVerticalGlue());
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

            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            listPanel.add(row);
        }
        listPanel.add(Box.createVerticalGlue());

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
        private double[] dataPoints = {0.2, 0.3, 0.4, 0.5, 0.2, 0.35, 0.5, 0.6, 0.7, 0.5, 0.8, 0.6, 0.9, 0.7, 0.8, 0.45, 0.6, 0.5, 0.3, 0.7, 0.5, 0.8, 0.9, 0.6, 0.4, 0.5, 0.7, 0.8, 0.5, 0.6, 0.9};
        private String[] xLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        public CustomChartPanel() {
            setOpaque(false);
        }

        public void updateData(double[] dataPoints, String[] xLabels) {
            this.dataPoints = dataPoints;
            this.xLabels = xLabels;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int paddingX = 50;
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
                g2.drawString(yLabels[i], paddingX - fm.stringWidth(yLabels[i]) - 15, y + fm.getHeight() / 3);
                g2.setColor(new Color(240, 235, 230));
            }

            g2.setColor(DARK_BROWN);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            FontMetrics fm = g2.getFontMetrics();
            if (xLabels.length > 1) {
                int stepX = chartWidth / (xLabels.length - 1);
                for (int i = 0; i < xLabels.length; i++) {
                    int x = paddingX + (i * stepX);
                    int y = height - paddingY + fm.getHeight() + 8;
                    g2.drawString(xLabels[i], x - fm.stringWidth(xLabels[i]) / 2, y);
                }
            }

            int[] xCoords = new int[dataPoints.length];
            int[] yCoords = new int[dataPoints.length];
            int dataStepX = dataPoints.length > 1 ? chartWidth / (dataPoints.length - 1) : 0;

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

    private String fmtRevenue(long v) {
        if (v >= 1_000_000) {
            return String.format("%,.0fMđ", v / 1_000_000.0).replace(",", ".");
        }
        return String.format("%,dđ", v).replace(",", ".");
    }

    public void loadData() {
        todayValue.setText("...");
        ordersValue.setText("...");
        productValue.setText("...");
        ratingValue.setText("...");

        if (bottomSection != null) {
            bottomSection.removeAll();
            bottomSection.setLayout(new GridLayout(1, 1, 0, 0));
            JLabel loading = new JLabel("Loading...", SwingConstants.CENTER);
            loading.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            loading.setForeground(TEXT_MENU);
            bottomSection.add(loading);
            bottomSection.revalidate();
            bottomSection.repaint();
        }

        chartPanel.updateData(new double[]{0}, new String[]{""});

        new SwingWorker<Void, Void>() {
            private long todayRev;
            private long totalOrders;
            private String best;
            private double avg;
            private List<Order> recentOrders;
            private List<Object[]> topProducts;

            @Override
            protected Void doInBackground() {
                todayRev = revenueService.getTodayRevenue();
                totalOrders = revenueService.getTotalOrders();
                best = revenueService.getBestSeller();
                avg = revenueService.getAverageRating();
                recentOrders = revenueService.getRecentOrders(4);
                topProducts = revenueService.getTopSellingProducts(4);
                return null;
            }

            @Override
            protected void done() {
                todayValue.setText(fmtRevenue(todayRev));
                ordersValue.setText(String.valueOf(totalOrders));
                productValue.setText(best.isEmpty() ? "..." : best);
                ratingValue.setText(String.format("%.1f", avg));

                if (bottomSection != null) {
                    bottomSection.removeAll();
                    bottomSection.setLayout(new GridLayout(1, 2, 15, 0));
                    bottomSection.add(createRecentOrdersPanel(recentOrders));
                    bottomSection.add(createTopSellingPanel(topProducts));
                    bottomSection.revalidate();
                    bottomSection.repaint();
                }

                loadChartData(currentFilter);
            }
        }.execute();
    }

    private void loadChartData(String filter) {
        currentFilter = filter;
        chartPanel.updateData(new double[]{0}, new String[]{""});

        LocalDate now = LocalDate.now();
        LocalDate start, end;

        switch (filter) {
            case "Today":
                start = now; end = now; break;
            case "This Week":
                start = now.with(java.time.DayOfWeek.MONDAY);
                end = start.plusDays(6);
                if (end.isAfter(now)) end = now;
                break;
            case "This Month":
                start = now.withDayOfMonth(1);
                end = start.withDayOfMonth(start.lengthOfMonth());
                break;
            case "Year":
                start = now.withDayOfYear(1);
                end = start.withDayOfYear(start.lengthOfYear());
                break;
            default:
                return;
        }

        final LocalDate fStart = start;
        final LocalDate fEnd = end;
        final boolean isYear = "Year".equals(filter);

        new SwingWorker<Void, Void>() {
            private double[] chartData;
            private String[] chartLabels;

            @Override
            protected Void doInBackground() {
                if (isYear) {
                    Map<String, Long> monthlyRev = revenueService.getMonthlyRevenue(now.getYear());
                    int monthsElapsed = now.getMonthValue();
                    chartData = new double[monthsElapsed];
                    chartLabels = new String[monthsElapsed];
                    long yrMax = 1;
                    String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                    for (int m = 0; m < monthsElapsed; m++) {
                        String key = String.format("%d-%02d", now.getYear(), m + 1);
                        long rev = monthlyRev.getOrDefault(key, 0L);
                        if (rev > yrMax) yrMax = rev;
                        chartData[m] = rev;
                        chartLabels[m] = monthNames[m];
                    }
                    if (yrMax > 0) {
                        for (int m = 0; m < monthsElapsed; m++) chartData[m] = chartData[m] / (double) yrMax;
                    }
                } else {
                    Map<LocalDate, Long> revenueByDay = revenueService.getRevenueByDateRange(fStart, fEnd);
                    int totalDays = (int) java.time.temporal.ChronoUnit.DAYS.between(fStart, fEnd) + 1;
                    chartData = new double[totalDays];
                    chartLabels = new String[totalDays];
                    long maxRevenue = 1;
                    for (int d = 0; d < totalDays; d++) {
                        LocalDate date = fStart.plusDays(d);
                        long rev = revenueByDay.getOrDefault(date, 0L);
                        if (rev > maxRevenue) maxRevenue = rev;
                        chartData[d] = rev;
                        chartLabels[d] = date.format(DateTimeFormatter.ofPattern("dd/MM"));
                    }
                    if (maxRevenue > 0) {
                        for (int d = 0; d < totalDays; d++) chartData[d] = chartData[d] / (double) maxRevenue;
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                chartPanel.updateData(chartData, chartLabels);
            }
        }.execute();
    }

    private JPanel createRecentOrdersPanel(List<Order> orders) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel recentTitle = new JLabel("Recent Orders");
        recentTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        recentTitle.setForeground(DARK_BROWN);
        panel.add(recentTitle, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel headerRow = new JPanel(new GridLayout(1, 4, 0, 0));
        headerRow.setOpaque(false);
        headerRow.add(createTableLabel("Order ID"));
        headerRow.add(createTableLabel("Date"));
        headerRow.add(createTableLabel("Day"));
        headerRow.add(createTableLabel("Total"));
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tablePanel.add(headerRow);

        JPanel sep = new JPanel();
        sep.setBackground(new Color(211, 181, 147));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        tablePanel.add(Box.createVerticalStrut(6));
        tablePanel.add(sep);
        tablePanel.add(Box.createVerticalStrut(4));

        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            JPanel row = new JPanel(new GridLayout(1, 4, 0, 0));
            row.setBackground(i % 2 == 0 ? Color.WHITE : new Color(250, 247, 244));
            row.setBorder(new EmptyBorder(6, 0, 6, 0));
            row.add(new JLabel("#" + o.getOrderId().toString().substring(0, 8)));
            row.add(new JLabel(o.getOrderDate() != null ? o.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM")) : ""));
            row.add(new JLabel(o.getOrderDate() != null ? o.getOrderDate().format(DateTimeFormatter.ofPattern("EEE")) : ""));
            row.add(new JLabel(o.getTotal() != null ? fmtRevenue(o.getTotal()) : "0đ"));
            for (java.awt.Component c : row.getComponents()) {
                ((JLabel) c).setFont(new Font("Segoe UI", Font.PLAIN, 13));
                ((JLabel) c).setForeground(DARK_BROWN);
            }
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            tablePanel.add(row);
        }
        tablePanel.add(Box.createVerticalGlue());
        panel.add(tablePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTopSellingPanel(List<Object[]> topProducts) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20; border: 1,1,1,1, #5A463D");
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel topTitle = new JLabel("Top Selling Products");
        topTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topTitle.setForeground(DARK_BROWN);
        panel.add(topTitle, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        listPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel sep2 = new JPanel();
        sep2.setBackground(new Color(211, 181, 147));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        listPanel.add(sep2);
        listPanel.add(Box.createVerticalStrut(4));

        Color[] rankColors = {
            new Color(212, 175, 55),
            new Color(176, 176, 176),
            new Color(166, 124, 82),
            DARK_BROWN
        };

        for (int i = 0; i < topProducts.size(); i++) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(i % 2 == 0 ? Color.WHITE : new Color(250, 247, 244));
            row.setBorder(new EmptyBorder(6, 8, 6, 8));

            JLabel rankLbl = new JLabel(String.valueOf(i + 1));
            rankLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            rankLbl.setForeground(i < 3 ? rankColors[i] : DARK_BROWN);
            rankLbl.setPreferredSize(new Dimension(20, 20));
            row.add(rankLbl, BorderLayout.WEST);

            String name = topProducts.get(i)[0] != null ? (String) topProducts.get(i)[0] : "";
            JLabel nameLbl = new JLabel(name);
            nameLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nameLbl.setForeground(DARK_BROWN);
            row.add(nameLbl, BorderLayout.CENTER);

            String qty = topProducts.get(i)[1] != null ? topProducts.get(i)[1].toString() : "0";
            JLabel qtyLbl = new JLabel(qty + " sold");
            qtyLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            qtyLbl.setForeground(TEXT_MENU);
            row.add(qtyLbl, BorderLayout.EAST);

            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            listPanel.add(row);
        }
        listPanel.add(Box.createVerticalGlue());
        panel.add(listPanel, BorderLayout.CENTER);
        return panel;
    }

}