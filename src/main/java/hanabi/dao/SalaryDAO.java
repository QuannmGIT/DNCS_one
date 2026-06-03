package hanabi.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import hanabi.model.Salary;
import hanabi.model.Staff;
import hanabi.util.MongoDBUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.Document;

public class SalaryDAO extends BaseDAO<Salary> {

    public SalaryDAO() {
        super(Salary.class, "salaries");
    }

    public List<Object[]> findAllWithStaff() {
        MongoCollection<Staff> staffCol = MongoDBUtil.getCollection("staff", Staff.class);
        MongoCollection<Salary> salaryCol = getCollection();

        List<Staff> allStaff = staffCol.find().into(new ArrayList<>());
        List<Object[]> result = new ArrayList<>();

        for (Staff s : allStaff) {
            Salary sa = salaryCol.find(Filters.eq("staffId", s.getStaffId())).first();
            result.add(new Object[]{
                s.getStaffId(),
                s.getFullName(),
                s.getRole(),
                sa != null ? sa.getBaseSalary() : 0.0,
                sa != null ? sa.getCommissionRate() : 0.0
            });
        }
        return result;
    }

    public List<Object[]> findAllWithStaffAndTotals() {
        MongoCollection<Staff> staffCol = MongoDBUtil.getCollection("staff", Staff.class);
        MongoCollection<Salary> salaryCol = getCollection();
        MongoCollection<Document> invoiceCol = MongoDBUtil.getCollection("invoices");

        List<Staff> allStaff = staffCol.find().into(new ArrayList<>());
        List<Object[]> result = new ArrayList<>();

        for (Staff s : allStaff) {
            Salary sa = salaryCol.find(Filters.eq("staffId", s.getStaffId())).first();

            List<Document> invoiceAgg = invoiceCol.aggregate(List.of(
                    Aggregates.match(Filters.and(
                            Filters.eq("staffId", s.getStaffId()),
                            Filters.eq("status", true)
                    )),
                    Aggregates.group(null, com.mongodb.client.model.Accumulators.sum("total", "$total"))
            )).into(new ArrayList<>());

            double monthlyTotal = 0;
            if (!invoiceAgg.isEmpty()) {
                Number t = invoiceAgg.get(0).get("total", Number.class);
                if (t != null) monthlyTotal = t.doubleValue();
            }

            double base = sa != null && sa.getBaseSalary() != null ? sa.getBaseSalary() : 0.0;
            double total = base + 0.1 * monthlyTotal;

            result.add(new Object[]{
                s.getStaffId(),
                s.getFullName(),
                s.getRole(),
                base,
                monthlyTotal,
                total
            });
        }
        return result;
    }

    public Double getTotalByStaffId(UUID staffId) {
        Salary sa = findById(staffId);
        double base = sa != null && sa.getBaseSalary() != null ? sa.getBaseSalary() : 0.0;

        MongoCollection<Document> invoiceCol = MongoDBUtil.getCollection("invoices");
        List<Document> agg = invoiceCol.aggregate(List.of(
                Aggregates.match(Filters.and(
                        Filters.eq("staffId", staffId),
                        Filters.eq("status", true)
                )),
                Aggregates.group(null, com.mongodb.client.model.Accumulators.sum("total", "$total"))
        )).into(new ArrayList<>());

        double monthlyTotal = 0;
        if (!agg.isEmpty()) {
            Number t = agg.get(0).get("total", Number.class);
            if (t != null) monthlyTotal = t.doubleValue();
        }

        return base + 0.1 * monthlyTotal;
    }
}