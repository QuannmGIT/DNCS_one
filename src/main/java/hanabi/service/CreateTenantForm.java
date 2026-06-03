package hanabi.service;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import hanabi.dao.TenantDAO;
import hanabi.model.Tenant;
import hanabi.util.MongoDBUtil;
import hanabi.util.PasswordUtil;
import hanabi.util.TenantContext;
import java.util.UUID;
import java.awt.Component;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CreateTenantForm extends JPanel {

    private JTextField txtTenantName, txtCafeName, txtFullName, txtEmail;
    private JPasswordField txtPassword;
    private JButton cmdCreate;
    private static JFrame f;
    private final TenantDAO tenantDAO = new TenantDAO();
    private final Runnable onSuccess;

    public CreateTenantForm(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 420", "[fill]"));

        add(new JLabel(new FlatSVGIcon("hanabi/assets/icon/AccountIcon.svg")), "center");
        add(new JSeparator(), "gapy 15 15");

        JLabel lblTenant = new JLabel("Cafe / Tenant name *");
        lblTenant.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lblTenant, "gapy 10 n");
        txtTenantName = new JTextField();
        txtTenantName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. hanabi");
        add(txtTenantName);

        JLabel lblCafe = new JLabel("Cafe display name *");
        lblCafe.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lblCafe, "gapy 10 n");
        txtCafeName = new JTextField();
        txtCafeName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. HANABI CAFE");
        add(txtCafeName);

        JLabel lblName = new JLabel("Admin full name *");
        lblName.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lblName, "gapy 10 n");
        txtFullName = new JTextField();
        txtFullName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. Nguyen Van A");
        add(txtFullName);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lblEmail, "split 2, gapy 10 n");
        add(optionalTag(), "right, gapx 200");

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "example@mail.com");
        add(txtEmail);

        JLabel lblPass = new JLabel("Admin password *");
        lblPass.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lblPass, "gapy 10 n");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "at least 8 characters");
        add(txtPassword);

        cmdCreate = new JButton("Create Cafe") {
            @Override
            public boolean isDefaultButton() { return true; }
        };
        cmdCreate.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        add(cmdCreate, "gapy 10 n");

        cmdCreate.addActionListener(e -> createTenant());
    }

    private Component optionalTag() {
        JLabel otpLabel = new JLabel("Optional");
        otpLabel.putClientProperty(FlatClientProperties.STYLE, "font:italic; foreground:#929493;");
        return otpLabel;
    }

    private void createTenant() {
        String tenantName = txtTenantName.getText().trim();
        String cafeName = txtCafeName.getText().trim();
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (tenantName.isEmpty() || cafeName.isEmpty() || fullName.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tenantDAO.findByTenantName(tenantName).isPresent()) {
            JOptionPane.showMessageDialog(this, "Tenant name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            UUID tenantId = UUID.randomUUID();
            String salt = PasswordUtil.generateSalt();

            Tenant tenant = new Tenant();
            tenant.setTenantId(tenantId);
            tenant.setTenantName(tenantName);
            tenant.setCafeName(cafeName);
            tenant.setFullName(fullName);
            tenant.setPassword(salt + ":" + PasswordUtil.hash(pass, salt));
            tenant.setEmail(email.isEmpty() ? null : email);
            tenant.setStatus(true);
            tenantDAO.save(tenant);

            TenantContext.setCurrentTenantId(tenantId);
            try {
                var db = MongoDBUtil.getDatabase();
                String prefix = MongoDBUtil.tenantPrefix(tenantId);
                for (String col : new String[]{"staff", "products", "invoices", "orders", "orders_details", "salaries", "chat_messages"}) {
                    if (!collectionExists(db, prefix + col)) {
                        db.createCollection(prefix + col);
                    }
                }

                var staffCol = db.getCollection(prefix + "staff", hanabi.model.Staff.class);
                var salaryCol = db.getCollection(prefix + "salaries", hanabi.model.Salary.class);

                hanabi.model.Staff adminStaff = new hanabi.model.Staff();
                adminStaff.setStaffId(tenantId);
                adminStaff.setStaffName("admin");
                adminStaff.setFullName(fullName);
                String adminSalt = PasswordUtil.generateSalt();
                adminStaff.setPassword(adminSalt + ":" + PasswordUtil.hash("admin", adminSalt));
                adminStaff.setRole("admin");
                adminStaff.setStatus(true);
                staffCol.insertOne(adminStaff);

                hanabi.model.Salary adminSalary = new hanabi.model.Salary();
                adminSalary.setStaffId(tenantId);
                adminSalary.setBaseSalary(0.0);
                adminSalary.setCommissionRate(0.0);
                salaryCol.insertOne(adminSalary);
            } finally {
                TenantContext.clear();
            }

            JOptionPane.showMessageDialog(this,
                    "Cafe \"" + cafeName + "\" created successfully!\n"
                    + "Tenant: " + tenantName + "\n"
                    + "Admin login: " + tenantName + " / admin",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            if (onSuccess != null) onSuccess.run();
            if (f != null) f.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating tenant: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean collectionExists(com.mongodb.client.MongoDatabase db, String name) {
        for (String colName : db.listCollectionNames()) {
            if (colName.equals(name)) return true;
        }
        return false;
    }

    public static void show(Runnable onSuccess) {
        f = new JFrame("Create New Cafe / Tenant");
        f.add(new CreateTenantForm(onSuccess));
        f.setSize(450, 520);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
