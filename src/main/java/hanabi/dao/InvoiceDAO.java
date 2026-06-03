package hanabi.dao;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import hanabi.model.Invoice;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.conversions.Bson;

public class InvoiceDAO extends BaseDAO<Invoice> {

    public InvoiceDAO() {
        super(Invoice.class, "invoices");
    }

    public List<Invoice> findByStaffId(UUID staffId) {
        return getCollection().find(Filters.eq("staffId", staffId))
                .sort(Sorts.descending("invoiceDate"))
                .into(new ArrayList<>());
    }

    public List<Invoice> findByDate(LocalDate date) {
        return getCollection().find(Filters.eq("invoiceDate", date))
                .into(new ArrayList<>());
    }

    public List<Invoice> findByDateRange(LocalDate start, LocalDate end) {
        return getCollection().find(
                Filters.and(Filters.gte("invoiceDate", start), Filters.lte("invoiceDate", end))
        ).sort(Sorts.ascending("invoiceDate")).into(new ArrayList<>());
    }

    public long totalRevenueToday() {
        List<Document> result = getCollection().withDocumentClass(Document.class).aggregate(Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.eq("invoiceDate", LocalDate.now()),
                        Filters.eq("status", true)
                )),
                Aggregates.group(null, Accumulators.sum("total", "$total"))
        )).into(new ArrayList<>());

        if (result.isEmpty()) return 0L;
        Number total = result.get(0).get("total", Number.class);
        return total == null ? 0L : total.longValue();
    }

    public long totalRevenueByDateRange(LocalDate start, LocalDate end) {
        List<Document> result = getCollection().withDocumentClass(Document.class).aggregate(Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.gte("invoiceDate", start),
                        Filters.lte("invoiceDate", end),
                        Filters.eq("status", true)
                )),
                Aggregates.group(null, Accumulators.sum("total", "$total"))
        )).into(new ArrayList<>());

        if (result.isEmpty()) return 0L;
        Number total = result.get(0).get("total", Number.class);
        return total == null ? 0L : total.longValue();
    }
}