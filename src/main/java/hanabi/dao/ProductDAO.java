package hanabi.dao;

import com.mongodb.client.model.Filters;
import hanabi.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductDAO extends BaseDAO<Product> {

    public ProductDAO() {
        super(Product.class, "products");
    }

    public List<Product> findByCategory(String category) {
        return getCollection().find(Filters.eq("category", category))
                .into(new ArrayList<>());
    }

    public Optional<Product> findByName(String name) {
        Product product = getCollection().find(Filters.eq("productName", name)).first();
        return Optional.ofNullable(product);
    }

    public List<Product> findAvailable() {
        return getCollection().find(Filters.eq("status", true))
                .into(new ArrayList<>());
    }
}