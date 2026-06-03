package hanabi.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import hanabi.dao.InvoiceDAO;
import hanabi.dao.OrderDAO;
import hanabi.dao.OrderDetailDAO;
import hanabi.dao.ProductDAO;
import hanabi.model.Invoice;
import hanabi.model.Order;
import hanabi.model.OrderDetail;
import hanabi.model.Product;
import hanabi.model.Staff;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;

import hanabi.util.global;
import hanabi.view.ui.MenuItemsPanel;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class MenuService {
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productDAO.findAvailable();
    }

    public List<Product> getProductsByCategory(String category) {
        return productDAO.findByCategory(category);
    }

    public void addProduct(Product product) {
        productDAO.save(product);
    }

    public UUID placeOrder(Staff staff, Map<String, int[]> cartItems, Map<String, Double> productPrices) {
        UUID orderId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();
        int total = 0;

        for (Map.Entry<String, int[]> entry : cartItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue()[0];
            double price = productPrices.getOrDefault(productName, 0.0);
            total += (int) (price * quantity);
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setStaffId(staff.getStaffId());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setTotal(total);
        invoice.setStatus(true);
        invoiceDAO.save(invoice);

        Order order = new Order();
        order.setOrderId(orderId);
        order.setStaffId(staff.getStaffId());
        order.setOrderDate(LocalDate.now());
        order.setInvoiceId(invoiceId);
        order.setTotal(total);
        order.setStatus(true);
        orderDAO.save(order);

        for (Map.Entry<String, int[]> entry : cartItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue()[0];
            Product product = productDAO.findByName(productName).orElse(null);
            if (product != null) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(orderId);
                detail.setProductId(product.getProductId());
                detail.setQuantity(quantity);
                orderDetailDAO.save(detail);
            }
        }

        return orderId;
    }

    public void QRPayment(int totalAmount, Staff staff, MenuItemsPanel panel) {
        JDialog qrDialog = new JDialog(
                (JFrame) SwingUtilities.getWindowAncestor(panel),
                "VietQR payment", true);
        qrDialog.setLayout(new BorderLayout());
        qrDialog.setSize(400, 500);
        qrDialog.setLocationRelativeTo(panel);
        qrDialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(" Scan the QR code to pay", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(90, 70, 61));
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        qrDialog.add(lblTitle, BorderLayout.NORTH);

        JLabel lblQR = new JLabel("Loading QR code...", SwingConstants.CENTER);
        lblQR.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        qrDialog.add(lblQR, BorderLayout.CENTER);

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String encodedName = URLEncoder.encode(global.ACCOUNTNAME, StandardCharsets.UTF_8.toString()).replace(
                        "+",
                        "%20");
                String encodedInfo = URLEncoder.encode(global.ADDINFO, StandardCharsets.UTF_8.toString()).replace("+",
                        "%20");

                String urlString = String.format(
                        "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s&accountName=%s",
                        global.BANKCODE, global.ACCOUNTNUMBER, totalAmount, encodedInfo, encodedName);

                URL url = new URL(urlString);
                Image img = ImageIO.read(url);
                Image scaledImg = img.getScaledInstance(300, 350, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }

            @Override
            protected void done() {
                try {
                    lblQR.setText("");
                    lblQR.setIcon(get());
                } catch (Exception e) {
                    lblQR.setText("Network Error: Can't load QR code");
                }
            }
        }.execute();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton btnConfirm = new JButton("Confirm money received");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setBackground(new Color(46, 204, 113));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; focusWidth:0; padding:10,20,10,20");

        btnConfirm.addActionListener(e -> {
            qrDialog.dispose();

            Map<String, Double> productPrices = new java.util.HashMap<>();
            for (Map.Entry<String, int[]> entry : panel.getCartMap().entrySet()) {
                productPrices.put(entry.getKey(), (double) entry.getValue()[1]);
            }

            java.util.UUID orderId = placeOrder(staff, panel.getCartMap(), productPrices);

            if (orderId != null) {
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(panel),
                        "Payment successful!\nOrder code: " + orderId.toString().substring(0, 8),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                panel.clearCart();
            }
        });

        bottomPanel.add(btnConfirm);
        qrDialog.add(bottomPanel, BorderLayout.SOUTH);

        qrDialog.setVisible(true);
    };

    public boolean deleteProduct(UUID productId) {
        Product product = productDAO.findById(productId);
        if (product != null) {
            product.setStatus(false);
            productDAO.update(product, productId);
            return true;
        }
        return false;
    }

    public boolean hardDeleteProduct(UUID productId) {
        Product product = productDAO.findById(productId);
        if (product != null) {
            productDAO.delete(productId);
            return true;
        }
        return false;
    }

    public Product getProductById(UUID productId) {
        return productDAO.findById(productId);
    }

    public void updateProduct(Product product) {
        productDAO.update(product, product.getProductId());
    }
}
