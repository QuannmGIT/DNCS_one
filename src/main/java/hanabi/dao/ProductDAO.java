package hanabi.dao;

import hanabi.model.Product;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductDAO extends BaseDAO<Product> {

    @Override
    protected String tableName() { return "products"; }

    @Override
    protected String idColumn() { return "product_id"; }

    private final RowMapper<Product> mapper = this::mapRow;

    @Override
    protected RowMapper<Product> rowMapper() { return mapper; }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(UUID.fromString(rs.getString("product_id")));
        p.setProductName(rs.getString("product_name"));
        p.setCategory(rs.getString("category"));
        p.setPrice(rs.getDouble("price"));
        p.setCost(rs.getDouble("cost"));
        p.setImage(rs.getString("image"));
        p.setStatus(rs.getBoolean("status"));
        if (rs.wasNull()) p.setStatus(null);
        return p;
    }

    public List<Product> findByCategory(String category) {
        return SupabaseUtil.queryList(
                "SELECT * FROM products WHERE category = ? ORDER BY product_name",
                rowMapper(), category);
    }

    public Optional<Product> findByName(String name) {
        Product p = SupabaseUtil.querySingle(
                "SELECT * FROM products WHERE product_name = ?",
                rowMapper(), name);
        return Optional.ofNullable(p);
    }

    public List<Product> findAvailable() {
        return SupabaseUtil.queryList(
                "SELECT * FROM products WHERE status = true ORDER BY product_name",
                rowMapper());
    }

    public void save(Product product) {
        SupabaseUtil.update(
                "INSERT INTO products (product_id, product_name, category, price, cost, image, status) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                product.getProductId(), product.getProductName(), product.getCategory(),
                product.getPrice(), product.getCost(), product.getImage(), product.getStatus());
    }

    public void update(Product product, UUID id) {
        SupabaseUtil.update(
                "UPDATE products SET product_name=?, category=?, price=?, cost=?, image=?, status=? WHERE product_id=?::uuid",
                product.getProductName(), product.getCategory(), product.getPrice(),
                product.getCost(), product.getImage(), product.getStatus(), id);
    }
}
