package hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import hanabi.dao.ChatMessageDAO;
import hanabi.model.ChatMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;

public class ChatConversationPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);
    private static final Color BUBBLE_RECEIVED = new Color(242, 242, 242);

    private final JLabel lblContactName;
    private final JPanel chatArea;
    private final JScrollPane scrollPane;
    private final JTextField txtInput;
    private final ChatMessageDAO messageDAO;
    private final Timer pollTimer;

    private UUID currentUserId;
    private UUID currentContactId;
    private Timestamp lastPollTime;
    private final Consumer<ChatMessage> onSendMessage;

    public ChatConversationPanel(Consumer<ChatMessage> onSendMessage) {
        this.onSendMessage = onSendMessage;
        this.messageDAO = new ChatMessageDAO();

        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // Top Header
        JPanel header = new JPanel(new MigLayout("insets 15 30 15 30, fillx", "[grow][]"));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 225, 220)));

        lblContactName = new JLabel("Chọn một cuộc trò chuyện");
        lblContactName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblContactName.setForeground(DARK_BROWN);

        JLabel lblStatus = new JLabel("● Đang hoạt động");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(46, 204, 113));

        JPanel titleWrapper = new JPanel(new MigLayout("insets 0, wrap"));
        titleWrapper.setOpaque(false);
        titleWrapper.add(lblContactName);
        titleWrapper.add(lblStatus);

        header.add(titleWrapper);
        add(header, BorderLayout.NORTH);

        // Chat Area
        chatArea = new JPanel(new MigLayout("insets 20 30 20 30, wrap, fillx"));
        chatArea.setBackground(LIGHT_BG);

        scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:8; trackArc:999; thumbInsets:2,2,2,2;");
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Input
        JPanel inputPanel = new JPanel(new MigLayout("insets 15 30 20 30, fillx", "[grow][50!]"));
        inputPanel.setBackground(LIGHT_BG);

        txtInput = new JTextField();
        txtInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tin nhắn...");
        txtInput.putClientProperty(FlatClientProperties.STYLE,
                "arc:99; borderWidth:0; margin:10,15,10,15;");
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSend = new JButton("Gửi");
        btnSend.setBackground(DARK_BROWN);
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSend.putClientProperty(FlatClientProperties.STYLE,
                "arc:99; borderWidth:0; focusWidth:0;");
        btnSend.setPreferredSize(new Dimension(50, 40));

        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
        btnSend.addActionListener(e -> sendMessage());

        inputPanel.add(txtInput, "growx, height 45!");
        inputPanel.add(btnSend, "height 45!");
        add(inputPanel, BorderLayout.SOUTH);

        // Poll every 2.5s for new messages in the active conversation
        pollTimer = new Timer(2500, e -> pollNewMessages());
        pollTimer.start();
    }

    public void loadConversation(UUID contactId, String contactName,
            List<ChatMessage> messages, UUID currentUserId) {
        this.currentContactId = contactId;
        this.currentUserId = currentUserId;
        lastPollTime = messages.isEmpty()
                ? new Timestamp(System.currentTimeMillis())
                : messages.get(messages.size() - 1).getCreatedAt();

        lblContactName.setText(contactName);
        chatArea.removeAll();

        for (ChatMessage msg : messages) {
            boolean isSent = msg.getSenderId().equals(currentUserId);
            appendMessage(msg.getContent(), msg.getCreatedAt(), isSent);
        }

        chatArea.revalidate();
        chatArea.repaint();
        scrollToBottom();
    }

    private void sendMessage() {
        String text = txtInput.getText().trim();
        if (text.isEmpty() || currentContactId == null || currentUserId == null)
            return;

        ChatMessage msg = new ChatMessage();
        msg.setMessageId(UUID.randomUUID());
        msg.setSenderId(currentUserId);
        msg.setReceiverId(currentContactId);
        msg.setContent(text);
        msg.setMessageType(ChatMessage.MessageType.TEXT);
        msg.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        onSendMessage.accept(msg);

        appendMessage(text, msg.getCreatedAt(), true);
        lastPollTime = msg.getCreatedAt();
        txtInput.setText("");
        chatArea.revalidate();
        chatArea.repaint();
        scrollToBottom();
    }

    private void pollNewMessages() {
        if (currentUserId == null || currentContactId == null || lastPollTime == null)
            return;

        List<ChatMessage> newMsgs = messageDAO.findNewMessages(
                currentUserId, currentContactId, lastPollTime);

        if (!newMsgs.isEmpty()) {
            for (ChatMessage msg : newMsgs) {
                boolean isSent = msg.getSenderId().equals(currentUserId);
                appendMessage(msg.getContent(), msg.getCreatedAt(), isSent);
            }
            lastPollTime = newMsgs.get(newMsgs.size() - 1).getCreatedAt();
            chatArea.revalidate();
            chatArea.repaint();
            scrollToBottom();
        }
    }

    private void appendMessage(String content, Timestamp time, boolean isSent) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel lblTime = new JLabel(sdf.format(time));
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTime.setForeground(Color.GRAY);

        JTextArea txtMsg = new JTextArea(content);
        txtMsg.setEditable(false);
        txtMsg.setLineWrap(true);
        txtMsg.setWrapStyleWord(true);
        txtMsg.setOpaque(false);
        txtMsg.setBorder(null);
        txtMsg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMsg.setForeground(isSent ? Color.WHITE : Color.BLACK);

        JPanel bubble = new JPanel(new MigLayout("insets 10 15 10 15, wrap"));
        bubble.setBackground(isSent ? DARK_BROWN : BUBBLE_RECEIVED);
        bubble.putClientProperty(FlatClientProperties.STYLE, "arc:35;");
        bubble.add(txtMsg, "growx");

        String align = isSent ? "al right" : "al left";
        JPanel wrapper = new JPanel(new MigLayout("insets 5 10 5 10, " + align + ", wrap"));
        wrapper.setOpaque(false);
        wrapper.add(bubble, "wmax 1000px, gapy 0 0");
        wrapper.add(lblTime, "gapy 2 0");

        chatArea.add(wrapper, "growx, wrap, gapy 0 0");
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(
                    scrollPane.getVerticalScrollBar().getMaximum());
        });
    }
}
