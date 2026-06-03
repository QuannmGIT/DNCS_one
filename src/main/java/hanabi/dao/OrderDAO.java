package hanabi.dao;

import hanabi.model.Order;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDAO extends BaseDAO<Order> {

    @Override
    protected String tableName() { return "orders"; }

    @Override
    protected String idColumn() { return "order_id"; }

    private final RowMapper<Order> mapper = this::mapRow;

    @Override
    protected RowMapper<Order> rowMapper() { return mapper; }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(UUID.fromString(rs.getString("order_id")));
        o.setInvoiceId(UUID.fromString(rs.getString("invoice_id")));
        o.setStaffId(UUID.fromString(rs.getString("staff_id")));
        o.setStatus(rs.getBoolean("status"));
        if (rs.wasNull()) o.setStatus(null);
        o.setOrderDate(rs.getObject("order_date", LocalDate.class));
        o.setTotal(rs.getInt("total"));
        if (rs.wasNull()) o.setTotal(null);
        return o;
    }

    public List<Order> findByStaffId(UUID staffId) {
        return SupabaseUtil.queryList(
                "SELECT * FROM orders WHERE staff_id = ?::uuid ORDER BY order_date DESC",
                rowMapper(), staffId);
    }

    public List<Order> findByDate(LocalDate date) {
        return SupabaseUtil.queryList(
                "SELECT * FROM orders WHERE order_date = ? ORDER BY order_date DESC",
                rowMapper(), date);
    }

    public List<Order> findRecent(int limit) {
        return SupabaseUtil.queryList(
                "SELECT * FROM orders ORDER BY order_date DESC, order_id DESC LIMIT ?",
                rowMapper(), limit);
    }

    public long countByStaffId(UUID staffId) {
        Long result = SupabaseUtil.querySingle(
                "SELECT COUNT(*) AS cnt FROM orders WHERE staff_id = ?::uuid",
                rs -> rs.getLong("cnt"), staffId);
        return result != null ? result : 0L;
    }

    public long countAll() {
        Long result = SupabaseUtil.querySingle(
                "SELECT COUNT(*) AS cnt FROM orders",
                rs -> rs.getLong("cnt"));
        return result != null ? result : 0L;
    }

    public List<Order> findFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId, int offset, int limit) {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (fromDate != null) { conditions.add("order_date >= ?"); params.add(fromDate); }
        if (toDate != null) { conditions.add("order_date <= ?"); params.add(toDate); }
        if (staffId != null) { conditions.add("staff_id = ?::uuid"); params.add(staffId); }

        String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
        params.add(limit);
        params.add(offset);
        String sql = "SELECT * FROM orders" + where + " ORDER BY order_date DESC, order_id DESC LIMIT ? OFFSET ?";
        return SupabaseUtil.queryList(sql, rowMapper(), params.toArray());
    }

    public long countFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId) {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (fromDate != null) { conditions.add("order_date >= ?"); params.add(fromDate); }
        if (toDate != null) { conditions.add("order_date <= ?"); params.add(toDate); }
        if (staffId != null) { conditions.add("staff_id = ?::uuid"); params.add(staffId); }

        String where = conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
        Long result = SupabaseUtil.querySingle(
                "SELECT COUNT(*) AS cnt FROM orders" + where,
                rs -> rs.getLong("cnt"), params.toArray());
        return result != null ? result : 0L;
    }

    public void save(Order order) {
        SupabaseUtil.update(
                "INSERT INTO orders (order_id, invoice_id, staff_id, status, order_date, total) VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?)",
                order.getOrderId(), order.getInvoiceId(), order.getStaffId(),
                order.getStatus(), order.getOrderDate(), order.getTotal());
    }

    public void update(Order order, UUID id) {
        SupabaseUtil.update(
                "UPDATE orders SET invoice_id=?::uuid, staff_id=?::uuid, status=?, order_date=?, total=? WHERE order_id=?::uuid",
                order.getInvoiceId(), order.getStaffId(), order.getStatus(),
                order.getOrderDate(), order.getTotal(), id);
    }
}
