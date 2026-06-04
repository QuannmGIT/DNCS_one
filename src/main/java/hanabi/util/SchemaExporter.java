package hanabi.util;

import hanabi.model.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.*;

public class SchemaExporter {

    private static final String SCHEMAS_DIR = "src/main/java/schemas";

    public static void main(String[] args) throws Exception {
        exportAll();
        System.out.println("All schema files generated in " + SCHEMAS_DIR);
    }

    public static void exportAll() throws Exception {
        Map<String, Class<?>> tables = new LinkedHashMap<>();
        tables.put("tenants", Tenant.class);
        tables.put("staff", Staff.class);
        tables.put("products", Product.class);
        tables.put("invoices", Invoice.class);
        tables.put("orders", Order.class);
        tables.put("orders_details", OrderDetail.class);
        tables.put("salaries", Salary.class);
        tables.put("chat_messages", ChatMessage.class);

        Path schemasPath = Paths.get(SCHEMAS_DIR);
        if (!Files.exists(schemasPath)) {
            Files.createDirectories(schemasPath);
        }

        for (Map.Entry<String, Class<?>> entry : tables.entrySet()) {
            String sql = generateCreateTable(entry.getKey(), entry.getValue());
            Path sqlFile = schemasPath.resolve(entry.getKey() + ".sql");
            Files.writeString(sqlFile, sql);
            System.out.println("  " + entry.getKey() + ".sql");
        }
    }

    private static String generateCreateTable(String tableName, Class<?> modelClass) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n-- Structure for table `").append(tableName).append("`\n\n");
        sb.append("CREATE TABLE `").append(tableName).append("` (\n");

        List<Field> fields = getModelFields(modelClass);
        List<String> columnDefs = new ArrayList<>();
        List<String> extras = new ArrayList<>();

        if (modelClass == OrderDetail.class) {
            columnDefs.add("  `order_id` BINARY(16) NOT NULL");
            columnDefs.add("  `product_id` BINARY(16) NOT NULL");
            columnDefs.add("  `quantity` INT");
            extras.add("  PRIMARY KEY (`order_id`, `product_id`)");
            extras.add("  INDEX idx_od_product (`product_id`)");
        } else if (modelClass == Salary.class) {
            columnDefs.add("  `staff_id` BINARY(16) NOT NULL PRIMARY KEY");
            columnDefs.add("  `base_salary` DECIMAL(10,2)");
            columnDefs.add("  `commission_rate` DECIMAL(10,2)");
        } else {
            String primaryKeyCol = null;
            for (Field field : fields) {
                String colName = toSnakeCase(field.getName());
                String colType = getSqlType(field);

                if (field.getAnnotation(org.bson.codecs.pojo.annotations.BsonId.class) != null) {
                    colType = colType.replace(" NOT NULL", "") + " NOT NULL PRIMARY KEY";
                    primaryKeyCol = colName;
                }

                columnDefs.add("  `" + colName + "` " + colType);
            }

            for (Field field : fields) {
                if (field.getAnnotation(org.bson.codecs.pojo.annotations.BsonId.class) != null) continue;
                if (field.getType() == java.util.UUID.class && field.getName().endsWith("Id")) {
                    String refCol = toSnakeCase(field.getName());
                    String idxName = "idx_" + tableName + "_" + refCol.replace("_id", "");
                    extras.add("  INDEX " + idxName + " (`" + refCol + "`)");
                }
            }
        }

        for (int i = 0; i < columnDefs.size(); i++) {
            sb.append(columnDefs.get(i));
            if (i < columnDefs.size() - 1 || !extras.isEmpty()) {
                sb.append(",");
            }
            sb.append("\n");
        }

        for (int i = 0; i < extras.size(); i++) {
            sb.append(extras.get(i));
            if (i < extras.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;\n");
        return sb.toString();
    }

    private static List<Field> getModelFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) continue;
            if (field.getName().startsWith("this$")) continue;
            fields.add(field);
        }
        return fields;
    }

    private static String getSqlType(Field field) {
        Class<?> type = field.getType();
        if (type.isEnum()) return "VARCHAR(10) DEFAULT 'TEXT'";
        if (type == java.util.UUID.class) return "BINARY(16) NOT NULL";
        if (type == String.class) return "VARCHAR(255)";
        if (type == Integer.class || type == int.class) return "INT";
        if (type == Double.class || type == double.class) return "DECIMAL(10,2)";
        if (type == Boolean.class || type == boolean.class) return "TINYINT(1) DEFAULT 1";
        if (type == LocalDate.class) return "DATE";
        if (type == Timestamp.class) return "DATETIME NOT NULL";
        return "VARCHAR(255)";
    }

    private static String toSnakeCase(String camelCase) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (sb.length() > 0) sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
