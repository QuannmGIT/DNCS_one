package hanabi.view.Category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.Main;

public class CategoryPanel extends JPanel {

    public static final int PAGE_MENU_ITEMS = 0;
    public static final int PAGE_ACCOUNTS = 1;
    public static final int PAGE_REVENUE = 2;
    // public static final int PAGE_SALARY = 3;

    private static final Color SIDEBAR_BG = new Color(211, 181, 147);
    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);

    private JButton buttonMenuItems;
    private JButton buttonAccounts;
    private JButton buttonRevenue;
    // private JButton buttonSalary;
    private JButton buttonLogOut;

    private Icon menuIcon, menuIconLight;
    private Icon accountIcon, accountIconLight;
    private Icon revenueIcon, revenueIconLight;

    private Consumer<Integer> onNavigate;

    public CategoryPanel() {
        this(null);
    }

    public CategoryPanel(Consumer<Integer> onNavigate) {
        this.onNavigate = onNavigate;
        initComponents();
    }

    public void setActivePage(int page) {
        JButton[] buttons = { buttonMenuItems, buttonAccounts, buttonRevenue};
        for (int i = 0; i < buttons.length; i++) {
            applyButtonAppearance(buttons[i], i == page);
        }
    }

    private void initComponents() {
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(240, 600));
        setLayout(new GridBagLayout());

        menuIcon = new FlatSVGIcon("hanabi/assets/icon/MenuIcon.svg", 22, 22);
        menuIconLight = new FlatSVGIcon("hanabi/assets/icon/MenuIconLight.svg", 22, 22);
        accountIcon = new FlatSVGIcon("hanabi/assets/icon/AccountIcon.svg", 22, 22);
        accountIconLight = new FlatSVGIcon("hanabi/assets/icon/AccountIconLight.svg", 22, 22);
        revenueIcon = new FlatSVGIcon("hanabi/assets/icon/RevenueIcon.svg", 22, 22);
        revenueIconLight = new FlatSVGIcon("hanabi/assets/icon/RevenueIconLight.svg", 22, 22);

        JLabel title = new JLabel("CATEGORY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(DARK_BROWN);
        title.setBorder(new EmptyBorder(0, 0, 6, 0));

        JPanel titleWrapper = new JPanel(new BorderLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.add(title, BorderLayout.WEST);

        buttonMenuItems = createNavBtn("Menu Items");
        buttonAccounts = createNavBtn("Accounts");
        buttonRevenue = createNavBtn("Revenue");
        // buttonSalary = createNavBtn("Salary");

        buttonMenuItems.addActionListener(this::onMenuItems);
        buttonAccounts.addActionListener(this::onAccounts);
        buttonRevenue.addActionListener(this::onRevenue);
        // buttonSalary.addActionListener(this::onSalary);

        setActivePage(PAGE_MENU_ITEMS);

        buttonLogOut = new JButton("Log Out");
        buttonLogOut.addActionListener(e -> {
            Main.logout();
        });
        buttonLogOut.setHorizontalAlignment(SwingConstants.CENTER);
        buttonLogOut.setPreferredSize(new Dimension(0, 44));
        buttonLogOut.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buttonLogOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonLogOut.setBackground(Color.WHITE);
        buttonLogOut.setForeground(TEXT_MENU);
        buttonLogOut.putClientProperty(FlatClientProperties.STYLE,
                "arc:16; borderWidth:0; focusWidth:0; innerFocusWidth:0;" +
                        "pressedBackground:#D0B8A0");
        buttonLogOut.putClientProperty("JButton.hoverBackground", new Color(220, 200, 185));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(30, 20, 6, 20);
        add(titleWrapper, gbc);

        JPanel line = new JPanel();
        line.setBackground(DARK_BROWN);
        line.setPreferredSize(new Dimension(0, 2));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 14, 20);
        add(line, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridy = 2;
        add(buttonMenuItems, gbc);
        gbc.gridy = 3;
        add(buttonAccounts, gbc);
        gbc.gridy = 4;
        add(buttonRevenue, gbc);
        gbc.gridy = 5;
        // add(buttonSalary, gbc);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(10, 20, 30, 20);
        add(buttonLogOut, gbc);

        revalidate();
        repaint();
    }

    private JButton createNavBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(12);
        btn.setPreferredSize(new Dimension(0, 48));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:14; borderWidth:0; focusWidth:0; innerFocusWidth:0;" +
                        "pressedBackground:#A89480");
        return btn;
    }

    private void applyButtonAppearance(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(DARK_BROWN);
            btn.setForeground(Color.WHITE);
            btn.putClientProperty("JButton.hoverBackground", new Color(70, 50, 41));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(TEXT_MENU);
            btn.putClientProperty("JButton.hoverBackground", new Color(200, 180, 160));
        }

        if (btn == buttonMenuItems) {
            btn.setIcon(active ? menuIconLight : menuIcon);
        } else if (btn == buttonAccounts) {
            btn.setIcon(active ? accountIconLight : accountIcon);
        } else if (btn == buttonRevenue) {
            btn.setIcon(active ? revenueIconLight : revenueIcon);
        }
    }

    private void onMenuItems(ActionEvent evt) {
        setActivePage(PAGE_MENU_ITEMS);
        if (onNavigate != null)
            onNavigate.accept(PAGE_MENU_ITEMS);
    }

    private void onAccounts(ActionEvent evt) {
        setActivePage(PAGE_ACCOUNTS);
        if (onNavigate != null)
            onNavigate.accept(PAGE_ACCOUNTS);
    }

    private void onRevenue(ActionEvent evt) {
        setActivePage(PAGE_REVENUE);
        if (onNavigate != null)
            onNavigate.accept(PAGE_REVENUE);
    }

    // private void onSalary(ActionEvent evt) {
    //     setActivePage(PAGE_SALARY);
    //     if (onNavigate != null)
    //         onNavigate.accept(PAGE_SALARY);
    // }

    public static void main(String[] args) {
        // SwingUtilities.invokeLater(() -> {
        //     JFrame frame = new JFrame("Category Panel Demo");
        //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //     frame.setSize(300, 600);
        //     frame.setLocationRelativeTo(null);
        //     frame.setLayout(new BorderLayout());
        //     frame.add(new CategoryPanel(), BorderLayout.WEST);
        //     frame.setVisible(true);
        // });
    }
}
