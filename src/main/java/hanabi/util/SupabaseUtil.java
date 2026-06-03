package hanabi.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupabaseUtil {

    private static final Logger LOG = Logger.getLogger(SupabaseUtil.class.getName());
    private static HikariDataSource dataSource;
    private static volatile boolean initialized = false;
    private static volatile RuntimeException initError;
    private static String resolvedUrl;

    public static synchronized void initialize() {
        if (initialized) return;
        LOG.info("Initializing Supabase (PostgreSQL) connection...");

        try {
            String jdbcUrl = resolveHostname(global.SUPABASE_URL);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(global.SUPABASE_USER);
            config.setPassword(global.SUPABASE_PASSWORD);
            config.setDriverClassName(org.postgresql.Driver.class.getName());
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(15000);
            config.setIdleTimeout(300000);
            config.setMaxLifetime(600000);
            config.addDataSourceProperty("ApplicationName", "HanabiCafe");
            dataSource = new HikariDataSource(config);
            initialized = true;
            LOG.info("Supabase connection pool established");
            try {
                DatabaseInitializer.initialize();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Database schema/seed initialization failed: " + e.getMessage(), e);
            }
        } catch (RuntimeException e) {
            initError = e;
            initialized = false;
            LOG.log(Level.SEVERE, "Failed to initialize Supabase connection: " + e.getMessage(), e);
            throw e;
        }
    }

    private static String resolveHostname(String jdbcUrl) {
        String hostname = global.SUPABASE_HOST;
        try {
            InetAddress[] addrs = InetAddress.getAllByName(hostname);
            LOG.info("DNS via OS resolver for '" + hostname + "':");
            for (InetAddress a : addrs) {
                LOG.info("  -> " + a.getHostAddress());
            }
            return jdbcUrl;
        } catch (Exception e) {
            LOG.warning("OS DNS resolution failed for '" + hostname + "': " + e.getMessage());
            LOG.info("Trying DNS via Google DoH (8.8.8.8)...");
            try {
                String ip = resolveViaGoogleDoh(hostname);
                if (ip != null) {
                    LOG.info("Resolved '" + hostname + "' via Google DoH -> " + ip);
                    String wrappedIp = ip.contains(":") ? "[" + ip + "]" : ip;
                    resolvedUrl = jdbcUrl.replace(hostname, wrappedIp);
                    LOG.info("Using resolved URL: " + resolvedUrl.replaceAll(":.*@", ":****@"));
                    return resolvedUrl;
                }
            } catch (Exception e2) {
                LOG.warning("Google DoH resolution also failed: " + e2.getMessage());
            }
            throw new RuntimeException(
                "Cannot resolve '" + hostname + "' — the hostname only has an IPv6 address and " +
                "your machine's DNS cannot resolve it.\n" +
                "Fix: Change your network DNS servers to 8.8.8.8 / 8.8.4.4 (Google Public DNS),\n" +
                "or enable IPv6 on your machine/network, or use a different database provider.\n" +
                "Error: " + e.getMessage(), e);
        }
    }

    private static String resolveViaGoogleDoh(String hostname) throws Exception {
        URL url = new URL("https://dns.google/resolve?name=" + hostname + "&type=AAAA");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("Accept", "application/json");
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) return null;

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        String json = sb.toString();
        Pattern p = Pattern.compile("\"data\":\"([^\"]+)\"");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            if (initError != null) {
                throw new SQLException("Supabase not connected: " + initError.getMessage(), initError);
            }
            initialize();
        }
        return dataSource.getConnection();
    }

    public static boolean isConnected() {
        return initialized && dataSource != null && !dataSource.isClosed();
    }

    public static RuntimeException getInitError() {
        return initError;
    }

    public static <T> T querySingle(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapper.mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + sql, e);
        }
        return null;
    }

    public static <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + sql, e);
        }
        return results;
    }

    public static int update(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update failed: " + sql, e);
        }
    }

    public static void execute(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Execute failed: " + sql, e);
        }
    }

    private static void setParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof java.util.UUID) {
                ps.setObject(i + 1, param, java.sql.Types.OTHER);
            } else if (param instanceof java.time.LocalDate) {
                ps.setObject(i + 1, param);
            } else if (param instanceof java.sql.Timestamp) {
                ps.setTimestamp(i + 1, (java.sql.Timestamp) param);
            } else if (param instanceof Enum) {
                ps.setString(i + 1, ((Enum<?>) param).name());
            } else {
                ps.setObject(i + 1, param);
            }
        }
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOG.info("Supabase connection pool closed");
        }
        initialized = false;
        initError = null;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T mapRow(ResultSet rs) throws SQLException;
    }
}
