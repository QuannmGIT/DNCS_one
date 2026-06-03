package hanabi.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import hanabi.model.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.conversions.Bson;

public class OrderDAO extends BaseDAO<Order> {

    public OrderDAO() {
        super(Order.class, "orders");
    }

    public List<Order> findByStaffId(UUID staffId) {
        return getCollection().find(Filters.eq("staffId", staffId))
                .sort(Sorts.descending("orderDate"))
                .into(new ArrayList<>());
    }

    public List<Order> findByDate(LocalDate date) {
        return getCollection().find(Filters.eq("orderDate", date))
                .sort(Sorts.descending("orderDate"))
                .into(new ArrayList<>());
    }

    public List<Order> findRecent(int limit) {
        return getCollection().find()
                .sort(Sorts.descending("orderDate", "orderId"))
                .limit(limit)
                .into(new ArrayList<>());
    }

    public long countByStaffId(UUID staffId) {
        return getCollection().countDocuments(Filters.eq("staffId", staffId));
    }

    public long countAll() {
        return getCollection().countDocuments();
    }

    public List<Order> findFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId, int offset, int limit) {
        List<Bson> filters = new ArrayList<>();
        if (fromDate != null) filters.add(Filters.gte("orderDate", fromDate));
        if (toDate != null) filters.add(Filters.lte("orderDate", toDate));
        if (staffId != null) filters.add(Filters.eq("staffId", staffId));

        var find = getCollection().find(
                filters.isEmpty() ? Filters.empty() : Filters.and(filters)
        ).sort(Sorts.descending("orderDate", "orderId"))
         .skip(offset)
         .limit(limit);

        return find.into(new ArrayList<>());
    }

    public long countFiltered(LocalDate fromDate, LocalDate toDate, UUID staffId) {
        List<Bson> filters = new ArrayList<>();
        if (fromDate != null) filters.add(Filters.gte("orderDate", fromDate));
        if (toDate != null) filters.add(Filters.lte("orderDate", toDate));
        if (staffId != null) filters.add(Filters.eq("staffId", staffId));

        return getCollection().countDocuments(
                filters.isEmpty() ? Filters.empty() : Filters.and(filters)
        );
    }
}