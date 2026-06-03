package hanabi.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import hanabi.model.Tenant;
import hanabi.util.MongoDBUtil;
import hanabi.util.PasswordUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TenantDAO {

    public TenantDAO() {}

    private MongoCollection<Tenant> getCollection() {
        return MongoDBUtil.getDatabase().getCollection("tenants", Tenant.class);
    }

    public Optional<Tenant> authenticate(String tenantName, String password) {
        Optional<Tenant> result = findByTenantName(tenantName);
        if (result.isEmpty()) return Optional.empty();
        Tenant tenant = result.get();
        if (tenant.getStatus() == null || !tenant.getStatus()) return Optional.empty();
        String stored = tenant.getPassword();
        if (stored == null) return Optional.empty();
        if (stored.contains(":")) {
            String[] parts = stored.split(":", 2);
            if (PasswordUtil.verify(password, parts[0], parts[1])) {
                return Optional.of(tenant);
            }
        } else if (stored.equals(password)) {
            String salt = PasswordUtil.generateSalt();
            tenant.setPassword(salt + ":" + PasswordUtil.hash(password, salt));
            getCollection().replaceOne(Filters.eq("_id", tenant.getTenantId()), tenant);
            return Optional.of(tenant);
        }
        return Optional.empty();
    }

    public Optional<Tenant> findByTenantName(String tenantName) {
        Tenant tenant = getCollection().find(Filters.eq("tenantName", tenantName)).first();
        return Optional.ofNullable(tenant);
    }

    public Optional<Tenant> findById(UUID tenantId) {
        Tenant tenant = getCollection().find(Filters.eq("_id", tenantId)).first();
        return Optional.ofNullable(tenant);
    }

    public void save(Tenant tenant) {
        getCollection().insertOne(tenant);
    }

    public List<Tenant> findAll() {
        return getCollection().find().into(new ArrayList<>());
    }

    public void deleteById(UUID tenantId) {
        getCollection().deleteOne(Filters.eq("_id", tenantId));
    }
}
