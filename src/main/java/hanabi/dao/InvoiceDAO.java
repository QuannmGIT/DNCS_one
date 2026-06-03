package hanabi.dao;

import hanabi.model.Invoice;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InvoiceDAO extends BaseDAO<Invoice> {

    @Override
    protected String tableName() { return "invoices"; }

    @Override
    protected String idColumn() { return "invoice_id"; }

    private final RowMapper<Invoice> mapper = this::mapRow;

    @Override
    protected RowMapper<Invoice> rowMapper() { return mapper; }

    private Invoice mapRow(ResultSet rs) throws SQLException {
        Invoice i = new Invoice();
        i.setInvoiceId(UUID.fromString(rs.getString("invoice_id")));
        i.setStaffId(UUID.fromString(rs.getString("staff_id")));
        i.setInvoiceDate(rs.getObject("invoice_date", LocalDate.class));
        i.setTotal(rs.getInt("total"));
        if (rs.wasNull()) i.setTotal(null);
        i.setStatus(rs.getBoolean("status"));
        if (rs.wasNull()) i.setStatus(null);
        return i;
    }

    public List<Invoice> findByStaffId(UUID staffId) {
        return SupabaseUtil.queryList(
                "SELECT * FROM invoices WHERE staff_id = ?::uuid ORDER BY invoice_date DESC",
                rowMapper(), staffId);
    }

    public List<Invoice> findByDate(LocalDate date) {
        return SupabaseUtil.queryList(
                "SELECT * FROM invoices WHERE invoice_date = ?",
                rowMapper(), date);
    }

    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        return SupabaseUtil.queryList(
                "SELECT * FROM invoices WHERE invoice_date >= ? AND invoice_date <= ? ORDER BY invoice_date ASC",
                rowMapper(), start, end);
    }

    public long totalRevenueToday() {
        Long result = SupabaseUtil.querySingle(
                "SELECT COALESCE(SUM(total), 0) AS total FROM invoices WHERE invoice_date = CURRENT_DATE AND status = true",
                rs -> rs.getLong("total"));
        return result != null ? result : 0L;
    }

    public long totalRevenueByDateRange(LocalDate start, LocalDate end) {
        Long result = SupabaseUtil.querySingle(
                "SELECT COALESCE(SUM(total), 0) AS total FROM invoices WHERE invoice_date >= ? AND invoice_date <= ? AND status = true",
                rs -> rs.getLong("total"), start, end);
        return result != null ? result : 0L;
    }

    public void save(Invoice invoice) {
        SupabaseUtil.update(
                "INSERT INTO invoices (invoice_id, staff_id, invoice_date, total, status) VALUES (?::uuid, ?::uuid, ?, ?, ?)",
                invoice.getInvoiceId(), invoice.getStaffId(), invoice.getInvoiceDate(),
                invoice.getTotal(), invoice.getStatus());
    }

    public void update(Invoice invoice, UUID id) {
        SupabaseUtil.update(
                "UPDATE invoices SET staff_id=?::uuid, invoice_date=?, total=?, status=? WHERE invoice_id=?::uuid",
                invoice.getStaffId(), invoice.getInvoiceDate(), invoice.getTotal(),
                invoice.getStatus(), id);
    }
}
