package hanabi.dao;

import hanabi.model.OrderDetail;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDetailDAO extends BaseDAO<OrderDetail> {

    @Override
    protected String tableName() { return "orders_details"; }

    @Override
    protected String idColumn() { return "order_id"; }

    private final RowMapper<OrderDetail> mapper = this::mapRow;

    @Override
    protected RowMapper<OrderDetail> rowMapper() { return mapper; }

    private OrderDetail mapRow(ResultSet rs) throws SQLException {
        OrderDetail d = new OrderDetail();
        d.setOrderId(UUID.fromString(rs.getString("order_id")));
        d.setProductId(UUID.fromString(rs.getString("product_id")));
        d.setQuantity(rs.getInt("quantity"));
        return d;
    }

    public List<OrderDetail> findByOrderId(UUID orderId) {
        return SupabaseUtil.queryList(
                "SELECT * FROM orders_details WHERE order_id = ?::uuid",
                rowMapper(), orderId);
    }

    public List<Object[]> findTopSelling(int limit) {
        List<Object[]> top = new ArrayList<>();
        List<Object[]> results = SupabaseUtil.queryList(
                "SELECT p.product_name, COALESCE(SUM(od.quantity), 0) AS total_qty " +
                "FROM orders_details od JOIN products p ON od.product_id = p.product_id " +
                "GROUP BY p.product_name ORDER BY total_qty DESC LIMIT ?",
                rs -> new Object[]{ rs.getString("product_name"), rs.getInt("total_qty") },
                limit);
        // Ensure all results are non-null
        for (Object[] row : results) {
            if (row[0] != null && row[1] != null) {
                top.add(row);
            }
        }
        return top;
    }

    public List<Object[]> findInvoiceDetails(UUID invoiceId) {
        return SupabaseUtil.queryList(
                "SELECT p.product_name, od.quantity, p.price, (od.quantity * p.price) AS subtotal " +
                "FROM orders o " +
                "JOIN orders_details od ON o.order_id = od.order_id " +
                "JOIN products p ON od.product_id = p.product_id " +
                "WHERE o.invoice_id = ?::uuid " +
                "ORDER BY p.product_name",
                rs -> new Object[]{
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getDouble("subtotal")
                },
                invoiceId);
    }

    public void save(OrderDetail detail) {
        SupabaseUtil.update(
                "INSERT INTO orders_details (order_id, product_id, quantity) VALUES (?::uuid, ?::uuid, ?)",
                detail.getOrderId(), detail.getProductId(), detail.getQuantity());
    }

    public void update(OrderDetail detail, Object id) {
        SupabaseUtil.update(
                "UPDATE orders_details SET quantity=? WHERE order_id=?::uuid AND product_id=?::uuid",
                detail.getQuantity(), detail.getOrderId(), detail.getProductId());
    }

    @Override
    public void delete(Object id) {
        // id is expected to be OrderDetailId
        if (id instanceof OrderDetail.OrderDetailId) {
            OrderDetail.OrderDetailId oid = (OrderDetail.OrderDetailId) id;
            SupabaseUtil.update(
                    "DELETE FROM orders_details WHERE order_id=?::uuid AND product_id=?::uuid",
                    oid.getOrderId(), oid.getProductId());
        }
    }
}
