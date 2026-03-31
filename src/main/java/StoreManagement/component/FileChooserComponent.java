package StoreManagement.component;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooserComponent extends JPanel {
    private static String imagePath; // Instance variable to store the path

    private static void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        // Optional: Filter to only show image files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(null); // 'null' for center screen, or 'this' if in a JFrame

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            System.out.println(imagePath);
            // You can display the path in a JTextField for user confirmation
            // Example: pathTextField.setText(imagePath);

            // Optional: display the image in a JLabel
            // ImageIcon icon = new ImageIcon(imagePath);
            // yourJLabel.setIcon(icon);
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    static void main(String[] args) {
        uploadButtonActionPerformed(null);
    }
}