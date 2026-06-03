package hanabi.dao;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import hanabi.model.OrderDetail;
import hanabi.model.Product;
import hanabi.util.MongoDBUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.conversions.Bson;

public class OrderDetailDAO extends BaseDAO<OrderDetail> {

    public OrderDetailDAO() {
        super(OrderDetail.class, "orders_details");
    }

    public List<OrderDetail> findByOrderId(UUID orderId) {
        return getCollection().find(Filters.eq("orderId", orderId))
                .into(new ArrayList<>());
    }

    public List<Object[]> findTopSelling(int limit) {
        List<Document> results = getCollection().withDocumentClass(Document.class).aggregate(List.of(
                Aggregates.lookup("products", "productId", "productId", "product"),
                Aggregates.unwind("$product"),
                Aggregates.group("$product.productName", Accumulators.sum("totalQty", "$quantity")),
                Aggregates.sort(Sorts.descending("totalQty")),
                Aggregates.limit(limit)
        )).into(new ArrayList<>());

        List<Object[]> top = new ArrayList<>();
        for (Document doc : results) {
            top.add(new Object[]{
                doc.getString("_id"),
                doc.getInteger("totalQty", 0)
            });
        }
        return top;
    }

    public List<Object[]> findInvoiceDetails(UUID invoiceId) {
        List<Document> results = MongoDBUtil.getCollection("orders").withDocumentClass(Document.class)
                .aggregate(List.of(
                        Aggregates.match(Filters.eq("invoiceId", invoiceId)),
                        Aggregates.lookup("orders_details", "orderId", "orderId", "details"),
                        Aggregates.unwind("$details"),
                        Aggregates.lookup("products", "details.productId", "productId", "product"),
                        Aggregates.unwind("$product"),
                        Aggregates.project(
                                com.mongodb.client.model.Projections.fields(
                                        com.mongodb.client.model.Projections.include("product.productName"),
                                        com.mongodb.client.model.Projections.include("details.quantity"),
                                        com.mongodb.client.model.Projections.computed("price", "$product.price"),
                                        com.mongodb.client.model.Projections.computed("subtotal",
                                                new Document("$multiply", List.of("$details.quantity", "$product.price")))
                                )
                        )
                )).into(new ArrayList<>());

        List<Object[]> details = new ArrayList<>();
        for (Document doc : results) {
            Document product = doc.get("product", Document.class);
            Document det = doc.get("details", Document.class);
            String productName = product != null ? product.getString("productName") : "N/A";
            int qty = det != null ? det.getInteger("quantity", 0) : 0;
            double price = product != null ? product.getDouble("price") : 0.0;
            double subtotal = qty * price;
            details.add(new Object[]{productName, qty, price, subtotal});
        }
        return details;
    }
}