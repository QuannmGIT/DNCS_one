import StoreManagement.component.MainFrame;
import StoreManagement.model.Product;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
        });
        Image pic = new ImageIcon("ImageFile/HanabiIcon.png").getImage();
        Product product = new Product("name", "description", "category", pic, new ArrayList<>());

    }
}