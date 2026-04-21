# Image Upload, Storage & Rendering Implementation

## 1. Database Schema Setup

```sql
CREATE TABLE images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    filename VARCHAR(255),
    image_data BLOB,
    file_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

## 2. Image Upload & Database Storage

**File**: `StoreManagement/utility/ImageUploader.java`

```java
package StoreManagement.utility;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;

public class ImageUploader {
    
    // Upload image to database and save locally
    public static boolean uploadImage(File imageFile, int productId, String storagePath) {
        dbConnect db = new dbConnect();
        
        try (Connection conn = db.getConnection();
             FileInputStream fis = new FileInputStream(imageFile)) {
            
            // Generate unique filename
            String uniqueFileName = System.currentTimeMillis() + "_" + imageFile.getName();
            String localPath = storagePath + File.separator + uniqueFileName;
            
            // 1. Save to local directory
            File destinationFile = new File(localPath);
            destinationFile.getParentFile().mkdirs(); // Create directories if not exist
            copyFile(imageFile, destinationFile);
            
            // 2. Save to database
            String sql = "INSERT INTO images (product_id, filename, image_data, file_path) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ps.setString(2, uniqueFileName);
                ps.setBinaryStream(3, fis, (int) imageFile.length());
                ps.setString(4, localPath);
                
                return ps.executeUpdate() > 0;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}
```

## 3. Image Retrieval & Rendering

**File**: `StoreManagement/utility/ImageRenderer.java`

```java
package StoreManagement.utility;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageRenderer {
    
    // Method 1: Load from database (BLOB)
    public static ImageIcon loadFromDatabase(int productId, int width, int height) {
        dbConnect db = new dbConnect();
        
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT image_data FROM images WHERE product_id = ? ORDER BY created_at DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    byte[] imageData = rs.getBytes("image_data");
                    return createImageIcon(imageData, width, height);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Method 2: Load from local path
    public static ImageIcon loadFromPath(String filePath, int width, int height) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                BufferedImage originalImage = ImageIO.read(file);
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Helper method to create scaled ImageIcon from byte array
    private static ImageIcon createImageIcon(byte[] imageData, int width, int height) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage originalImage = ImageIO.read(bais);
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Method 3: Hybrid approach - try local first, fallback to database
    public static ImageIcon loadImage(int productId, String localPath, int width, int height) {
        ImageIcon icon = loadFromPath(localPath, width, height);
        if (icon == null) {
            icon = loadFromDatabase(productId, width, height);
        }
        return icon;
    }
}
```

## 4. UI Integration Example

**File**: `StoreManagement/component/ImageUploadPanel.java`

```java
package StoreManagement.component;

import StoreManagement.utility.ImageUploader;
import StoreManagement.utility.ImageRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ImageUploadPanel extends JPanel {
    private JLabel imagePreview;
    private int productId;
    private static final String STORAGE_PATH = "src/main/resources/assets/img";
    private static final int PREVIEW_WIDTH = 250;
    private static final int PREVIEW_HEIGHT = 250;

    public ImageUploadPanel(int productId) {
        this.productId = productId;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image preview area
        imagePreview = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreview.setPreferredSize(new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT));
        imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(imagePreview, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        
        JButton btnUpload = new JButton("Upload Image");
        btnUpload.addActionListener(e -> handleUpload());
        
        JButton btnLoad = new JButton("Load from DB");
        btnLoad.addActionListener(e -> handleLoadFromDB());
        
        buttonPanel.add(btnUpload);
        buttonPanel.add(btnLoad);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"
        ));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Upload to database and save locally
            boolean success = ImageUploader.uploadImage(selectedFile, productId, STORAGE_PATH);
            
            if (success) {
                // Show preview
                ImageIcon icon = ImageRenderer.loadFromPath(
                    STORAGE_PATH + File.separator + selectedFile.getName(), 
                    PREVIEW_WIDTH, PREVIEW_HEIGHT
                );
                imagePreview.setIcon(icon);
                imagePreview.setText(null);
                JOptionPane.showMessageDialog(this, "Upload successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Upload failed!");
            }
        }
    }

    private void handleLoadFromDB() {
        ImageIcon icon = ImageRenderer.loadFromDatabase(productId, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        if (icon != null) {
            imagePreview.setIcon(icon);
            imagePreview.setText(null);
        } else {
            JOptionPane.showMessageDialog(this, "No image found in database");
        }
    }
}
```

## 5. Integration with Product Components

Update `ProductDisplayComponent.java` to use the image renderer:

```java
// In ProductDisplayComponent.java, modify loadImage method:
private void loadImage(JLabel imageLabel) {
    // Try local path first, fallback to database
    ImageIcon icon = ImageRenderer.loadImage(
        product.getId(), 
        product.getImagePath(), 
        250, 250
    );
    
    if (icon != null) {
        imageLabel.setIcon(icon);
    } else {
        imageLabel.setText("[Image not available]");
        imageLabel.setForeground(Color.GRAY);
    }
}
```

## Key Points

- **Storage Strategy**: Save to local filesystem for fast access, store in database for backup/portability
- **File Naming**: Use timestamps to avoid filename conflicts
- **Scaling**: Always scale images to component size for memory efficiency
- **Error Handling**: Provide fallbacks when images aren't found
- **File Types**: Restrict to image formats (jpg, png, gif) via file chooser filters
