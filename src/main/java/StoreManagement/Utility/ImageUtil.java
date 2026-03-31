package StoreManagement.Utility;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

public class ImageUtil {
    // For PNG/JPG from database - use regular ImageIcon with FlatLaf styling
    public static ImageIcon bytesToImageIcon(byte[] imageData, int width, int height) {
        if (imageData == null) return null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage img = ImageIO.read(bais);
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }

    // For SVG from database - use FlatSVGIcon
    public static FlatSVGIcon bytesToSVGIcon(byte[] svgData, int width, int height) {
        if (svgData == null) return null;
        try {
            FlatSVGIcon icon = new FlatSVGIcon(new ByteArrayInputStream(svgData));
            icon.derive(width, height);
            return icon;
        } catch (Exception e) {
            return null;
        }
    }

    // For SVG files from resources
    public static FlatSVGIcon loadSVGResource(String resourcePath, int width, int height) {
        FlatSVGIcon icon = new FlatSVGIcon(resourcePath, width, height);
        return icon;
    }

    // Upload SVG to database
    public static void saveSVGToDB(Connection conn, int productId, File svgFile) throws SQLException, IOException {
        byte[] svgData = java.nio.file.Files.readAllBytes(svgFile.toPath());
        String sql = "UPDATE products SET svg_data = ? WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBytes(1, svgData);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // Load SVG from database
    public static FlatSVGIcon loadSVGFromDB(Connection conn, int productId, int width, int height) throws SQLException {
        String sql = "SELECT svg_data FROM products WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] svgData = rs.getBytes("svg_data");
                return bytesToSVGIcon(svgData, width, height);
            }
        }
        return null;
    }
}