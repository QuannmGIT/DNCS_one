package hanabi.components;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import hanabi.model.Product;
import hanabi.service.MenuService;
import hanabi.util.HibernateUtil;

public class ProductTable extends JTable {

    private final DefaultTableModel model;
    private final MenuService menuService = new MenuService();
    private final List<UUID> productIdList = new ArrayList<>();
    private final List<String> dbColumnNames = new ArrayList<>();
    private List<ColumnInfo> columnInfos = new ArrayList<>();
    private Runnable onRefreshListener;

    public ProductTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public void setOnRefreshListener(Runnable onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void refresh() {
        new SwingWorker<LoadResult, Void>() {
            @Override
            protected LoadResult doInBackground() {
                try {
                    List<ColumnInfo> columns = fetchColumnMetadata();
                    List<Product> products = menuService.getAllProducts();
                    return new LoadResult(columns, products);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    LoadResult result = get();
                    if (result != null) {
                        columnInfos = result.columns;
                        buildModel(result.columns);
                        loadProductData(result.products);
                    }
                    if (onRefreshListener != null) {
                        onRefreshListener.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private List<ColumnInfo> fetchColumnMetadata() throws Exception {
        List<ColumnInfo> columns = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.doReturningWork((Connection connection) -> {
                DatabaseMetaData meta = connection.getMetaData();
                try (ResultSet rs = meta.getColumns(null, null, "products", null)) {
                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        int dataType = rs.getInt("DATA_TYPE");
                        if (colName.equals("product_id")) continue;
                        if (dataType == java.sql.Types.BINARY || dataType == java.sql.Types.VARBINARY
                                || dataType == java.sql.Types.LONGVARBINARY) continue;
                        columns.add(new ColumnInfo(colName, toDisplayName(colName), toFieldName(colName)));
                    }
                }
                return null;
            });
        }
        return columns;
    }

    private String toDisplayName(String dbName) {
        StringBuilder sb = new StringBuilder();
        boolean cap = true;
        for (int i = 0; i < dbName.length(); i++) {
            char c = dbName.charAt(i);
            if (c == '_') {
                cap = true;
            } else {
                sb.append(cap ? Character.toUpperCase(c) : c);
                cap = false;
            }
        }
        return sb.toString();
    }

    private String toFieldName(String dbName) {
        StringBuilder sb = new StringBuilder();
        boolean underscore = false;
        for (int i = 0; i < dbName.length(); i++) {
            char c = dbName.charAt(i);
            if (c == '_') {
                underscore = true;
            } else {
                sb.append(underscore ? Character.toUpperCase(c) : c);
                underscore = false;
            }
        }
        return sb.toString();
    }

    private void buildModel(List<ColumnInfo> columns) {
        dbColumnNames.clear();
        String[] colNames = new String[columns.size() + 1];
        colNames[0] = "No";
        for (int i = 0; i < columns.size(); i++) {
            colNames[i + 1] = columns.get(i).displayName;
            dbColumnNames.add(columns.get(i).dbName);
        }
        model.setColumnIdentifiers(colNames);
    }

    private void loadProductData(List<Product> products) {
        model.setRowCount(0);
        productIdList.clear();
        int stt = 1;
        for (Product p : products) {
            Object[] row = new Object[columnInfos.size() + 1];
            row[0] = stt++;
            for (int i = 0; i < columnInfos.size(); i++) {
                row[i + 1] = getColumnValue(p, columnInfos.get(i));
            }
            model.addRow(row);
            productIdList.add(p.getProductId());
        }
    }

    private Object getColumnValue(Product p, ColumnInfo info) {
        try {
            Method getter = Product.class.getMethod("get" + Character.toUpperCase(info.fieldName.charAt(0))
                    + info.fieldName.substring(1));
            Object raw = getter.invoke(p);
            if (raw == null) return "";
            if (info.fieldName.equals("price") || info.fieldName.equals("cost")) {
                return String.format("%,.0f đ", ((Number) raw).doubleValue());
            }
            if (info.fieldName.equals("status")) {
                return Boolean.TRUE.equals(raw) ? "Active" : "Inactive";
            }
            return raw;
        } catch (Exception e) {
            return "";
        }
    }

    public UUID getProductIdAtRow(int row) {
        if (row >= 0 && row < productIdList.size()) {
            return productIdList.get(row);
        }
        return null;
    }

    private static class ColumnInfo {
        final String dbName;
        final String displayName;
        final String fieldName;

        ColumnInfo(String dbName, String displayName, String fieldName) {
            this.dbName = dbName;
            this.displayName = displayName;
            this.fieldName = fieldName;
        }
    }

    private static class LoadResult {
        final List<ColumnInfo> columns;
        final List<Product> products;

        LoadResult(List<ColumnInfo> columns, List<Product> products) {
            this.columns = columns;
            this.products = products;
        }
    }
}