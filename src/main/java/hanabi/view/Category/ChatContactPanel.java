package hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import hanabi.view.Category.ChatPanel.ChatContactItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class ChatContactPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color HOVER_BG = new Color(248, 245, 242);
    private static final Color SELECTED_BG = new Color(232, 222, 213);
    private static final Color TEXT_SECONDARY = new Color(140, 124, 110);
    private static final Color SEARCH_BG = new Color(245, 245, 245);

    private final JPanel listContainer;
    private ChatContactItem selectedItem = null;
    private final Consumer<ChatContactItem> onContactSelected;

    public ChatContactPanel(List<ChatContactItem> initialContacts, Consumer<ChatContactItem> onContactSelected) {
        this.onContactSelected = onContactSelected;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 225, 220)));

        // Header (Tiêu đề + Tìm kiếm)
        JPanel headerPanel = new JPanel(new MigLayout("insets 20 20 10 20, fillx, wrap", "[fill]"));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Đoạn chat");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK_BROWN);
        
        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm liên hệ...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc:99; borderWidth:0; margin:5,10,5,10;");
        txtSearch.setBackground(SEARCH_BG);
        txtSearch.setPreferredSize(new Dimension(0, 36));

        headerPanel.add(title);
        headerPanel.add(txtSearch, "gapy 10");
        add(headerPanel, BorderLayout.NORTH);

        // Khung chứa danh sách
        listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "width:8; trackArc:999; thumbInsets:2,2,2,2;");
        
        add(scrollPane, BorderLayout.CENTER);
        refreshContacts(initialContacts);
    }

    public void refreshContacts(List<ChatContactItem> contacts) {
        listContainer.removeAll();
        for (ChatContactItem contact : contacts) {
            listContainer.add(createContactItem(contact));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JPanel createContactItem(ChatContactItem contact) {
        JPanel item = new JPanel(new MigLayout("insets 12 20 12 20, fillx", "[grow][]"));
        item.setBackground(Color.WHITE);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Bo góc khi hover/click
        item.putClientProperty(FlatClientProperties.STYLE, "arc:15;");

        // Sự kiện Click & Hover
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedItem = contact;
                for (java.awt.Component comp : listContainer.getComponents()) {
                    comp.setBackground(Color.WHITE);
                }
                item.setBackground(SELECTED_BG);
                if (onContactSelected != null) {
                    onContactSelected.accept(contact);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (contact != selectedItem) item.setBackground(HOVER_BG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (contact != selectedItem) item.setBackground(Color.WHITE);
            }
        });

        String displayName = contact.staff.getFullName() != null ? contact.staff.getFullName() : contact.staff.getStaffName();
        JLabel lblName = new JLabel(displayName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblName.setForeground(DARK_BROWN);

        JLabel lblLastMsg = new JLabel(contact.lastMessage);
        lblLastMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLastMsg.setForeground(TEXT_SECONDARY);
        
        JPanel textWrapper = new JPanel(new MigLayout("insets 0, wrap", "[fill]"));
        textWrapper.setOpaque(false);
        textWrapper.add(lblName);
        textWrapper.add(lblLastMsg, "width 10:150:150");

        JLabel lblTime = new JLabel(contact.lastTime);
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTime.setForeground(new Color(180, 170, 160));

        item.add(textWrapper, "growx");
        item.add(lblTime, "top");

        return item;
    }
}