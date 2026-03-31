import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

import StoreManagement.component.MainFrame;

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
        Image pic = new ImageIcon("assets/img/HanabiIcon.png").getImage();
//        Product product = new Product("name", "description", "category", pic, new ArrayList<>());

    }
}