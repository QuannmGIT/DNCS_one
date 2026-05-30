package hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

public class ChatContactPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color HOVER_BG = new Color(245, 242, 240);
    private static final Color SELECTED_BG = new Color(232, 222, 213);
    private static final Color TEXT_SECONDARY = new Color(140, 124, 110);
    private static final Color TIME_COLOR = new Color(176, 160, 144);
    private static final Color BADGE_BG = new Color(231, 76, 60);
    private static final Color SEARCH_BG = new Color(240, 237, 232);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_NAME = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_MSG = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TIME = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_SEARCH = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 10);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 20);

    private final JPanel contactListPanel;
    private final Consumer<ChatConversationPanel.ContactInfo> onContactSelected;
    private JPanel selectedItem;

    public ChatContactPanel(List<ChatConversationPanel.ContactInfo> contacts,
                            Consumer<ChatConversationPanel.ContactInfo> onContactSelected) {
        this.onContactSelected = onContactSelected;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(320, 0));

        JPanel topSection = new JPanel(new MigLayout("wrap, fillx, insets 0", "[fill]"));
        topSection.setBackground(Color.WHITE);
        topSection.add(createHeader(), "growx");
        topSection.add(createSearchField(), "growx, gapy 0 8");

        contactListPanel = new JPanel(new MigLayout("wrap, fillx, insets 0", "[fill]"));
        contactListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(contactListPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:8; trackArc:999; thumbInsets:0,0,0,0; trackInsets:0,0,0,0;");

        add(topSection, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        populateContacts(contacts);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("insets 18 20 6 20, fillx", "[fill, grow][]"));
        header.setBackground(Color.WHITE);

        JLabel title = new JLabel("Chat");
        title.setFont(FONT_TITLE);
        title.setForeground(DARK_BROWN);
        header.add(title);

        JPanel btnPanel = new JPanel(new MigLayout("insets 0, gapx 2", "[][]"));
        btnPanel.setOpaque(false);
        btnPanel.add(createHeaderBtn("+"));
        btnPanel.add(createHeaderBtn("\u22EE"));
        header.add(btnPanel);

        return header;
    }

    private JTextField createSearchField() {
        JTextField txt = new JTextField();
        txt.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm...");
        txt.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON,
                new FlatSVGIcon("hanabi/assets/icon/user.svg", 16, 16));
        txt.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; margin:4,15,4,15; borderWidth:1; focusColor:#D3B593;"
                + "background:" + colorToHex(SEARCH_BG) + ";");
        txt.setFont(FONT_SEARCH);
        return txt;
    }

    private JButton createHeaderBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setForeground(DARK_BROWN);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12; borderWidth:0; focusWidth:0; background:null; hoverBackground:#F0EBE5");
        return btn;
    }

    private void populateContacts(List<ChatConversationPanel.ContactInfo> contacts) {
        contactListPanel.removeAll();
        for (ChatConversationPanel.ContactInfo contact : contacts) {
            contactListPanel.add(createContactItem(contact), "growx, gapy 1");
        }
        contactListPanel.revalidate();
        contactListPanel.repaint();
    }

    private JPanel createContactItem(ChatConversationPanel.ContactInfo contact) {
        JPanel item = new JPanel(new MigLayout("insets 10 20 10 20, fillx", "[fill, grow][]")) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = Math.min(d.width,
                        getParent() != null ? getParent().getWidth() : 320);
                return d;
            }
        };
        item.setBackground(Color.WHITE);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (selectedItem != null) {
                    selectedItem.setBackground(Color.WHITE);
                }
                item.setBackground(SELECTED_BG);
                selectedItem = item;
                if (onContactSelected != null) {
                    onContactSelected.accept(contact);
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(HOVER_BG);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (item != selectedItem) {
                    item.setBackground(Color.WHITE);
                }
            }
        });

        JLabel lblName = new JLabel(contact.name);
        lblName.setFont(FONT_NAME);
        lblName.setForeground(DARK_BROWN);

        String lastMsg = contact.lastMessage != null ? contact.lastMessage : "";
        JLabel lblLastMsg = new JLabel(lastMsg);
        lblLastMsg.setFont(FONT_MSG);
        lblLastMsg.setForeground(TEXT_SECONDARY);
        lblLastMsg.putClientProperty(FlatClientProperties.STYLE, "truncateText:true");

        JPanel textWrapper = new JPanel(new MigLayout("insets 0, wrap", "[fill]"));
        textWrapper.setOpaque(false);
        textWrapper.add(lblName);
        textWrapper.add(lblLastMsg);

        JLabel lblTime = new JLabel(contact.lastTime != null ? contact.lastTime : "");
        lblTime.setFont(FONT_TIME);
        lblTime.setForeground(TIME_COLOR);

        JPanel rightPanel = new JPanel(new MigLayout("insets 0, wrap, al right", "[right]"));
        rightPanel.setOpaque(false);
        rightPanel.add(lblTime);
        if (contact.unreadCount > 0) {
            JLabel badge = new JLabel(String.valueOf(contact.unreadCount), SwingConstants.CENTER);
            badge.setFont(FONT_BADGE);
            badge.setForeground(Color.WHITE);
            badge.setOpaque(true);
            badge.setBackground(BADGE_BG);
            badge.putClientProperty(FlatClientProperties.STYLE, "arc:999");
            badge.setPreferredSize(new Dimension(20, 20));
            rightPanel.add(badge, "gaptop 4");
        }

        item.add(textWrapper, "growx");
        item.add(rightPanel, "top");

        return item;
    }

    private static String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
