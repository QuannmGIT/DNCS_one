package hanabi.view.Category;

import hanabi.view.Category.ChatConversationPanel.ContactInfo;
import hanabi.view.Category.ChatConversationPanel.MessageItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class ChatPanel extends JPanel {

    private static final Color DIVIDER_COLOR = new Color(224, 216, 208);

    private final List<ContactInfo> contacts;

    public ChatPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        contacts = createDummyContacts();
        init();
    }

    private void init() {
        ChatConversationPanel conversationPanel = new ChatConversationPanel();

        ChatContactPanel contactPanel = new ChatContactPanel(contacts, contact -> {
            conversationPanel.showConversation(contact);
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactPanel, conversationPanel);
        splitPane.setDividerLocation(320);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerSize(1);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);
        splitPane.setBackground(DIVIDER_COLOR);
        splitPane.putClientProperty("FlatLaf.style", "dividerColor: " + colorToHex(DIVIDER_COLOR));

        add(splitPane, BorderLayout.CENTER);
    }

    private String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private List<ContactInfo> createDummyContacts() {
        return Arrays.asList(
            new ContactInfo("1", "Nguy\u1EC5n V\u0103n A",
                "Ch\u00E0o b\u1EA1n, m\u00ECnh c\u1EA7n h\u1ED7 tr\u1EE3 v\u1EC1 v\u1EA5n \u0111\u1EC1 k\u1EF9 thu\u1EADT",
                "10:30", 2,
                Arrays.asList(
                    new MessageItem("Ch\u00E0o b\u1EA1n, m\u00ECnh c\u1EA7n h\u1ED7 tr\u1EE3 v\u1EC1 v\u1EA5n \u0111\u1EC1 k\u1EF9 thu\u1EADT", false, "homnay 10:28"),
                    new MessageItem("Ch\u00E0o anh, anh g\u1EB7p v\u1EA5n \u0111\u1EC1 g\u00EC \u1EA1?", true, "homnay 10:29"),
                    new MessageItem("M\u00E1y in b\u1ECB l\u1ED7i kh\u00F4ng nh\u1EADn gi\u1EA5y, anh c\u00F3 th\u1EC3 qua ki\u1EC3m tra gi\u00FAp kh\u00F4ng?", false, "homnay 10:30"),
                    new MessageItem("V\u00E2ng \u1EA1, \u0111\u1EC3 em xu\u1ED1ng ph\u00F2ng anh ki\u1EC3m tra ngay.", true, "homnay 10:32"),
                    new MessageItem("C\u1EA3m \u01A1n em nhi\u1EC1u.", false, "homnay 10:33")
                )
            ),

            new ContactInfo("2", "Tr\u1EA7n Th\u1ECB B",
                "Anh \u01A1i, em mu\u1ED1n xin ngh\u1EC9 ph\u00E9p th\u1EE9 6 tu\u1EA7n n\u00E0y \u1EA1.",
                "09:15", 1,
                Arrays.asList(
                    new MessageItem("Anh \u01A1i, em mu\u1ED1n xin ngh\u1EC9 ph\u00E9p th\u1EE9 6 tu\u1EA7n n\u00E0y \u1EA1.", false, "homnay 09:15"),
                    new MessageItem("\u0110\u01B0\u1EE3c em, anh ghi l\u1EA1i r\u1ED3i. Em nh\u1EDB l\u00E0m \u0111\u01A1n xin ngh\u1EC9 nh\u00E9.", true, "homnay 09:17"),
                    new MessageItem("V\u00E2ng \u1EA1, c\u1EA3m \u01A1n anh!", false, "homnay 09:18")
                )
            ),

            new ContactInfo("3", "L\u00EA V\u0103n C",
                "B\u00E1o c\u00E1o doanh thu th\u00E1ng n\u00E0y \u0111\u00E3 xong.",
                "H\u00F4m qua", 0,
                Arrays.asList(
                    new MessageItem("Anh \u01A1i, b\u00E1o c\u00E1o doanh thu th\u00E1ng n\u00E0y em \u0111\u00E3 l\u00E0m xong r\u1ED3i \u1EA1.", false, "homqua 16:45"),
                    new MessageItem("T\u1ED1t, em g\u1EEDi file qua \u0111\u00E2y cho anh xem v\u1EDBi.", true, "homqua 16:46"),
                    new MessageItem("D\u1EA1, em g\u1EEDi k\u00E8m \u1EDF tr\u00EAn \u1EA1.", false, "homqua 16:47")
                )
            ),

            new ContactInfo("4", "Ph\u1EA1m Th\u1ECB D",
                "Em \u0111\u00E3 s\u1EAFp x\u1EBFp l\u1EA1i qu\u1EA7y pha ch\u1EBF xong r\u1ED3i.",
                "15:42", 0,
                Arrays.asList(
                    new MessageItem("Anh cho em xin \u00FD ki\u1EBFn v\u1EC1 c\u00E1ch s\u1EAFp x\u1EBFp l\u1EA1i qu\u1EA7y pha ch\u1EBF \u1EA1.", false, "12/05/2026 15:30"),
                    new MessageItem("\u0110\u1EC3 anh xu\u1ED1ng xem r\u1ED3i g\u00F3p \u00FD cho em.", true, "12/05/2026 15:35"),
                    new MessageItem("Em c\u1EA3m \u01A1n anh!", false, "12/05/2026 15:42")
                )
            ),

            new ContactInfo("5", "Ho\u00E0ng V\u0103n E",
                "H\u00F4m nay qu\u00E1n \u0111\u00F4ng kh\u00E1ch qu\u00E1 anh \u01A1i!",
                "12:05", 3,
                Arrays.asList(
                    new MessageItem("Anh \u01A1i, h\u00F4m nay qu\u00E1n \u0111\u00F4ng kh\u00E1ch qu\u00E1, em c\u1EA7n th\u00EAm ng\u01B0\u1EDDi h\u1ED7 tr\u1EE3.", false, "homnay 11:50"),
                    new MessageItem("\u0110\u01B0\u1EE3c, anh s\u1EBD c\u1EED th\u00EAm ng\u01B0\u1EDDi xu\u1ED1ng gi\u00FAp em.", true, "homnay 11:52"),
                    new MessageItem("C\u1EA3m \u01A1n anh! C\u00F3 th\u00EAm ng\u01B0\u1EDDi \u0111\u1EE1 r\u1ED3i \u1EA1.", false, "homnay 12:05")
                )
            )
        );
    }
}