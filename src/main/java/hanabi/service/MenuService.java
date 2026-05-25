package hanabi.service;

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

        Order order = new Order();
        order.setOrderId(orderId);
        order.setStaff(staff);
        order.setOrderDate(LocalDate.now());

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setStaff(staff);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setTotal(total);
        invoice.setStatus(true);
        invoiceDAO.save(invoice);

        order.setInvoice(invoice);
        for (Map.Entry<String, int[]> entry : cartItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue()[0];
            double price = productPrices.getOrDefault(productName, 0.0);
            total += (int) (price * quantity);
        }
        order.setTotal(total);
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
}
