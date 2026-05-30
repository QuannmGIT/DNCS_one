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
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.model.Order;
import hanabi.model.Staff;
import hanabi.service.AccountService;
import hanabi.service.RevenueService;

public class OrdersPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);
    private static final Color BORDER_COLOR = new Color(230, 225, 220);
    private static final Color TABLE_ALT_BG = new Color(252, 250, 248);
    private static final Color SUBTITLE_COLOR = new Color(140, 120, 110);
    private static final Color PAGINATION_ACTIVE = new Color(211, 181, 147);

    private final RevenueService revenueService = new RevenueService();
    private final AccountService accountService = new AccountService();

    private List<Order> allOrders = new ArrayList<>();
    private int currentPage = 1;
    private int pageSize = 12;
    private int totalFilteredCount = 0;

    private JLabel lblTotalCount;
    private JTextField txtSearch;
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JComboBox<StaffItem> cboStaff;
    private JTable table;
    private DefaultTableModel model;
    private JPanel paginationPanel;

    public OrdersPanel() {
        initComponents();
    }

    public void loadData() {
        new SwingWorker<List<Order>, Void>() {
            private List<Staff> staffList;

            @Override
            protected List<Order> doInBackground() {
                staffList = accountService.getAllStaff();
                return revenueService.getRecentOrders(1000);
            }

            @Override
            protected void done() {
                try {
                    allOrders = get();
                    cboStaff.removeAllItems();
                    cboStaff.addItem(new StaffItem(null, "Tất cả nhân viên"));
                    if (staffList != null) {
                        for (Staff s : staffList) {
                            cboStaff.addItem(new StaffItem(s.getStaffId(), s.getFullName()));
                        }
                    }
                    currentPage = 1;
                    applyFilters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void applyFilters() {
        String search = txtSearch.getText().trim();
        String fromText = txtFromDate.getText().trim();
        String toText = txtToDate.getText().trim();
        StaffItem selected = (StaffItem) cboStaff.getSelectedItem();
        UUID staffId = (selected != null && selected.id != null) ? selected.id : null;

        LocalDate fromDate = null;
        LocalDate toDate = null;
        try { fromDate = fromText.isEmpty() ? null : LocalDate.parse(fromText, DateTimeFormatter.ofPattern("dd/MM/yyyy")); } catch (Exception ignored) {}
        try { toDate = toText.isEmpty() ? null : LocalDate.parse(toText, DateTimeFormatter.ofPattern("dd/MM/yyyy")); } catch (Exception ignored) {}

        String upperSearch = search.isEmpty() ? null : search.toUpperCase();
        LocalDate fFrom = fromDate;
        LocalDate fTo = toDate;

        List<Order> filtered = allOrders.stream()
            .filter(o -> upperSearch == null || (o.getOrderId() != null && o.getOrderId().toString().toUpperCase().contains(upperSearch)))
            .filter(o -> fFrom == null || (o.getOrderDate() != null && !o.getOrderDate().isBefore(fFrom)))
            .filter(o -> fTo == null || (o.getOrderDate() != null && !o.getOrderDate().isAfter(fTo)))
            .filter(o -> staffId == null || (o.getStaff() != null && staffId.equals(o.getStaff().getStaffId())))
            .collect(Collectors.toList());

        totalFilteredCount = filtered.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalFilteredCount / pageSize));
        if (currentPage > totalPages) currentPage = totalPages;

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());
        List<Order> pageOrders = (fromIndex < filtered.size()) ? filtered.subList(fromIndex, toIndex) : List.of();

        updateTable(pageOrders);
        updatePagination(totalPages);
        lblTotalCount.setText("Tổng số đơn: " + totalFilteredCount);
    }

    private void updateTable(List<Order> orders) {
        model.setRowCount(0);
        int stt = (currentPage - 1) * pageSize + 1;
        for (Order order : orders) {
            String shortId = order.getOrderId() != null
                    ? order.getOrderId().toString().substring(0, 8).toUpperCase() : "N/A";
            String date = order.getOrderDate() != null
                    ? order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
            String staffName = (order.getStaff() != null && order.getStaff().getFullName() != null)
                    ? order.getStaff().getFullName() : "N/A";
            String totalStr = String.format("%,d đ", order.getTotal() != null ? order.getTotal() : 0);
            model.addRow(new Object[]{stt++, shortId, date, staffName, totalStr});
        }
    }

    private void updatePagination(int totalPages) {
        paginationPanel.removeAll();

        if (totalPages > 1) {
            JButton btnPrev = createPageBtn("");
            btnPrev.setIcon(new FlatSVGIcon("hanabi/assets/icon/LeftArrow.svg", 16, 16));
            btnPrev.setEnabled(currentPage > 1);
            btnPrev.addActionListener(e -> { if (currentPage > 1) { currentPage--; applyFilters(); } });
            paginationPanel.add(btnPrev);

            List<Integer> pages = new ArrayList<>();
            if (totalPages <= 7) {
                for (int i = 1; i <= totalPages; i++) pages.add(i);
            } else {
                pages.add(1);
                if (currentPage > 3) pages.add(-1);
                int start = Math.max(2, currentPage - 1);
                int end = Math.min(totalPages - 1, currentPage + 1);
                for (int i = start; i <= end; i++) pages.add(i);
                if (currentPage < totalPages - 2) pages.add(-1);
                pages.add(totalPages);
            }

            for (int p : pages) {
                if (p == -1) {
                    JLabel dots = new JLabel("...");
                    dots.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    dots.setForeground(DARK_BROWN);
                    dots.setPreferredSize(new Dimension(36, 36));
                    dots.setHorizontalAlignment(SwingConstants.CENTER);
                    paginationPanel.add(dots);
                } else {
                    JButton btnPage = createPageBtn(String.valueOf(p));
                    if (p == currentPage) {
                        btnPage.setBackground(PAGINATION_ACTIVE);
                        btnPage.setForeground(Color.WHITE);
                        btnPage.setBorder(BorderFactory.createEmptyBorder());
                    }
                    final int pageNum = p;
                    btnPage.addActionListener(e -> { currentPage = pageNum; applyFilters(); });
                    paginationPanel.add(btnPage);
                }
            }

            JButton btnNext = createPageBtn("");
            btnNext.setIcon(new FlatSVGIcon("hanabi/assets/icon/RightArrow.svg", 16, 16));
            btnNext.setEnabled(currentPage < totalPages);
            btnNext.addActionListener(e -> { if (currentPage < totalPages) { currentPage++; applyFilters(); } });
            paginationPanel.add(btnNext);
        }

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JButton createPageBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setBackground(Color.WHITE);
        btn.setForeground(DARK_BROWN);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:10; borderWidth:1; borderColor:#E6E1DC; focusWidth:0; innerFocusWidth:0;");
        btn.putClientProperty("JButton.hoverBackground", new Color(242, 236, 228));
        return btn;
    }

    private void initComponents() {
        setBackground(LIGHT_BG);
        setLayout(new BorderLayout(0, 0));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createPaginationBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setBorder(new EmptyBorder(25, 30, 20, 30)); 

        //Left: Title and total count
        JPanel left = new JPanel(new BorderLayout(0, 5));
        left.setOpaque(false);

        JLabel lblTitle = new JLabel("Lịch Sử Đơn Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(DARK_BROWN);

        lblTotalCount = new JLabel("Tổng số đơn: ...");
        lblTotalCount.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTotalCount.setForeground(SUBTITLE_COLOR);

        left.add(lblTitle, BorderLayout.NORTH);
        left.add(lblTotalCount, BorderLayout.CENTER);

        header.add(left, BorderLayout.WEST);

        //Right: Filter toolbar
        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 0, 0); 

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm mã đơn...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(180, 38));
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        txtSearch.addActionListener(e -> { currentPage = 1; applyFilters(); });

        gbc.gridx = 0;
        gbc.weightx = 0;
        right.add(txtSearch, gbc);

        txtFromDate = new JTextField();
        txtFromDate.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Từ ngày");
        txtFromDate.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtFromDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFromDate.setPreferredSize(new Dimension(110, 38));
        txtFromDate.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        txtFromDate.addActionListener(e -> { currentPage = 1; applyFilters(); });

        gbc.gridx = 1;
        right.add(txtFromDate, gbc);

        txtToDate = new JTextField();
        txtToDate.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Đến ngày");
        txtToDate.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtToDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtToDate.setPreferredSize(new Dimension(110, 38));
        txtToDate.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        txtToDate.addActionListener(e -> { currentPage = 1; applyFilters(); });

        gbc.gridx = 2;
        right.add(txtToDate, gbc);

        cboStaff = new JComboBox<>();
        cboStaff.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cboStaff.setPreferredSize(new Dimension(180, 38));
        cboStaff.putClientProperty(FlatClientProperties.STYLE, "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        cboStaff.addActionListener(e -> { if (cboStaff.getSelectedIndex() >= 0) { currentPage = 1; applyFilters(); } });

        gbc.gridx = 3;
        right.add(cboStaff, gbc);

        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createTablePanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(0, 30, 0, 30));

        String[] columns = {"STT", "Mã Đơn Hàng (ID)", "Ngày Đặt", "Nhân Viên Thu Ngân", "Tổng Tiền"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(55); 
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(50, 40, 35));
        table.setSelectionBackground(new Color(242, 236, 228));
        table.setSelectionForeground(DARK_BROWN);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(DARK_BROWN);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50)); // Tăng chiều cao Header lên 50

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);

        table.getColumnModel().getColumn(0).setCellRenderer(new StripeCenterRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new StripeCenterRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new StripeCenterRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new StripeCenterRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new StripeRightRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:10; trackArc:999; thumbInsets:2,2,2,2; trackInsets:2,2,2,2;");

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private JPanel createPaginationBar() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_BG);
        wrapper.setBorder(new EmptyBorder(15, 30, 20, 30));

        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        paginationPanel.setOpaque(false);

        wrapper.add(paginationPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private static class StripeCenterRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_BG);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            return c;
        }
    }

    private static class StripeRightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_BG);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(new EmptyBorder(0, 10, 0, 20));
            return c;
        }
    }

    private static class StaffItem {
        final UUID id;
        final String display;
        StaffItem(UUID id, String display) { this.id = id; this.display = display; }
        @Override public String toString() { return display; }
    }
}