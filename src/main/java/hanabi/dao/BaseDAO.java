package hanabi.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import hanabi.util.MongoDBUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {

    private final Class<T> entityClass;
    private final String collectionName;

    public BaseDAO(Class<T> entityClass, String collectionName) {
        this.entityClass = entityClass;
        this.collectionName = collectionName;
    }

    protected MongoCollection<T> getCollection() {
        return MongoDBUtil.getCollection(collectionName, entityClass);
    }

    public void save(T entity) {
        getCollection().insertOne(entity);
    }

    public void update(T entity, Object id) {
        getCollection().replaceOne(Filters.eq("_id", id), entity);
    }

    public void delete(Object id) {
        getCollection().deleteOne(Filters.eq("_id", id));
    }

    public T findById(Object id) {
        return getCollection().find(Filters.eq("_id", id)).first();
    }

    public List<T> findAll() {
        return getCollection().find().into(new ArrayList<>());
    }
}