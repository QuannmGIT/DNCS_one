import com.formdev.flatlaf.FlatLightLaf;
import com.hanabi.view.Login.Banner;
import com.hanabi.view.Login.LoginPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class App {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to set FlatLaf: " + e.getMessage());
        }

        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("HANABI CAFE - Đăng nhập");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Banner banner = new Banner();
            LoginPanel loginPanel = new LoginPanel(frame);

            frame.getContentPane().setLayout(new BorderLayout());
            frame.add(banner, BorderLayout.WEST);
            frame.add(loginPanel, BorderLayout.CENTER);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
