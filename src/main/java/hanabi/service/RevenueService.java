package hanabi.service;

import hanabi.dao.AverageDAO;
import hanabi.dao.InvoiceDAO;
import hanabi.dao.OrderDAO;
import hanabi.dao.OrderDetailDAO;
import hanabi.dao.ProductDAO;
import hanabi.model.Invoice;
import hanabi.model.Order;
import hanabi.model.Product;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RevenueService {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private final AverageDAO averageDAO = new AverageDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public long getTodayRevenue() {
        return invoiceDAO.totalRevenueToday();
    }

    public long getTotalOrders() {
        return orderDAO.countAll();
    }

    public String getBestSeller() {
        List<Object[]> top = orderDetailDAO.findTopSelling(1);
        if (!top.isEmpty()) {
            return (String) top.get(0)[0];
        }
        return "";
    }

    public double getAverageRating() {
        return averageDAO.getAverageScore();
    }

    public List<Order> getRecentOrders(int limit) {
        return orderDAO.findRecent(limit);
    }

    public List<Object[]> getTopSellingProducts(int limit) {
        return orderDetailDAO.findTopSelling(limit);
    }

    public Map<LocalDate, Long> getRevenueByDateRange(LocalDate start, LocalDate end) {
        List<Invoice> invoices = invoiceDAO.findByDateRange(start, end);
        Map<LocalDate, Long> revenueMap = new LinkedHashMap<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            revenueMap.put(date, 0L);
        }
        for (Invoice inv : invoices) {
            if (inv.getInvoiceDate() != null && Boolean.TRUE.equals(inv.getStatus())) {
                revenueMap.merge(inv.getInvoiceDate(),
                        inv.getTotal() == null ? 0L : inv.getTotal().longValue(), Long::sum);
            }
        }
        return revenueMap;
    }

    public List<Invoice> getTodayInvoices() {
        return invoiceDAO.findByDate(LocalDate.now());
    }
}
