package StoreManagement.component;

import StoreManagement.Utility.DATABBASE;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SalePanel extends JPanel {

    private ManageFrame frame;
    private JLabel lblTotalRevenue;
    private JLabel lblYearRevenue;
    private JLabel lblTotalOrders;
    private JLabel lblAvgOrderValue;
    private BarChartPanel chartPanel;
    private DefaultListModel<String> topProductModel;

    public SalePanel() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Bảng điều khiển Doanh thu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        this.add(lblTitle, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBackground(Color.WHITE);
        this.add(mainContent, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setPreferredSize(new Dimension(0, 120));

        lblTotalRevenue = createStatCard(statsPanel, "Doanh thu (Tháng)", "0 đ");
        lblYearRevenue = createStatCard(statsPanel, "Doanh thu (Năm)", "0 đ");
        lblTotalOrders = createStatCard(statsPanel, "Số Đơn hàng (Tháng)", "0");
        lblAvgOrderValue = createStatCard(statsPanel, "Trung bình Đơn", "0 đ");

        mainContent.add(statsPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout(20, 0));
        bottomPanel.setBackground(Color.WHITE);
        mainContent.add(bottomPanel, BorderLayout.CENTER);

        chartPanel = new BarChartPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.BLACK), "Biểu đồ doanh thu 7 ngày gần nhất",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.PLAIN, 16)));
        bottomPanel.add(chartPanel, BorderLayout.CENTER);

        JPanel rightListPanel = new JPanel(new BorderLayout());
        rightListPanel.setBackground(Color.WHITE);
        rightListPanel.setPreferredSize(new Dimension(280, 0));
        rightListPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Color.BLACK), "Top sản phẩm bán chạy",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.PLAIN, 16)));

        topProductModel = new DefaultListModel<>();
        JList<String> lstTopProducts = new JList<>(topProductModel);
        lstTopProducts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lstTopProducts.setFixedCellHeight(35);
        rightListPanel.add(new JScrollPane(lstTopProducts), BorderLayout.CENTER);

        bottomPanel.add(rightListPanel, BorderLayout.EAST);

        loadDashboardData();
    }

    private JLabel createStatCard(JPanel parent, String title, String defaultValue) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(230, 230, 230), 1));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(defaultValue, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(new Color(32, 178, 170));

        if (title.contains("Doanh thu (Tháng)")) {
            lblValue.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lblValue.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    new MonthlyOrderListDialog(); 
                }
            });
        }

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        parent.add(card);
        return lblValue;
    }

    private void loadDashboardData() {
//        DATABBASE db = new DATABBASE();
        try (Connection conn = null) {
            if (conn == null) return;

      
            String sqlMonth = "SELECT COUNT(*) as total_orders, IFNULL(SUM(total_amount), 0) as total_rev " +
                              "FROM Orders WHERE MONTH(order_date) = MONTH(CURRENT_DATE()) " +
                              "AND YEAR(order_date) = YEAR(CURRENT_DATE())";
            
            double monthRev = 0;
            int totalOrders = 0;

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlMonth)) {
                if (rs.next()) {
                    totalOrders = rs.getInt("total_orders");
                    monthRev = rs.getDouble("total_rev");
                }
            }

         
            String sqlYear = "SELECT IFNULL(SUM(total_amount), 0) as year_rev " +
                             "FROM Orders WHERE YEAR(order_date) = YEAR(CURRENT_DATE())";
            double yearRev = 0;
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlYear)) {
                if (rs.next()) {
                    yearRev = rs.getDouble("year_rev");
                }
            }

          
            lblTotalRevenue.setText(formatMoney(monthRev));
            lblYearRevenue.setText(formatMoney(yearRev));
            lblTotalOrders.setText(String.valueOf(totalOrders));
            
            double avg = (totalOrders > 0) ? (monthRev / totalOrders) : 0;
            lblAvgOrderValue.setText(formatMoney(avg));


            String sqlTop = "SELECT p.product_name, SUM(od.quantity) as qty " +
                            "FROM OrderDetails od " +
                            "JOIN Products p ON od.product_id = p.product_id " +
                            "GROUP BY p.product_id " +
                            "ORDER BY qty DESC LIMIT 10";
            
            topProductModel.clear();
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlTop)) {
                int rank = 1;
                while (rs.next()) {
                    String name = rs.getString("product_name");
                    int qty = rs.getInt("qty");
                    topProductModel.addElement(" " + rank + ". " + name + " (" + qty + ")");
                    rank++;
                }
            }

            // Dữ liệu biểu đồ
            String sqlChart = "SELECT DATE(order_date) as order_day, SUM(total_amount) as daily_rev " +
                              "FROM Orders " +
                              "GROUP BY DATE(order_date) " +
                              "ORDER BY order_day DESC LIMIT 7";
            
            Map<String, Double> chartData = new LinkedHashMap<>(); 
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sqlChart)) {
                while (rs.next()) {
                    java.sql.Date date = rs.getDate("order_day"); 
                    double rev = rs.getDouble("daily_rev");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
                    chartData.put(sdf.format(date), rev);
                }
            }
            
            Map<String, Double> sortedData = new LinkedHashMap<>();
            List<String> keys = new ArrayList<>(chartData.keySet());
            Collections.reverse(keys);
            for(String key : keys) sortedData.put(key, chartData.get(key));

            chartPanel.setData(sortedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatMoney(double money) {
        DecimalFormat df = new DecimalFormat("###,### đ");
        return df.format(money);
    }


    class BarChartPanel extends JPanel {
        private Map<String, Double> data = new LinkedHashMap<>();

        public void setData(Map<String, Double> data) {
            this.data = data;
            repaint(); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data.isEmpty()) {
                g.drawString("Chưa có dữ liệu", getWidth()/2 - 40, getHeight()/2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 40; 
            int barWidth = (width - 2 * padding) / Math.max(data.size(), 1) - 15; 
            
            double maxVal = 0;
            for (Double val : data.values()) if (val > maxVal) maxVal = val;
            if(maxVal == 0) maxVal = 1; 

            g2.setColor(Color.GRAY);
            g2.drawLine(padding, height - padding, width - padding, height - padding); 
            g2.drawLine(padding, height - padding, padding, padding); 

            int x = padding + 15;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                String label = entry.getKey();
                double value = entry.getValue();
                int barHeight = (int) ((value / maxVal) * (height - 2 * padding));

                g2.setColor(new Color(100, 149, 237)); 
                g2.fillRect(x, height - padding - barHeight, barWidth, barHeight);
                g2.setColor(Color.BLACK);
                g2.drawRect(x, height - padding - barHeight, barWidth, barHeight);

                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fm = g2.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2.drawString(label, x + (barWidth - labelWidth) / 2, height - padding + 15);

                x += barWidth + 15; 
            }
        }
    }


    class MonthlyOrderListDialog extends JDialog {
        private JTable table;

        public MonthlyOrderListDialog() {
//            super(SalePanel, "Danh Sách Hóa Đơn Trong Tháng", true);
//            SalePanel.getInstance().
            
            JPanel main = new JPanel(new BorderLayout());
            main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setContentPane(main);

            JLabel titleLabel = new JLabel();
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            add(titleLabel, BorderLayout.NORTH);

            table = new JTable();
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setFillsViewportHeight(true);
            table.setDefaultEditor(Object.class, null); 

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            setSize(800, 500);
            setLocationRelativeTo(SalePanel.this.frame);

            loadOrderData();
            setVisible(true);
        }

        private void loadOrderData() {
//            DATABBASE db = new DATABBASE();
            String sql = "SELECT o.order_id, o.order_date, u.username, o.total_amount " +
                         "FROM Orders o JOIN users u ON o.user_id = u.user_id " +
                         "WHERE MONTH(o.order_date) = MONTH(CURRENT_DATE()) " +
                         "AND YEAR(o.order_date) = YEAR(CURRENT_DATE()) " +
                         "ORDER BY o.order_date DESC";

            try (Connection conn = null;
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                table.setModel(buildTableModel(rs));
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        private javax.swing.table.DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
            java.util.Vector<String> columnNames = new java.util.Vector<>();
            columnNames.add("Mã Hóa Đơn");
            columnNames.add("Ngày Giờ");
            columnNames.add("Thu Ngân");
            columnNames.add("Tổng Tiền");

            java.util.Vector<java.util.Vector<Object>> data = new java.util.Vector<>();
            while (rs.next()) {
                java.util.Vector<Object> row = new java.util.Vector<>();
                row.add("#" + rs.getInt("order_id"));
                row.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("order_date")));
                row.add(rs.getString("username"));
                row.add(SalePanel.this.formatMoney(rs.getDouble("total_amount"))); 
                data.add(row);
            }
            return new javax.swing.table.DefaultTableModel(data, columnNames);
        }
    }
}