package hanabi.view.Login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hanabi.util.FontLoader;

public class Banner extends JPanel {

    private Image backgroundImage;
    private JLabel lblMainTitle, lblSlogan, txtLabel1, txtLabel2, txtLabel3;

    public Banner() {
        init();
        initComponents();
    }

    private void init() {
        setPreferredSize(new Dimension(550, 600));
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        JPanel textPanel = new JPanel();
        lblMainTitle = new JLabel("HANABI CAFE");
        lblSlogan = new JLabel("FIND THE PERFECT COFFEE");
        Color textColor = new Color(54, 38, 28);
        txtLabel1 = new JLabel("Những bậc thầy về cà phê của chúng tôi đã đúc kết nhiều năm");
        txtLabel2 = new JLabel("kinh nghiệm, nếm thử cà phê của họ trong ba câu hỏi đơn giản");
        txtLabel3 = new JLabel("để giúp bạn tìm loại cà phê mà bạn chắc hẳn sẽ thích.");

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/hanabi/assets/img/CuaHangImage2.png")).getImage();
        } catch (Exception e) {
            System.err.println("Not found Banner background image: " + e.getMessage());
        }

        lblMainTitle.setForeground(new Color(74, 53, 41));
        try {
            lblMainTitle.setFont(FontLoader.load("/hanabi/assets/Fonts/AmaticSC-Regular.ttf", 72f));
        } catch (RuntimeException e) {
            lblMainTitle.setFont(new Font("Segoe UI", Font.BOLD, 60));
        }
        lblMainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSlogan.setForeground(new Color(92, 64, 51));
        try {
            lblSlogan.setFont(FontLoader.load("/hanabi/assets/Fonts/DancingScript-Regular.ttf", 26f));
        } catch (RuntimeException e) {
            lblSlogan.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        }
        lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font vietnameseFont;
        try {
            vietnameseFont = FontLoader.load("/hanabi/assets/Fonts/DancingScript-Regular.ttf", 21f);
        } catch (RuntimeException e) {
            vietnameseFont = new Font("Segoe UI", Font.PLAIN, 18);
        }

        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        txtLabel1.setFont(vietnameseFont);
        txtLabel1.setForeground(textColor);
        txtLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtLabel2.setFont(vietnameseFont);
        txtLabel2.setForeground(textColor);
        txtLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtLabel3.setFont(vietnameseFont);
        txtLabel3.setForeground(textColor);
        txtLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(Box.createVerticalStrut(45));
        textPanel.add(lblMainTitle);
        textPanel.add(Box.createVerticalStrut(50));
        textPanel.add(lblSlogan);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(txtLabel1);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(txtLabel2);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(txtLabel3);
        textPanel.add(Box.createVerticalGlue());

        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.setColor(new Color(222, 196, 162));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
