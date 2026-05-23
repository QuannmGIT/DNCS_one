package hanabi.view.Category;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import hanabi.util.FontLoader;

public class MenuItemsPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color TEXT_MENU = new Color(74, 53, 44);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color CARD_BG = new Color(253, 251, 249);
    private static final Color ROW_EVEN = new Color(252, 250, 248);
    private static final Color ROW_ODD = Color.WHITE;
    private static final Color LINE_COLOR = new Color(230, 220, 210);

    private Font amaticFont;

    private final String[][] itemData = {
        {"Matcha Ice\nBlended", "35.000đ", "1", "Iced"},
        {"Americano", "28.000đ", "2", "Hot"},
        {"croissants", "25.000đ", "3", "Bakery"},
        {"Ice Latte", "25.000đ", "4", "Iced"},
        {"Ice Black\nCoffee", "35.000đ", "5", "Iced"},
        {"Caramel\nMachito", "40.000đ", "6", "Hot"},
        {"Tiramisu", "45.000đ", "7", "Bakery"},
        {"Lemon\nTea", "19.000đ", "8", "Iced"},
        {"Orange", "23.000đ", "9", "Iced"},
        {"Espresso", "20.000đ", "10", "Hot"},
        {"Hot\nChocolate", "35.000đ", "11", "Hot"},
        {"Cappuccino", "32.000đ", "12", "Hot"},
        {"Blueberry\nMuffin", "25.000đ", "24", "Bakery"},
        {"Iced\nMocha", "38.000đ", "15", "Iced"},
        {"Hot Matcha\nLatte", "35.000đ", "13", "Hot"},
        {"Red Velvet", "45.000đ", "22", "Bakery"},
        {"Peach\nTea", "25.000đ", "16", "Iced"},
        {"Earl Grey\nTea", "25.000đ", "14", "Hot"},
        {"Cheesecake", "40.000đ", "20", "Bakery"},
        {"Cold Brew", "35.000đ", "17", "Iced"},
        {"Macaron\n(Set 3)", "30.000đ", "23", "Bakery"},
        {"Mango\nSmoothie", "40.000đ", "18", "Iced"},
        {"Choco\nCookie", "15.000đ", "21", "Bakery"},
        {"Strawberry\nMacchiato", "42.000đ", "19", "Iced"}
    };

    private static class CatInfo {
        final String name;
        final String activeIcon;
        final String inactiveIcon;
        CatInfo(String name, String activeIcon, String inactiveIcon) {
            this.name = name;
            this.activeIcon = activeIcon;
            this.inactiveIcon = inactiveIcon;
        }
    }

    private final CatInfo[] categories = {
        new CatInfo("All", "AlliconLight.svg", "Allicon.svg"),
        new CatInfo("Hot", "MenuIconLight.svg", "MenuIcon.svg"),
        new CatInfo("Iced", "IceIconLight.svg", "IceIcon.svg"),
        new CatInfo("Bakery", "BakeryIconLight.svg", "BakeryIcon.svg")
    };

    private final Map<String, int[]> cartMap = new LinkedHashMap<>();
    private String activeCat = "All";
    private List<JButton> catButtons = new ArrayList<>();

    private JPanel gridContainer;
    private JPanel cartBody;
    private JLabel totalLabel;

    public MenuItemsPanel() {
        loadFont();
        initComponents();
    }

    private void loadFont() {
        try {
            amaticFont = FontLoader.load(
                "/hanabi/assets/Fonts/AmaticSC-Regular.ttf", 48f);
        } catch (Exception e) {
            amaticFont = new Font("Segoe UI", Font.BOLD, 42);
        }
    }

    private void initComponents() {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout(20, 0));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(25, 0));
        body.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setOpaque(false);
        left.add(createCategoryBar(), BorderLayout.NORTH);

        gridContainer = new JPanel(new GridLayout(0, 3, 15, 15));
        gridContainer.setBackground(BG_COLOR);
        gridContainer.setBorder(new EmptyBorder(15, 0, 0, 0));

        JScrollPane gridScroll = new JScrollPane(gridContainer);
        gridScroll.setBorder(null);
        gridScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gridScroll.getVerticalScrollBar().setUnitIncrement(16);
        gridScroll.setOpaque(false);
        gridScroll.getViewport().setOpaque(false);

        left.add(gridScroll, BorderLayout.CENTER);
        body.add(left, BorderLayout.CENTER);
        body.add(createCartPanel(), BorderLayout.EAST);

        add(body, BorderLayout.CENTER);

        rebuildGrid();
    }

    // ─── HEADER ───────────────────────────────────────────────

    private JPanel createHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Place Order");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(DARK_BROWN);

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        brand.setOpaque(false);
        JLabel name = new JLabel("HANABI CAFE");
        name.setFont(amaticFont);
        name.setForeground(DARK_BROWN);
        JLabel icon = new JLabel();
        try {
            ImageIcon img = new ImageIcon(MenuItemsPanel.class.getResource(
                "/hanabi/assets/icon/HanabiCafe.png"));
            icon.setIcon(new ImageIcon(img.getImage().getScaledInstance(48, 38, Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {}
        brand.add(name);
        brand.add(icon);

        p.add(title, BorderLayout.WEST);
        p.add(brand, BorderLayout.EAST);
        return p;
    }

    // ─── CATEGORY BAR ─────────────────────────────────────────

    private JPanel createCategoryBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(0, 0, 10, 0));

        for (CatInfo c : categories) {
            boolean active = c.name.equals(activeCat);
            JButton btn = makeCatBtn(c, active);
            catButtons.add(btn);
            bar.add(btn);
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(bar, BorderLayout.NORTH);
        JPanel line = new JPanel();
        line.setBackground(DARK_BROWN);
        line.setPreferredSize(new Dimension(0, 2));
        wrapper.add(line, BorderLayout.SOUTH);
        return wrapper;
    }

    private JButton makeCatBtn(CatInfo info, boolean active) {
        String path = "hanabi/assets/icon/";
        String iconName = active ? info.activeIcon : info.inactiveIcon;
        JButton btn = new JButton(info.name);

        try {
            btn.setIcon(new FlatSVGIcon(path + iconName, 20, 20));
        } catch (Exception ignored) {}

        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setIconTextGap(8);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
            "arc:40; borderWidth:0; focusWidth:0; innerFocusWidth:0; margin:6,16,6,16;");

        updateCatBtnStyle(btn, active);
        btn.addActionListener(e -> onCatClick(info.name));
        return btn;
    }

    private void updateCatBtnStyle(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(DARK_BROWN);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(DARK_BROWN);
        }
    }

    private void switchCatIcon(JButton btn, CatInfo info, boolean active) {
        String path = "/hanabi/assets/icon/";
        String iconName = active ? info.activeIcon : info.inactiveIcon;
        try {
            btn.setIcon(new FlatSVGIcon(path + iconName, 20, 20));
        } catch (Exception ignored) {}
    }

    private void onCatClick(String cat) {
        if (cat.equals(activeCat)) return;
        activeCat = cat;

        for (int i = 0; i < categories.length; i++) {
            boolean active = categories[i].name.equals(cat);
            updateCatBtnStyle(catButtons.get(i), active);
            switchCatIcon(catButtons.get(i), categories[i], active);
        }
        rebuildGrid();
    }

    // ─── MENU GRID ────────────────────────────────────────────

    private void rebuildGrid() {
        gridContainer.removeAll();
        int idx = 0;
        for (String[] row : itemData) {
            String cat = row[3];
            if (!activeCat.equals("All") && !cat.equals(activeCat)) {
                idx++;
                continue;
            }
            gridContainer.add(createItemCard(row[0], row[1], row[2]));
            idx++;
        }
        if (gridContainer.getComponentCount() == 0) {
            JLabel empty = new JLabel("Không có món nào trong danh mục này", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            empty.setForeground(new Color(180, 170, 160));
            gridContainer.add(empty);
        }
        gridContainer.revalidate();
        gridContainer.repaint();
    }

    private JPanel createItemCard(String name, String price, String imgIdx) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(0, 145));
        card.setMinimumSize(new Dimension(160, 145));
        card.putClientProperty(FlatClientProperties.STYLE,
            "arc:20; border: 1,1,1,1, #5A463D");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(65, 65));
        try {
            img.setIcon(new FlatSVGIcon(
                "hanabi/assets/img/" + imgIdx + ".svg", 65, 65));
        } catch (Exception e) {
            img.setOpaque(true);
            img.setBackground(new Color(240, 235, 230));
        }
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        card.add(img, gbc);

        JLabel nameLbl = new JLabel("<html>" + name.replace("\n", "<br>") + "</html>");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLbl.setForeground(TEXT_MENU);
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(nameLbl, gbc);

        JLabel priceLbl = new JLabel(price);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLbl.setForeground(DARK_BROWN);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        card.add(priceLbl, gbc);

        JButton btnAdd = new JButton("+");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnAdd.setBackground(DARK_BROWN);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.putClientProperty(FlatClientProperties.STYLE,
            "arc:12; borderWidth:0; focusWidth:0; margin:0,0,4,0;");
        btnAdd.setPreferredSize(new Dimension(35, 35));

        String finalName = name;
        String finalPrice = price;
        btnAdd.addActionListener(e -> addToCart(finalName, finalPrice));

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        card.add(btnAdd, gbc);

        return card;
    }

    // ─── CART PANEL ───────────────────────────────────────────

    private JPanel createCartPanel() {
        JPanel cart = new JPanel(new BorderLayout(0, 10));
        cart.setBackground(Color.WHITE);
        cart.setPreferredSize(new Dimension(360, 0));
        cart.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; border: 2,2,2,2, #5A463D;");
            // padding: 18,14,14,14
        cart.setBorder(new EmptyBorder(18, 14, 14, 14));
        JLabel title = new JLabel("Đơn hàng hiện tại", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(DARK_BROWN);
        title.setBorder(new EmptyBorder(6, 0, 12, 0));
        cart.add(title, BorderLayout.NORTH);

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, DARK_BROWN),
            new EmptyBorder(0, 4, 8, 4)));

        Font hf = new Font("Segoe UI", Font.BOLD, 13);
        JLabel hItem = new JLabel("Món");
        JLabel hQty = new JLabel("SL", SwingConstants.CENTER);
        JLabel hPrice = new JLabel("Thành tiền", SwingConstants.RIGHT);
        for (JLabel l : new JLabel[]{hItem, hQty, hPrice}) {
            l.setFont(hf);
            l.setForeground(DARK_BROWN);
        }
        hItem.setPreferredSize(new Dimension(100, 22));
        hQty.setPreferredSize(new Dimension(70, 22));
        hPrice.setPreferredSize(new Dimension(90, 22));
        header.add(hItem, BorderLayout.WEST);
        header.add(hQty, BorderLayout.CENTER);
        header.add(hPrice, BorderLayout.EAST);
        listWrapper.add(header, BorderLayout.NORTH);

        cartBody = new JPanel();
        cartBody.setLayout(new BoxLayout(cartBody, BoxLayout.Y_AXIS));
        cartBody.setOpaque(false);
        JScrollPane scroll = new JScrollPane(cartBody);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        listWrapper.add(scroll, BorderLayout.CENTER);

        cart.add(listWrapper, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12, 4, 4, 4));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, DARK_BROWN),
            new EmptyBorder(10, 0, 4, 0)));

        JLabel totalPrefix = new JLabel("Tổng cộng:");
        totalPrefix.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalPrefix.setForeground(DARK_BROWN);
        totalLabel = new JLabel("0đ", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(DARK_BROWN);
        totalRow.add(totalPrefix, BorderLayout.WEST);
        totalRow.add(totalLabel, BorderLayout.EAST);
        bottom.add(totalRow, BorderLayout.NORTH);

        JButton payBtn = new JButton("Proceed to Payment");
        payBtn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        payBtn.setBackground(DARK_BROWN);
        payBtn.setForeground(Color.WHITE);
        payBtn.setPreferredSize(new Dimension(0, 52));
        payBtn.putClientProperty(FlatClientProperties.STYLE,
            "arc:18; borderWidth:0; focusWidth:0;");
        payBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bottom.add(payBtn, BorderLayout.SOUTH);

        cart.add(bottom, BorderLayout.SOUTH);

        return cart;
    }

    // ─── CART LOGIC ───────────────────────────────────────────

    private int parsePrice(String s) {
        return Integer.parseInt(s.replace("đ", "").replace(".", "").trim());
    }

    private String fmtPrice(int v) {
        return String.format("%,d", v).replace(",", ".") + "đ";
    }

    private void addToCart(String name, String priceStr) {
        int unitPrice = parsePrice(priceStr);
        int[] entry = cartMap.get(name);
        if (entry == null) {
            cartMap.put(name, new int[]{1, unitPrice});
        } else {
            entry[0]++;
        }
        rebuildCart();
    }

    private void changeQty(String name, int delta) {
        int[] entry = cartMap.get(name);
        if (entry == null) return;
        entry[0] += delta;
        if (entry[0] <= 0) {
            cartMap.remove(name);
        }
        rebuildCart();
    }

    private void rebuildCart() {
        cartBody.removeAll();
        int total = 0;
        int idx = 0;
        for (Map.Entry<String, int[]> e : cartMap.entrySet()) {
            String name = e.getKey();
            int qty = e.getValue()[0];
            int unitPrice = e.getValue()[1];
            int lineTotal = qty * unitPrice;
            total += lineTotal;
            cartBody.add(makeCartRow(name, qty, unitPrice, lineTotal, idx % 2 == 0));
            idx++;
        }
        if (cartMap.isEmpty()) {
            JLabel empty = new JLabel("Chưa có món nào", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(new Color(180, 170, 160));
            empty.setBorder(new EmptyBorder(20, 0, 20, 0));
            cartBody.add(empty);
        }
        totalLabel.setText(fmtPrice(total));
        cartBody.revalidate();
        cartBody.repaint();
    }

    private JPanel makeCartRow(String name, int qty, int unitPrice,
                                int lineTotal, boolean even) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(even ? ROW_EVEN : ROW_ODD);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, LINE_COLOR),
            new EmptyBorder(10, 4, 10, 4)));

        JLabel nameLbl = new JLabel("<html>" + name.replace("\n", "<br>") + "</html>");
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(TEXT_MENU);
        nameLbl.setPreferredSize(new Dimension(90, 36));

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        qtyPanel.setOpaque(false);

        JLabel minus = new JLabel("\u2212");
        minus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        minus.setForeground(DARK_BROWN);
        minus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        roundLabel(minus, 18);
        String fn = name;
        minus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { changeQty(fn, -1); }
        });

        JLabel count = new JLabel(String.valueOf(qty));
        count.setFont(new Font("Segoe UI", Font.BOLD, 15));
        count.setForeground(DARK_BROWN);
        count.setHorizontalAlignment(SwingConstants.CENTER);
        count.setPreferredSize(new Dimension(24, 24));

        JLabel plus = new JLabel("+");
        plus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        plus.setForeground(DARK_BROWN);
        plus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        roundLabel(plus, 18);
        plus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { changeQty(fn, 1); }
        });

        qtyPanel.add(minus);
        qtyPanel.add(count);
        qtyPanel.add(plus);

        JLabel priceLbl = new JLabel(fmtPrice(lineTotal), SwingConstants.RIGHT);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLbl.setForeground(DARK_BROWN);
        priceLbl.setPreferredSize(new Dimension(90, 22));

        row.add(nameLbl, BorderLayout.WEST);
        row.add(qtyPanel, BorderLayout.CENTER);
        row.add(priceLbl, BorderLayout.EAST);
        return row;
    }

    private void roundLabel(JLabel lbl, int size) {
        lbl.setOpaque(true);
        lbl.setBackground(new Color(235, 225, 215));
        lbl.setPreferredSize(new Dimension(size, size));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.putClientProperty(FlatClientProperties.STYLE, "arc:" + size);
    }

    // ─── MAIN ─────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Place Order View - HANABI CAFE");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(new MenuItemsPanel());
            frame.setSize(1100, 750);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
