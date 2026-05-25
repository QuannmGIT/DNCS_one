package hanabi.view.Field;

import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.model.Product;
import hanabi.service.MenuService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class AddProductForm extends JPanel {

    private JTextField txtName, txtPrice, txtCost;
    private JComboBox<String> cbCategory;
    private JCheckBox chkStatus;
    private JLabel lblSelectedFile;
    private JButton btnBrowse;
    private File selectedImageFile;
    private final JButton cmdCreate;
    private static JFrame f;
    private final MenuService menuService = new MenuService();
    private final Runnable onSuccess;

    public AddProductForm(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 380", "[fill]"));

        add(new JLabel(new FlatSVGIcon("hanabi/assets/icon/MenuIcon.svg")), "center");
        add(new JSeparator(), "gapy 15 15");

        JLabel lbName = new JLabel("Product Name");
        lbName.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbName, "gapy 10 n");
        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. Coffee Latte");
        add(txtName);

        JLabel lbCategory = new JLabel("Category");
        lbCategory.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbCategory, "gapy 10 n");
        cbCategory = new JComboBox<>(new String[]{"Hot", "Iced", "Bakery"});
        cbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(cbCategory);

        JLabel lbPrice = new JLabel("Price");
        lbPrice.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbPrice, "split 2, gapy 10 n");
        add(optionalTag(), "right, gapx 200");

        txtPrice = new JTextField();
        txtPrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. 45000");
        add(txtPrice);

        JLabel lbCost = new JLabel("Cost");
        lbCost.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbCost, "split 2, gapy 10 n");
        add(optionalTag(), "right, gapx 200");

        txtCost = new JTextField();
        txtCost.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. 20000");
        add(txtCost);

        JLabel lbImage = new JLabel("Image");
        lbImage.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbImage, "gapy 10 n");

        JPanel fileRow = new JPanel(new MigLayout("insets 0, fillx", "[fill][]"));
        fileRow.setOpaque(false);
        lblSelectedFile = new JLabel("No file selected");
        lblSelectedFile.putClientProperty(FlatClientProperties.STYLE,
                "font:italic; foreground:#929493;");
        btnBrowse = new JButton("Browse...");
        btnBrowse.putClientProperty(FlatClientProperties.STYLE,
                "arc:8; borderWidth:0; focusWidth:0; margin:4,12,4,12;");
        btnBrowse.addActionListener(e -> browseImage());
        fileRow.add(lblSelectedFile);
        fileRow.add(btnBrowse, "gapx 8");
        add(fileRow);

        chkStatus = new JCheckBox("Active");
        chkStatus.setSelected(true);
        add(chkStatus, "gapy 5 n");

        cmdCreate = new JButton("Add Product") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdCreate.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        add(cmdCreate);

        cmdCreate.addActionListener(e -> submitForm());
        setVisible(true);
    }

    public static void init(Runnable onSuccess) {
        f = new JFrame();
        f.setTitle("Add New Product");
        f.add(new AddProductForm(onSuccess));
        f.setSize(380, 550);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private Component optionalTag() {
        JLabel otpLabel = new JLabel("Optional");
        otpLabel.putClientProperty(FlatClientProperties.STYLE,
                "font:italic; foreground:#929493;");
        return otpLabel;
    }

    private void browseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select product image");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image files (SVG, PNG, JPG)", "svg", "png", "jpg", "jpeg"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = chooser.getSelectedFile();
            lblSelectedFile.setText(selectedImageFile.getName());
            lblSelectedFile.putClientProperty(FlatClientProperties.STYLE,
                    "font:bold; foreground:#000000;");
        }
    }

    private void submitForm() {
        String name = txtName.getText().trim();
        String category = (String) cbCategory.getSelectedItem();
        String priceStr = txtPrice.getText().trim();
        String costStr = txtCost.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Product name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedImageFile == null || !selectedImageFile.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Please select an image file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String fileName = selectedImageFile.getName();
            int dot = fileName.lastIndexOf('.');
            String imageName = (dot > 0) ? fileName.substring(0, dot) : fileName;
            if (imageName.length() > 200) {
                imageName = imageName.substring(0, 200);
            }

            String destDir = System.getProperty("user.dir")
                    + "/src/main/resources/hanabi/assets/img/";
            File destFile = new File(destDir, fileName);
            destFile.getParentFile().mkdirs();
            Files.copy(selectedImageFile.toPath(), destFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            String targetDir = System.getProperty("user.dir")
                    + "/target/classes/hanabi/assets/img/";
            File targetClassDir = new File(System.getProperty("user.dir") + "/target/classes");
            if (targetClassDir.exists()) {
                File targetFile = new File(targetDir, fileName);
                targetFile.getParentFile().mkdirs();
                Files.copy(selectedImageFile.toPath(), targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            Product p = new Product();
            p.setProductId(java.util.UUID.randomUUID());
            p.setProductName(name);
            p.setCategory(category);
            p.setImage(imageName);

            if (!priceStr.isEmpty()) {
                try {
                    p.setPrice(Double.parseDouble(priceStr));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Price must be a valid number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (!costStr.isEmpty()) {
                try {
                    p.setCost(Double.parseDouble(costStr));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Cost must be a valid number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            p.setStatus(chkStatus.isSelected());

            menuService.addProduct(p);
            JOptionPane.showMessageDialog(this,
                    "Product \"" + name + "\" added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            f.dispose();
            if (onSuccess != null) {
                onSuccess.run();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Price and Cost must be valid numbers!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to copy image file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException e) {
            System.err.println(e.getMessage());
        }
    }
}
