package hanabi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LaragonManager {

    private static final String[] LARAGON_PATHS = {
        System.getenv("ProgramFiles") + "\\Laragon",
        System.getenv("ProgramFiles(x86)") + "\\Laragon",
        System.getenv("LOCALAPPDATA") + "\\Laragon",
        "C:\\Laragon"
    };

    private static final String MYSQL_PORT = "3306";
    private static final int MAX_RETRIES = 30;
    private static final int RETRY_DELAY_MS = 2000;

    private static final String INSTALLER_URL =
        "https://github.com/leokhoa/laragon/releases/latest/download/laragon-wamp.exe";

    private static String laragonPath = null;

    public static boolean isMySQLRunning() {
        String url = "jdbc:mysql://localhost:" + MYSQL_PORT + "/";
        try (Connection conn = DriverManager.getConnection(url, global.USER, global.PASSWORD)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static String findLaragonPath() {
        if (laragonPath != null) return laragonPath;
        for (String path : LARAGON_PATHS) {
            if (path == null) continue;
            File mysqlBin = new File(path, "usr\\bin\\mysql.exe");
            if (mysqlBin.isFile()) {
                laragonPath = path;
                return laragonPath;
            }
        }
        return null;
    }

    public static boolean startLaragonApp() {
        String larPath = findLaragonPath();
        if (larPath == null) return false;
        File laragonExe = new File(larPath, "laragon.exe");
        if (!laragonExe.isFile()) return false;
        try {
            ProcessBuilder pb = new ProcessBuilder(laragonExe.getAbsolutePath(), "start");
            pb.directory(new File(larPath));
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
            return p.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static boolean startMySQL() {
        String larPath = findLaragonPath();
        if (larPath == null) {
            return false;
        }
        File laragonExe = new File(larPath, "laragon.exe");
        if (laragonExe.isFile()) {
            try {
                ProcessBuilder pb = new ProcessBuilder(laragonExe.getAbsolutePath(), "start", "mysql");
                pb.directory(new File(larPath));
                pb.inheritIO();
                Process p = pb.start();
                p.waitFor();
                return p.exitValue() == 0;
            } catch (IOException | InterruptedException e) {
                return false;
            }
        }
        File mysqld = new File(larPath, "usr\\bin\\mysqld.exe");
        if (mysqld.isFile()) {
            try {
                ProcessBuilder pb = new ProcessBuilder(mysqld.getAbsolutePath());
                pb.directory(new File(larPath, "usr\\bin"));
                pb.inheritIO();
                pb.start();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean downloadAndInstallLaragon() {
        try {
            String tempDir = System.getenv("TEMP");
            if (tempDir == null) tempDir = System.getProperty("java.io.tmpdir");
            String installerPath = Paths.get(tempDir, "laragon.exe").toString();

            System.out.println("[LaragonManager] Laragon not found. Downloading installer...");
            URL url = URI.create(INSTALLER_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.connect();

            long totalSize = conn.getContentLengthLong();
            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                 FileOutputStream out = new FileOutputStream(installerPath)) {

                byte[] buf = new byte[8192];
                int read;
                long downloaded = 0;
                int lastPercent = -1;
                while ((read = in.read(buf)) != -1) {
                    out.write(buf, 0, read);
                    downloaded += read;
                    if (totalSize > 0) {
                        int percent = (int) (downloaded * 100 / totalSize);
                        if (percent != lastPercent) {
                            System.out.print("\r[LaragonManager] Downloading... " + percent + "%");
                            lastPercent = percent;
                        }
                    }
                }
            }
            System.out.println("\n[LaragonManager] Download complete. Installing silently...");

            ProcessBuilder pb = new ProcessBuilder(installerPath, "/silent");
            pb.inheritIO();
            Process p = pb.start();
            int exit = p.waitFor();
            if (exit != 0) {
                System.out.println("[LaragonManager] Installer exited with code " + exit);
                return false;
            }

            laragonPath = null;
            String found = findLaragonPath();
            if (found == null) {
                System.out.println("[LaragonManager] Installation completed but Laragon not found at expected paths.");
                return false;
            }
            System.out.println("[LaragonManager] Laragon installed at " + found);
            return true;
        } catch (Exception e) {
            System.out.println("[LaragonManager] Failed to download/install Laragon: " + e.getMessage());
            return false;
        }
    }

    public static boolean ensureMySQLRunning() {
        if (isMySQLRunning()) {
            return true;
        }
        if (findLaragonPath() == null) {
            if (!downloadAndInstallLaragon()) {
                return false;
            }
            System.out.println("[LaragonManager] Starting Laragon app after fresh install...");
            startLaragonApp();
        }
        String larPath = findLaragonPath();
        if (larPath == null) {
            return false;
        }
        System.out.println("[LaragonManager] Starting Laragon app at " + larPath + "...");
        startLaragonApp();
        System.out.println("[LaragonManager] Starting MySQL via Laragon...");
        if (!startMySQL()) {
            return false;
        }
        System.out.println("[LaragonManager] Waiting for MySQL to become available...");
        for (int i = 0; i < MAX_RETRIES; i++) {
            if (isMySQLRunning()) {
                System.out.println("[LaragonManager] MySQL is now running.");
                return true;
            }
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
