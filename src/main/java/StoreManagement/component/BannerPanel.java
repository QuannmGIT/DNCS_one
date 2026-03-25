package StoreManagement.component;

import StoreManagement.Utility.FontLoader;

import javax.swing.*;
import java.awt.*;

public class BannerPanel extends JPanel {
    
    public BannerPanel() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new OverlayLayout(imagePanel));

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/ImageFile/CuaHangImage2.png"));
        Image image = originalIcon.getImage();
        Image newImage = image.getScaledInstance(600, 600, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(newImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);


        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false); 
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));


        JLabel textLabel = new JLabel("HANABI CAFE");
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(FontLoader.load("Fonts/AmaticSC-Regular.ttf", 70f));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel2 = new JLabel("FIND THE PERFECT COFFEE                        ");
        textLabel2.setForeground(Color.BLACK);   
        textLabel2.setFont(FontLoader.load("Fonts/DancingScript-Regular.ttf", 25f));
        textLabel2.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel textLabel3 = new JLabel("Những bậc thầy về cà phê của chúng tôi đã đúc kết nhiều năm" );
        textLabel3.setForeground(Color.BLACK);   
        textLabel3.setFont(FontLoader.load("Fonts/DancingScript-Regular.ttf", 20f));
        textLabel3.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel textLabel4 = new JLabel("kinh nghiệm, nếm thử cà phê của họ trong ba câu hỏi đơn giản");
        textLabel4.setForeground(Color.BLACK);   
        textLabel4.setFont(FontLoader.load("Fonts/DancingScript-Regular.ttf", 20f));
        textLabel4.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel textLabel5 = new JLabel("để giúp bạn tìm loại cà phê mà bạn chắc hẳn sẽ thích.           ");
        textLabel5.setForeground(Color.BLACK);   
        textLabel5.setFont(FontLoader.load("Fonts/DancingScript-Regular.ttf", 20f));
        textLabel5.setAlignmentX(Component.CENTER_ALIGNMENT);


        textPanel.add(Box.createVerticalGlue()); 
        
        textPanel.add(textLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 40))); 
        textPanel.add(textLabel2);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        textPanel.add(textLabel3);
        textPanel.add(textLabel4);
        textPanel.add(textLabel5);
        textPanel.add(Box.createRigidArea(new Dimension(0, 280)));
        textPanel.add(Box.createVerticalGlue());
 
        
        imagePanel.add(textPanel); 
        imagePanel.add(imageLabel);
        
        this.add(Box.createVerticalGlue());
        this.add(imagePanel);
    }
}