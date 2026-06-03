package hanabi.view.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import hanabi.dao.TenantDAO;
import hanabi.model.Tenant;
import hanabi.util.MongoDBUtil;
import hanabi.util.TenantContext;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;

public class CreateCafePanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color CARD_BG = new Color(253, 251, 249);

    private final TenantDAO tenantDAO = new TenantDAO();
    private final Runnable onCreateCafe;
    private JPanel tenantListPanel;
    private JLabel countLabel;

    public CreateCafePanel(Runnable onCreateCafe) {
        this.onCreateCafe = onCreateCafe;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));
        init();
    }

    private void init() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Developer Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(DARK_BROWN);
        header.add(title, BorderLayout.WEST);

        JButton btnCreate = new JButton("+ Create New Cafe");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.setBackground(new Color(80, 160, 80));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.putClientProperty(FlatClientProperties.STYLE,
                "arc:12; borderWidth:0; focusWidth:0; innerFocusWidth:0; margin:6,14,6,14;");
        btnCreate.addActionListener(e -> {
            if (onCreateCafe != null) onCreateCafe.run();
        });
        header.add(btnCreate, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);

        countLabel = new JLabel("Loading...");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countLabel.setForeground(DARK_BROWN);
        countLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        body.add(countLabel, BorderLayout.NORTH);

        tenantListPanel = new JPanel();
        tenantListPanel.setLayout(new BoxLayout(tenantListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(tenantListPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        body.add(scroll, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        countLabel.setText("Loading...");
        tenantListPanel.removeAll();

        SwingWorker<List<Tenant>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Tenant> doInBackground() {
                return tenantDAO.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<Tenant> tenants = get();
                    countLabel.setText(tenants.size() + " Cafe(s) registered");
                    tenantListPanel.removeAll();
                    for (Tenant t : tenants) {
                        tenantListPanel.add(createTenantCard(t));
                    }
                    if (tenants.isEmpty()) {
                        JLabel empty = new JLabel("No cafes yet. Click \"+ Create New Cafe\" to get started.",
                                SwingConstants.CENTER);
                        empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                        empty.setForeground(new Color(180, 170, 160));
                        empty.setBorder(new EmptyBorder(40, 0, 40, 0));
                        tenantListPanel.add(empty);
                    }
                } catch (Exception e) {
                    countLabel.setText("Error loading cafes");
                }
                tenantListPanel.revalidate();
                tenantListPanel.repaint();
            }
        };
        worker.execute();
    }

    private JPanel createTenantCard(Tenant tenant) {
        JPanel card = new JPanel(new MigLayout("insets 15 20 15 20, fillx", "[grow, fill]"));
        card.setBackground(CARD_BG);
        card.putClientProperty(FlatClientProperties.STYLE, "arc:15; border: 1,1,1,1, #D5C5B5;");

        JPanel info = new JPanel(new MigLayout("wrap 2, fillx, insets 0", "[][grow]"));
        info.setOpaque(false);

        JLabel nameLabel = new JLabel("Cafe:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(DARK_BROWN);
        info.add(nameLabel);

        JLabel nameVal = new JLabel(tenant.getCafeName() != null ? tenant.getCafeName() : "");
        nameVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.add(nameVal);

        JLabel tenantLabel = new JLabel("Tenant:");
        tenantLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tenantLabel.setForeground(DARK_BROWN);
        info.add(tenantLabel);

        JLabel tenantVal = new JLabel(tenant.getTenantName());
        tenantVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.add(tenantVal);

        JLabel adminLabel = new JLabel("Admin:");
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminLabel.setForeground(DARK_BROWN);
        info.add(adminLabel);

        JLabel adminVal = new JLabel(tenant.getFullName() != null ? tenant.getFullName() : "");
        adminVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.add(adminVal);

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginLabel.setForeground(DARK_BROWN);
        info.add(loginLabel);

        JLabel loginVal = new JLabel(tenant.getTenantName() + " / admin");
        loginVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginVal.setForeground(new Color(80, 160, 80));
        info.add(loginVal);

        card.add(info, "cell 0 0");

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.setBackground(new Color(200, 70, 70));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.putClientProperty(FlatClientProperties.STYLE,
                "arc:10; borderWidth:0; focusWidth:0; innerFocusWidth:0; margin:4,12,4,12;");

        UUID tenantId = tenant.getTenantId();
        String tenantName = tenant.getTenantName();
        String cafeName = tenant.getCafeName() != null ? tenant.getCafeName() : tenantName;
        btnDelete.addActionListener(e -> deleteTenant(tenantId, cafeName));

        card.add(btnDelete, "cell 1 0, align right, gapx 10");
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        return card;
    }

    private void deleteTenant(UUID tenantId, String cafeName) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete \"" + cafeName + "\"?\n"
                        + "All data (staff, products, orders, invoices, salaries, messages) will be lost!\n"
                        + "This action cannot be undone!",
                "Delete Cafe", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String verify = JOptionPane.showInputDialog(this,
                "Type \"DELETE\" to confirm:",
                "Confirm Deletion", JOptionPane.WARNING_MESSAGE);
        if (verify == null || !"DELETE".equals(verify.trim())) {
            JOptionPane.showMessageDialog(this, "Deletion cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog loading = new JDialog();
        loading.setTitle("Deleting...");
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        JLabel lblLoading = new JLabel("Deleting cafe data...", SwingConstants.CENTER);
        lblLoading.setBorder(new EmptyBorder(30, 40, 30, 40));
        loading.add(lblLoading);
        loading.setSize(250, 120);
        loading.setLocationRelativeTo(this);
        loading.setModal(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                var db = MongoDBUtil.getDatabase();
                String prefix = MongoDBUtil.tenantPrefix(tenantId);
                String[] collections = {"staff", "products", "invoices", "orders", "orders_details", "salaries", "chat_messages"};
                for (String col : collections) {
                    String fullName = prefix + col;
                    if (collectionExists(db, fullName)) {
                        db.getCollection(fullName).drop();
                    }
                }
                tenantDAO.deleteById(tenantId);
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
                JOptionPane.showMessageDialog(CreateCafePanel.this,
                        "Cafe \"" + cafeName + "\" has been deleted.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refresh();
            }
        };
        worker.execute();
        loading.setVisible(true);
    }

    private boolean collectionExists(com.mongodb.client.MongoDatabase db, String name) {
        for (String colName : db.listCollectionNames()) {
            if (colName.equals(name)) return true;
        }
        return false;
    }
}
