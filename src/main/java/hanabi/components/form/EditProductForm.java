package hanabi.components.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.components.Menu;
import hanabi.components.ProductTable;
import hanabi.model.Product;
import hanabi.service.MenuService;
import net.miginfocom.swing.MigLayout;

public class EditProductForm extends Menu {

    private JPanel tablePanel;
    private JScrollPane scrollPane;
    private ProductTable productTable = new ProductTable();
    private JButton btnRefresh;
    private JButton btnEdit;
    private JButton btnDelete;
    private final MenuService menuService = new MenuService();
    private JTextField txtName, txtPrice, txtCost;
    private JComboBox<String> cbCategory;
    private JCheckBox chkStatus;
    private JLabel lblSelectedFile;
    private JButton btnBrowse;
    private File selectedImageFile;
    private JButton cmdEdit;
    private JFrame fr;

    public EditProductForm() {
        init();
        initComponent();
    }

    @Override
    public void add(ArrayList<Component> components) {
        // TODO Auto-generated method stub
        super.add(components);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        super.init();
        setLayout(new BorderLayout());
        setSize(1200, 700);
        // setVisible(true);
        setLocationRelativeTo(null);
        productTable.refresh();
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowSelectionAllowed(true);
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = productTable.getSelectedRow();
                    UUID productId = productTable.getProductIdAtRow(row);
                    if (productId != null) {
                        openEditDialog(productId);
                    }
                }
            }
        });

    }

    @Override
    public void initComponent() {
        // TODO Auto-generated method stub
        super.initComponent();
        tablePanel = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(productTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        bottomPanel.add(RefreshBut());
        bottomPanel.add(EditBut());
        bottomPanel.add(DeleteBut());
        add(bottomPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private Component RefreshBut() {
        btnRefresh = new JButton("Refresh");
        btnRefresh.putClientProperty(FlatClientProperties.STYLE,
                "font: bold; arc: 15;");
        btnRefresh.addActionListener(e -> {
            productTable.refresh();
        });
        return btnRefresh;
    }

    private Component EditBut() {
        btnEdit = new JButton("Edit Product");
        btnEdit.putClientProperty(FlatClientProperties.STYLE,
                "background: #0398fc; foreground: #ffff; font: bold; arc: 15;");
        btnEdit.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                UUID productId = productTable.getProductIdAtRow(row);
                openEditDialog(productId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product first");
            }
        });
        return btnEdit;
    }

    private Component DeleteBut() {
        btnDelete = new JButton("Delete Product");
        btnDelete.putClientProperty(FlatClientProperties.STYLE,
                "background: #fc0317; foreground: #ffff; font: bold; arc: 15;");
        btnDelete.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a product first");
                return;
            }
            UUID productId = productTable.getProductIdAtRow(row);
            if (productId == null)
                return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this product?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                menuService.deleteProduct(productId);
                productTable.refresh();
            }
        });
        return btnDelete;
    }

    private void openEditDialog(UUID productId) {
        Product product = menuService.getProductById(productId);
        fr = new JFrame();
        fr.setSize(800, 950);
        fr.setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 380", "[fill]"));
        fr.setLocationRelativeTo(null);
        // fr.add(new JLabel(new FlatSVGIcon("hanabi/assets/icon/MenuIcon.svg")),
        // "center");
        // fr.add(new JSeparator(), "gapy 15 15");

        JLabel lbName = new JLabel("Product Name");
        lbName.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        fr.add(lbName, "gapy 10 n");
        txtName = new JTextField(product.getProductName());
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. Coffee Latte");
        fr.add(txtName);

        JLabel lbCategory = new JLabel("Category");
        lbCategory.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        fr.add(lbCategory, "gapy 10 n");
        cbCategory = new JComboBox<>(new String[] { "Hot", "Iced", "Bakery" });
        cbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbCategory.setSelectedItem(product.getCategory());
        fr.add(cbCategory);

        JLabel lbPrice = new JLabel("Price");
        lbPrice.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        fr.add(lbPrice, "split 2, gapy 10 n");

        txtPrice = new JTextField(String.valueOf(product.getPrice()));
        txtPrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. 45000");
        fr.add(txtPrice);

        JLabel lbCost = new JLabel("Cost");
        lbCost.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        fr.add(lbCost, "split 2, gapy 10 n");

        txtCost = new JTextField(String.valueOf(product.getCost()));
        txtCost.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, " e.g. 20000");
        fr.add(txtCost);

        JLabel lbImage = new JLabel("Image");
        lbImage.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        fr.add(lbImage, "gapy 10 n");

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
        fr.add(fileRow);

        chkStatus = new JCheckBox("Active");
        chkStatus.setSelected(true);
        fr.add(chkStatus, "gapy 5 n");

        cmdEdit = new JButton("Apply new information for Product") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdEdit.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        fr.add(cmdEdit);

        cmdEdit.addActionListener(e -> submitForm(product));
        fr.setVisible(true);
        productTable.refresh();
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

    private void submitForm(Product product) {
        String name = txtName.getText().trim();
        String category = (String) cbCategory.getSelectedItem();
        String priceStr = txtPrice.getText().trim();
        String costStr = txtCost.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Product name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        product.setProductName(name);
        product.setCategory(category);
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

            if (!priceStr.isEmpty()) {
                try {
                    product.setPrice(Double.valueOf(txtPrice.getText()));

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Price must be a valid number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (!costStr.isEmpty()) {
                try {
                    product.setCost(Double.valueOf(txtCost.getText()));

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Cost must be a valid number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            product.setImage(imageName);
            product.setStatus(chkStatus.isSelected() ? true : false);

            menuService.updateProduct(product);

            JOptionPane.showMessageDialog(this,
                    "Product \"" + name + "\" added successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            fr.dispose();

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

    public static void main(String[] args) {

    }
}