package StoreManagement.component;

import javax.swing.*;
import java.awt.*;

/**
 * PopUp class is used to create a custom pop-up window.
 *
 * @author MinhCreatorVN
 */
public class PopUp extends JDialog {
    private JFrame parent;
    private JComponent main;
    private Component Comp;
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
        this.title = title;
        this.width = width;
        this.height = height;
        initComponent();
    }

    public PopUp(Component Main, String title, int width, int height) {
        this.Comp = Main;
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

    // experimental function
    public static void main(String[] arg) {
        PopUp popUp = new PopUp(new JPanel(), "", 400, 300);
        popUp.initFrame();
    }
}