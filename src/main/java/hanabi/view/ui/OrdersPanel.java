package hanabi.view.ui;

import com.formdev.flatlaf.FlatClientProperties;
import hanabi.dao.InvoiceDAO;
import hanabi.dao.OrderDAO;
import hanabi.dao.OrderDetailDAO;
import hanabi.dao.StaffDAO;
import hanabi.model.Invoice;
import hanabi.model.Order;
import hanabi.model.OrderDetail;
import hanabi.model.Staff;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ItemEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class OrdersPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);
    private static final Color BORDER_COLOR = new Color(230, 225, 220);
    private static final Color TABLE_ALT_BG = new Color(252, 250, 248);
    private static final Color SUBTITLE_COLOR = new Color(140, 120, 110);
    private static final Color PAGINATION_ACTIVE = new Color(211, 181, 147);

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final StaffDAO staffDAO = new StaffDAO();

    private int currentPage = 1;
    private int pageSize = 12;
    private int totalFilteredCount = 0;

    private JLabel lblTotalCount;
    private JTextField txtSearch;
    private JTable tblInvoices;
    private DefaultTableModel invoiceModel;
    private JTable tblDetails;
    private DefaultTableModel detailModel;
    private JPanel paginationPanel;
    private List<java.util.UUID> invoiceIdList = new ArrayList<>();
    private JComboBox<Staff> cmbStaff;

    public OrdersPanel() {
        initComponents();
    }

    public void loadData() {
        currentPage = 1;
        loadStaffList();
        applyFilters();
    }

    private void loadStaffList() {
        new SwingWorker<Void, Void>() {
            private List<Staff> staffList;

            @Override
            protected Void doInBackground() {
                staffList = staffDAO.findAll();
                return null;
            }

            @Override
            protected void done() {
                cmbStaff.removeAllItems();
                cmbStaff.addItem(null);
                for (Staff s : staffList) {
                    if (Boolean.TRUE.equals(s.getStatus())) {
                        cmbStaff.addItem(s);
                    }
                }
            }
        }.execute();
    }

    private void applyFilters() {
        String search = txtSearch.getText().trim();
        Staff selectedStaff = (Staff) cmbStaff.getSelectedItem();
        int page = currentPage;
        int size = pageSize;

        new SwingWorker<Void, Void>() {
            private List<Invoice> resultInvoices = List.of();
            private int totalCount;
            private int resolvedPage = page;

            @Override
            protected Void doInBackground() {
                List<Invoice> all = invoiceDAO.findAll();
                List<Staff> allStaff = staffDAO.findAll();
                java.util.Map<java.util.UUID, String> staffNameMap = new java.util.HashMap<>();
                for (Staff s : allStaff) {
                    staffNameMap.put(s.getStaffId(), s.getFullName() != null ? s.getFullName() : s.getStaffName());
                }
                if (selectedStaff != null) {
                    java.util.UUID sid = selectedStaff.getStaffId();
                    all = all.stream()
                            .filter(inv -> inv.getStaffId() != null && sid.equals(inv.getStaffId()))
                            .collect(Collectors.toList());
                }
                if (!search.isEmpty()) {
                    String upper = search.toUpperCase();
                    all = all.stream()
                            .filter(inv -> inv.getInvoiceId() != null
                                    && inv.getInvoiceId().toString().toUpperCase().contains(upper))
                            .collect(Collectors.toList());
                }
                totalCount = all.size();
                int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / size));
                if (resolvedPage > totalPages)
                    resolvedPage = totalPages;
                int fromIdx = (resolvedPage - 1) * size;
                int toIdx = Math.min(fromIdx + size, all.size());
                resultInvoices = (fromIdx < all.size()) ? all.subList(fromIdx, toIdx) : List.of();
                for (Invoice inv : resultInvoices) {
                    String name = staffNameMap.get(inv.getStaffId());
                    if (name == null) name = "N/A";
                }
                return null;    
            }

            @Override
            protected void done() {
                currentPage = resolvedPage;
                totalFilteredCount = totalCount;
                updateInvoiceTable(resultInvoices);
                int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / size));
                updatePagination(totalPages);
                lblTotalCount.setText("Total invoices: " + totalCount);
            }
        }.execute();
    }

    private void updateInvoiceTable(List<Invoice> invoices) {
        invoiceModel.setRowCount(0);
        invoiceIdList.clear();
        List<Staff> allStaff = staffDAO.findAll();
        java.util.Map<java.util.UUID, String> staffNameMap = new java.util.HashMap<>();
        for (Staff s : allStaff) {
            staffNameMap.put(s.getStaffId(), s.getFullName() != null ? s.getFullName() : s.getStaffName());
        }
        int stt = (currentPage - 1) * pageSize + 1;
        for (Invoice inv : invoices) {
            java.util.UUID uuid = inv.getInvoiceId();
            String shortId = uuid != null ? uuid.toString().substring(0, 8).toUpperCase() : "N/A";
            String date = inv.getInvoiceDate() != null
                    ? inv.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "N/A";
            String staffName = staffNameMap.getOrDefault(inv.getStaffId(), "N/A");
            String totalStr = String.format("%,d đ", inv.getTotal() != null ? inv.getTotal() : 0);
            String statusStr = Boolean.TRUE.equals(inv.getStatus()) ? "Active" : "Inactive";
            invoiceModel.addRow(new Object[]{stt++, shortId, date, staffName, totalStr, statusStr});
            invoiceIdList.add(uuid);
        }
    }

    private void updatePagination(int totalPages) {
        paginationPanel.removeAll();

        if (totalPages > 1) {
            JButton btnPrev = createPageBtn("");
            btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hanabi/assets/icon/LeftArrow.svg")));
            btnPrev.setEnabled(currentPage > 1);
            btnPrev.addActionListener(e -> {
                if (currentPage > 1) {
                    currentPage--;
                    applyFilters();
                }
            });
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
                    btnPage.addActionListener(e -> {
                        currentPage = pageNum;
                        applyFilters();
                    });
                    paginationPanel.add(btnPage);
                }
            }

            JButton btnNext = createPageBtn("");
            btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hanabi/assets/icon/RightArrow.svg")));
            btnNext.setEnabled(currentPage < totalPages);
            btnNext.addActionListener(e -> {
                if (currentPage < totalPages) {
                    currentPage++;
                    applyFilters();
                }
            });
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
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createPaginationBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setBorder(new EmptyBorder(25, 30, 20, 30));

        JPanel left = new JPanel(new BorderLayout(0, 5));
        left.setOpaque(false);

        JLabel lblTitle = new JLabel("Order History");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(DARK_BROWN);

        lblTotalCount = new JLabel("Total invoices: ...");
        lblTotalCount.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTotalCount.setForeground(SUBTITLE_COLOR);

        left.add(lblTitle, BorderLayout.NORTH);
        left.add(lblTotalCount, BorderLayout.CENTER);
        header.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 8, 0, 0);

        cmbStaff = new JComboBox<>();
        cmbStaff.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbStaff.setPreferredSize(new Dimension(180, 38));
        cmbStaff.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        cmbStaff.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                currentPage = 1;
                applyFilters();
            }
        });
        cmbStaff.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("All Staff");
                } else if (value instanceof Staff) {
                    Staff s = (Staff) value;
                    setText(s.getFullName() != null ? s.getFullName() : s.getStaffName());
                }
                return this;
            }
        });

        gbc.gridx = 0;
        gbc.weightx = 0;
        right.add(cmbStaff, gbc);

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search invoice ID...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(200, 38));
        txtSearch.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:1; borderColor:#DCD5CE; focusColor:#D3B593;");
        txtSearch.addActionListener(e -> {
            currentPage = 1;
            applyFilters();
        });

        gbc.gridx = 1;
        gbc.weightx = 0;
        right.add(txtSearch, gbc);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setPreferredSize(new Dimension(100, 38));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; focusWidth:0;");
        btnRefresh.putClientProperty("JButton.hoverBackground", new Color(200, 180, 160));
        btnRefresh.addActionListener(e -> {
            currentPage = 1;
            txtSearch.setText("");
            applyFilters();
        });

        gbc.gridx = 2;
        right.add(btnRefresh, gbc);

        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createCenterPanel() {
        String[] invCols = {"No", "ID", "Date", "Staff", "Total", "Status"};
        invoiceModel = new DefaultTableModel(invCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblInvoices = new JTable(invoiceModel);
        tblInvoices.setRowHeight(50);
        tblInvoices.setShowGrid(false);
        tblInvoices.setIntercellSpacing(new Dimension(0, 0));
        tblInvoices.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblInvoices.setForeground(new Color(50, 40, 35));
        tblInvoices.setSelectionBackground(new Color(242, 236, 228));
        tblInvoices.setSelectionForeground(DARK_BROWN);

        JTableHeader hdr = tblInvoices.getTableHeader();
        hdr.setReorderingAllowed(false);
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 15));
        hdr.setBackground(DARK_BROWN);
        hdr.setForeground(Color.WHITE);
        hdr.setPreferredSize(new Dimension(0, 44));

        tblInvoices.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblInvoices.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblInvoices.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblInvoices.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblInvoices.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblInvoices.getColumnModel().getColumn(5).setPreferredWidth(80);

        tblInvoices.getColumnModel().getColumn(0).setCellRenderer(new StripeCenterRenderer());
        tblInvoices.getColumnModel().getColumn(1).setCellRenderer(new StripeCenterRenderer());
        tblInvoices.getColumnModel().getColumn(2).setCellRenderer(new StripeCenterRenderer());
        tblInvoices.getColumnModel().getColumn(3).setCellRenderer(new StripeCenterRenderer());
        tblInvoices.getColumnModel().getColumn(4).setCellRenderer(new StripeRightRenderer());
        tblInvoices.getColumnModel().getColumn(5).setCellRenderer(new StripeCenterRenderer());

        JScrollPane invScroll = new JScrollPane(tblInvoices);
        invScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        invScroll.getViewport().setBackground(Color.WHITE);
        invScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:10; trackArc:999; thumbInsets:2,2,2,2; trackInsets:2,2,2,2;");

        String[] detCols = {"Product", "Quantity", "Price", "Total"};
        detailModel = new DefaultTableModel(detCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetails = new JTable(detailModel);
        tblDetails.setRowHeight(40);
        tblDetails.setShowGrid(false);
        tblDetails.setIntercellSpacing(new Dimension(0, 0));
        tblDetails.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDetails.setForeground(new Color(50, 40, 35));
        tblDetails.setSelectionBackground(new Color(242, 236, 228));
        tblDetails.setSelectionForeground(DARK_BROWN);

        JTableHeader hdr2 = tblDetails.getTableHeader();
        hdr2.setReorderingAllowed(false);
        hdr2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        hdr2.setBackground(DARK_BROWN);
        hdr2.setForeground(Color.WHITE);
        hdr2.setPreferredSize(new Dimension(0, 44));

        tblDetails.getColumnModel().getColumn(0).setPreferredWidth(180);
        tblDetails.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblDetails.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblDetails.getColumnModel().getColumn(3).setPreferredWidth(120);

        tblDetails.getColumnModel().getColumn(0).setCellRenderer(new StripeCenterRenderer());
        tblDetails.getColumnModel().getColumn(1).setCellRenderer(new StripeCenterRenderer());
        tblDetails.getColumnModel().getColumn(2).setCellRenderer(new StripeRightRenderer());
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(new StripeRightRenderer());

        JScrollPane detScroll = new JScrollPane(tblDetails);
        detScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        detScroll.getViewport().setBackground(Color.WHITE);
        detScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:10; trackArc:999; thumbInsets:2,2,2,2; trackInsets:2,2,2,2;");

        JPanel invoicePanel = new JPanel(new BorderLayout());
        JLabel invTitle = new JLabel("Invoices");
        invTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        invTitle.setForeground(DARK_BROWN);
        invTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        invoicePanel.add(invTitle, BorderLayout.NORTH);
        invoicePanel.add(invScroll, BorderLayout.CENTER);

        JPanel detailPanel = new JPanel(new BorderLayout());
        JLabel detTitle = new JLabel("Invoice Details");
        detTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        detTitle.setForeground(DARK_BROWN);
        detTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        detailPanel.add(detTitle, BorderLayout.NORTH);
        detailPanel.add(detScroll, BorderLayout.CENTER);

        JPanel wrapper1 = new JPanel(new BorderLayout());
        wrapper1.setBackground(Color.WHITE);
        wrapper1.setBorder(new EmptyBorder(0, 30, 0, 10));
        wrapper1.add(invoicePanel, BorderLayout.CENTER);

        JPanel wrapper2 = new JPanel(new BorderLayout());
        wrapper2.setBackground(Color.WHITE);
        wrapper2.setBorder(new EmptyBorder(0, 10, 0, 30));
        wrapper2.add(detailPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wrapper1, wrapper2);
        split.setDividerLocation(500);
        split.setResizeWeight(0.6);
        split.setDividerSize(1);
        split.setContinuousLayout(true);
        split.setBorder(null);

        tblInvoices.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblInvoices.getSelectedRow();
                if (row >= 0 && row < invoiceIdList.size()) {
                    java.util.UUID id = invoiceIdList.get(row);
                    if (id != null) loadInvoiceDetails(id);
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(split, BorderLayout.CENTER);
        return centerPanel;
    }

    private void loadInvoiceDetails(java.util.UUID invoiceId) {
        detailModel.setRowCount(0);
        new SwingWorker<Void, Void>() {
            private List<Object[]> rows = new ArrayList<>();

            @Override
            protected Void doInBackground() {
                try {
                    rows = orderDetailDAO.findInvoiceDetails(invoiceId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                for (Object[] row : rows) {
                    detailModel.addRow(row);
                }
            }
        }.execute();
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
            if (!isSelected)
                c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_BG);
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
            if (!isSelected)
                c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_BG);
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(new EmptyBorder(0, 10, 0, 20));
            return c;
        }
    }
}
