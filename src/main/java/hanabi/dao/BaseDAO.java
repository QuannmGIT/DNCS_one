package hanabi.dao;

import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.util.List;

public abstract class BaseDAO<T> {

    protected abstract String tableName();
    protected abstract RowMapper<T> rowMapper();
    protected abstract String idColumn();

    public void executeSql(String sql, Object... params) {
        SupabaseUtil.update(sql, params);
    }

    public void delete(Object id) {
        SupabaseUtil.update("DELETE FROM " + tableName() + " WHERE " + idColumn() + " = ?::uuid", id);
    }

    public T findById(Object id) {
        return SupabaseUtil.querySingle(
                "SELECT * FROM " + tableName() + " WHERE " + idColumn() + " = ?::uuid",
                rowMapper(), id);
    }

    public List<T> findAll() {
        return SupabaseUtil.queryList("SELECT * FROM " + tableName() + " ORDER BY " + idColumn(), rowMapper());
    }
}
