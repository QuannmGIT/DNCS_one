package hanabi.components;

import javax.swing.*;

/**
 * PopUp class is used to create a custom pop-up window.
 *
 * @author MinhCreatorVN
 */
public class PopUp extends JDialog {
    private JFrame parent;
    private JComponent main;
    private String title;
    private int width;
    private int height;

    public PopUp(JFrame parent, String title, int width, int height) {
        this.parent = parent;
        this.title = title;
        this.width = width;
        this.height = height;
        initFrame();
    }

    public PopUp(JComponent Main, String title, int width, int height) {
        this.main = Main;
        this.parent = (Main != null) ? (JFrame) SwingUtilities.getWindowAncestor(Main) : null;
        this.title = title;
        this.width = width;
        this.height = height;
        initComponent();
    }

    public void initFrame() {
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void initComponent() {
        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

}