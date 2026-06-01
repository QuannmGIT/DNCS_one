package hanabi.service;

import hanabi.dao.InvoiceDAO;
import hanabi.dao.OrderDAO;
import hanabi.dao.OrderDetailDAO;
import hanabi.model.Invoice;
import hanabi.model.Order;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RevenueService {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

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

    public List<Order> getFilteredOrders(LocalDate fromDate, LocalDate toDate, UUID staffId, int offset, int limit) {
        return orderDAO.findFiltered(fromDate, toDate, staffId, offset, limit);
    }

    public long countFilteredOrders(LocalDate fromDate, LocalDate toDate, UUID staffId) {
        return orderDAO.countFiltered(fromDate, toDate, staffId);
    }

    public List<Invoice> getTodayInvoices() {
        return invoiceDAO.findByDate(LocalDate.now());
    }

    public Map<String, Long> getMonthlyRevenue(int year) {
        List<Invoice> invoices = invoiceDAO.findByDateRange(
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31));
        Map<String, Long> monthlyMap = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            monthlyMap.put(String.format("%d-%02d", year, m), 0L);
        }
        for (Invoice inv : invoices) {
            if (inv.getInvoiceDate() != null && Boolean.TRUE.equals(inv.getStatus())) {
                String key = inv.getInvoiceDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                monthlyMap.merge(key, inv.getTotal() == null ? 0L : inv.getTotal().longValue(), Long::sum);
            }
        }
        return monthlyMap;
    }
}
