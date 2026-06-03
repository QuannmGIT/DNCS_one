package hanabi.dao;

import hanabi.model.Salary;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SalaryDAO extends BaseDAO<Salary> {

    @Override
    protected String tableName() { return "salaries"; }

    @Override
    protected String idColumn() { return "staff_id"; }

    private final RowMapper<Salary> mapper = this::mapRow;

    @Override
    protected RowMapper<Salary> rowMapper() { return mapper; }

    private Salary mapRow(ResultSet rs) throws SQLException {
        Salary s = new Salary();
        s.setStaffId(UUID.fromString(rs.getString("staff_id")));
        s.setBaseSalary(rs.getDouble("base_salary"));
        s.setCommissionRate(rs.getDouble("commission_rate"));
        return s;
    }

    public List<Object[]> findAllWithStaff() {
        return SupabaseUtil.queryList(
                "SELECT s.staff_id, s.full_name, s.role, " +
                "COALESCE(sa.base_salary, 0) AS base_salary, " +
                "COALESCE(sa.commission_rate, 0) AS commission_rate " +
                "FROM staff s LEFT JOIN salaries sa ON s.staff_id = sa.staff_id " +
                "ORDER BY s.full_name",
                rs -> new Object[]{
                    UUID.fromString(rs.getString("staff_id")),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getDouble("base_salary"),
                    rs.getDouble("commission_rate")
                });
    }

    public List<Object[]> findAllWithStaffAndTotals() {
        return SupabaseUtil.queryList(
                "SELECT s.staff_id, s.full_name, s.role, " +
                "COALESCE(sa.base_salary, 0) AS base_salary, " +
                "COALESCE(SUM(i.total) FILTER (WHERE i.status = true), 0) AS monthly_total " +
                "FROM staff s " +
                "LEFT JOIN salaries sa ON s.staff_id = sa.staff_id " +
                "LEFT JOIN invoices i ON s.staff_id = i.staff_id " +
                "GROUP BY s.staff_id, s.full_name, s.role, sa.base_salary " +
                "ORDER BY s.full_name",
                rs -> {
                    double base = rs.getDouble("base_salary");
                    double monthlyTotal = rs.getDouble("monthly_total");
                    double total = base + 0.1 * monthlyTotal;
                    return new Object[]{
                        UUID.fromString(rs.getString("staff_id")),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        base,
                        monthlyTotal,
                        total
                    };
                });
    }

    public Double getTotalByStaffId(UUID staffId) {
        return SupabaseUtil.querySingle(
                "SELECT COALESCE(sa.base_salary, 0) + 0.1 * COALESCE(SUM(i.total) FILTER (WHERE i.status = true), 0) AS total " +
                "FROM staff s " +
                "LEFT JOIN salaries sa ON s.staff_id = sa.staff_id " +
                "LEFT JOIN invoices i ON s.staff_id = i.staff_id " +
                "WHERE s.staff_id = ?::uuid " +
                "GROUP BY sa.base_salary",
                rs -> rs.getDouble("total"),
                staffId);
    }

    public void save(Salary salary) {
        SupabaseUtil.update(
                "INSERT INTO salaries (staff_id, base_salary, commission_rate) VALUES (?::uuid, ?, ?)",
                salary.getStaffId(), salary.getBaseSalary(), salary.getCommissionRate());
    }

    public void update(Salary salary, UUID id) {
        SupabaseUtil.update(
                "UPDATE salaries SET base_salary=?, commission_rate=? WHERE staff_id=?::uuid",
                salary.getBaseSalary(), salary.getCommissionRate(), id);
    }
}
