package hanabi.view.Category;

import com.formdev.flatlaf.FlatClientProperties;
import hanabi.dao.ChatMessageDAO;
import hanabi.model.ChatMessage;
import hanabi.util.FileUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;

public class ChatConversationPanel extends JPanel {

    private static final Color DARK_BROWN = new Color(90, 70, 61);
    private static final Color LIGHT_BG = new Color(250, 248, 245);
    private static final Color BUBBLE_SENT = new Color(211, 181, 147);
    private static final Color BUBBLE_RECEIVED = Color.WHITE;
    private static final Color HEADER_BORDER = new Color(224, 216, 208);
    private static final Color TIME_COLOR = new Color(180, 170, 160);
    private static final Color SEP_BG = new Color(235, 230, 225);

    private static final Font FONT_NAME = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_STATUS = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BUBBLE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TIME = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font FONT_SEP = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_EMOJI = new Font("Segoe UI Emoji", Font.PLAIN, 18);
    private static final Font FONT_SEND = new Font("Segoe UI", Font.BOLD, 14);
    private static final int MAX_IMAGE_WIDTH = 250;

    private final JPanel chatArea;
    private final JScrollPane scrollPane;
    private final JPanel headerPanel;
    private final JPanel centerWrapper;
    private JTextArea txtInput;
    private JButton btnSend;
    private UUID currentContactId;
    private String currentContactName;
    private UUID myId;
    private boolean isAdmin;
    private BiConsumer<UUID, String> onSendMessage;
    private BiConsumer<UUID, File> onSendImage;
    private final ChatMessageDAO messageDAO = new ChatMessageDAO();

    public ChatConversationPanel() {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        headerPanel = createHeader();
        centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(LIGHT_BG);

        chatArea = new JPanel(new MigLayout("wrap, fillx, insets 20 30 10 30", "[fill]"));
        chatArea.setBackground(LIGHT_BG);

        scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "width:8; trackArc:999; thumbInsets:0,0,0,0; trackInsets:0,0,0,0;");

        centerWrapper.add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = createEmptyState();
        centerWrapper.add(emptyPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
    }

    public void init(UUID myId, boolean isAdmin,
            BiConsumer<UUID, String> onSendMessage,
            BiConsumer<UUID, File> onSendImage) {
        this.myId = myId;
        this.isAdmin = isAdmin;
        this.onSendMessage = onSendMessage;
        this.onSendImage = onSendImage;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("insets 12 24 12 24, fillx"));
        header.setBackground(Color.WHITE);
        header.putClientProperty(FlatClientProperties.STYLE,
                "border:0,0,1,0," + colorToHex(HEADER_BORDER));

        JLabel lblName = new JLabel("Chọn một cuộc trò chuyện");
        lblName.setFont(FONT_NAME);
        lblName.setForeground(DARK_BROWN);
        header.add(lblName, "wrap");

        JLabel lblStatus = new JLabel("");
        lblStatus.setFont(FONT_STATUS);
        header.add(lblStatus);

        return header;
    }

    private JPanel createInputPanel() {
        JPanel input = new JPanel(new MigLayout("insets 12 20 12 20, fillx", "[][][fill, grow][]"));
        input.setBackground(Color.WHITE);
        input.putClientProperty(FlatClientProperties.STYLE,
                "border:1,0,0,0," + colorToHex(HEADER_BORDER));

        JButton btnAttach = createIconBtn("\uD83D\uDCCE");
        JButton btnImage = createIconBtn("\uD83D\uDDBC\uFE0F");
        JButton btnEmoji = createIconBtn("\uD83D\uDE0A");

        btnImage.addActionListener(e -> chooseAndSendImage());

        txtInput = new JTextArea(1, 20);
        txtInput.setFont(FONT_BUBBLE);
        txtInput.setLineWrap(true);
        txtInput.setWrapStyleWord(true);
        txtInput.setRows(1);
        txtInput.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tin nhắn...");
        txtInput.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; margin:7,15,7,15; borderWidth:1; focusColor:#D3B593");
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendCurrentMessage();
                }
            }
        });

        btnSend = new JButton("Gửi");
        btnSend.setFont(FONT_SEND);
        btnSend.setForeground(Color.WHITE);
        btnSend.setBackground(DARK_BROWN);
        btnSend.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSend.putClientProperty(FlatClientProperties.STYLE,
                "arc:20; margin:7,18,7,18; borderWidth:0; focusWidth:0; hoverBackground:#4A352C");
        btnSend.addActionListener(e -> sendCurrentMessage());

        input.add(btnAttach, "height 36!, width 36!");
        input.add(btnImage, "height 36!, width 36!");
        input.add(txtInput, "growx, height 36!");
        input.add(btnEmoji, "height 36!, width 36!");
        input.add(btnSend, "height 36!");

        return input;
    }

    private void chooseAndSendImage() {
        if (currentContactId == null) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn ảnh để gửi");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Hình ảnh (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
                "jpg", "jpeg", "png", "gif", "bmp");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.exists() && file.isFile()) {
                if (onSendImage != null) {
                    onSendImage.accept(currentContactId, file);
                }
                appendImage(file.getAbsolutePath(), true);
            }
        }
    }

    private void sendCurrentMessage() {
        if (txtInput == null || currentContactId == null) return;
        String text = txtInput.getText().trim();
        if (text.isEmpty()) return;
        txtInput.setText("");

        if (onSendMessage != null) {
            onSendMessage.accept(currentContactId, text);
        }

        appendMessage(text, true);
    }

    private JPanel createEmptyState() {
        JPanel p = new JPanel(new MigLayout("insets 0, wrap, fill", "[center]"));
        p.setBackground(LIGHT_BG);

        JLabel icon = new JLabel("\uD83D\uDCAC", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));

        JLabel text = new JLabel("Chọn một cuộc trò chuyện để bắt đầu", SwingConstants.CENTER);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        text.setForeground(TIME_COLOR);

        p.add(icon, "wrap, gaptop 80");
        p.add(text);

        return p;
    }

    public void showConversation(UUID contactId, String contactName) {
        this.currentContactId = contactId;
        this.currentContactName = contactName;

        centerWrapper.removeAll();
        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        centerWrapper.add(createInputPanel(), BorderLayout.SOUTH);

        setHeaderInfo(contactName);
        loadMessages(contactId);
        centerWrapper.revalidate();
        centerWrapper.repaint();
    }

    private void setHeaderInfo(String name) {
        headerPanel.removeAll();
        headerPanel.setLayout(new MigLayout("insets 12 24 12 24, fillx"));

        JLabel lblName = new JLabel(name);
        lblName.setFont(FONT_NAME);
        lblName.setForeground(DARK_BROWN);
        headerPanel.add(lblName, "wrap");

        JLabel lblStatus = new JLabel("\u25CF Đang hoạt động");
        lblStatus.setFont(FONT_STATUS);
        lblStatus.setForeground(new Color(46, 204, 113));
        headerPanel.add(lblStatus);

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    public void loadMessages(UUID contactId) {
        chatArea.removeAll();

        try {
            List<ChatMessage> messages = messageDAO.getConversation(myId, contactId);
            if (messages == null || messages.isEmpty()) {
                chatArea.add(new JLabel("Chưa có tin nhắn nào"), "al center, gaptop 40");
                refreshChatArea();
                return;
            }

            String lastDate = "";
            for (ChatMessage msg : messages) {
                String dateStr = formatDateLabel(msg.getCreatedAt());
                if (!dateStr.equals(lastDate)) {
                    chatArea.add(createDateSeparator(dateStr), "al center, gapy 12 8, width ::220!");
                    lastDate = dateStr;
                }
                boolean isMe = msg.getSenderId().equals(myId);
                if (msg.getMessageType() == ChatMessage.MessageType.FILE && msg.getFilePath() != null) {
                    renderImageMessage(msg.getFilePath(), isMe, msg.getCreatedAt());
                } else {
                    renderMessage(msg.getContent(), isMe, msg.getCreatedAt());
                }
            }
        } catch (Exception e) {
            chatArea.add(new JLabel("Không thể tải tin nhắn"), "al center, gaptop 40");
        }

        refreshChatArea();
    }

    public void appendMessage(String content, boolean isMe) {
        String dateStr = formatDateLabel(Timestamp.valueOf(LocalDateTime.now()));
        if (shouldAddDateSeparator(dateStr)) {
            chatArea.add(createDateSeparator(dateStr), "al center, gapy 12 8, width ::220!");
        }
        renderMessage(content, isMe, Timestamp.valueOf(LocalDateTime.now()));
        refreshChatArea();
    }

    public void appendImage(String filePath, boolean isMe) {
        String dateStr = formatDateLabel(Timestamp.valueOf(LocalDateTime.now()));
        if (shouldAddDateSeparator(dateStr)) {
            chatArea.add(createDateSeparator(dateStr), "al center, gapy 12 8, width ::220!");
        }
        renderImageMessage(filePath, isMe, Timestamp.valueOf(LocalDateTime.now()));
        refreshChatArea();
    }

    private boolean shouldAddDateSeparator(String dateStr) {
        JLabel sep = findLastDateSeparator();
        return sep == null || !sep.getText().equals(getDateLabel(dateStr));
    }

    private JLabel findLastDateSeparator() {
        for (int i = chatArea.getComponentCount() - 1; i >= 0; i--) {
            if (chatArea.getComponent(i) instanceof JLabel) {
                return (JLabel) chatArea.getComponent(i);
            }
        }
        return null;
    }

    private void renderMessage(String content, boolean isMe, Timestamp timestamp) {
        String timeLabel = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        if (isMe) {
            JPanel bubble = createSentBubble(content);
            chatArea.add(bubble, "al right, gapleft push, width ::70%, gapy 2");
            JLabel timeTag = new JLabel(timeLabel);
            timeTag.setFont(FONT_TIME);
            timeTag.setForeground(TIME_COLOR);
            chatArea.add(timeTag, "al right, gapleft push, gaptop 0, gapbottom 6");
        } else {
            JPanel bubble = createReceivedBubble(content);
            chatArea.add(bubble, "al left, gapright push, width ::70%, gapy 2");
            JLabel timeTag = new JLabel(timeLabel);
            timeTag.setFont(FONT_TIME);
            timeTag.setForeground(TIME_COLOR);
            chatArea.add(timeTag, "al left, gapright push, gaptop 0, gapbottom 6");
        }
    }

    private void renderImageMessage(String filePath, boolean isMe, Timestamp timestamp) {
        String timeLabel = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        String fullPath;
        File f = new File(filePath);
        if (f.isAbsolute()) {
            fullPath = filePath;
        } else {
            fullPath = FileUtil.getUploadedFile(filePath).getAbsolutePath();
        }

        ImageIcon icon = new ImageIcon(fullPath);
        Image img = icon.getImage().getScaledInstance(MAX_IMAGE_WIDTH, -1, Image.SCALE_SMOOTH);
        ImageIcon scaled = new ImageIcon(img);

        JLabel imageLabel = new JLabel(scaled);
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (isMe) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBackground(BUBBLE_SENT);
            wrapper.putClientProperty(FlatClientProperties.STYLE,
                    "arc:18 18 4 18; borderWidth:0;");
            wrapper.add(imageLabel, BorderLayout.CENTER);
            chatArea.add(wrapper, "al right, gapleft push, width ::70%, gapy 2");
            JLabel timeTag = new JLabel(timeLabel);
            timeTag.setFont(FONT_TIME);
            timeTag.setForeground(TIME_COLOR);
            chatArea.add(timeTag, "al right, gapleft push, gaptop 0, gapbottom 6");
        } else {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBackground(BUBBLE_RECEIVED);
            wrapper.putClientProperty(FlatClientProperties.STYLE,
                    "arc:18 18 18 4; borderWidth:0;");
            wrapper.add(imageLabel, BorderLayout.CENTER);
            chatArea.add(wrapper, "al left, gapright push, width ::70%, gapy 2");
            JLabel timeTag = new JLabel(timeLabel);
            timeTag.setFont(FONT_TIME);
            timeTag.setForeground(TIME_COLOR);
            chatArea.add(timeTag, "al left, gapright push, gaptop 0, gapbottom 6");
        }
    }

    private JLabel createDateSeparator(String raw) {
        String label = getDateLabel(raw);
        JLabel sep = new JLabel(label, SwingConstants.CENTER);
        sep.setFont(FONT_SEP);
        sep.setForeground(TIME_COLOR);
        sep.setOpaque(true);
        sep.setBackground(SEP_BG);
        sep.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        return sep;
    }

    private JPanel createSentBubble(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setFont(FONT_BUBBLE);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBackground(BUBBLE_SENT);
        ta.setForeground(new Color(74, 53, 44));
        ta.putClientProperty(FlatClientProperties.STYLE,
                "arc:18 18 4 18; margin:10,15,10,15; borderWidth:0;");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(ta, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createReceivedBubble(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setEditable(false);
        ta.setFont(FONT_BUBBLE);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBackground(BUBBLE_RECEIVED);
        ta.setForeground(DARK_BROWN);
        ta.putClientProperty(FlatClientProperties.STYLE,
                "arc:18 18 18 4; margin:10,15,10,15; borderWidth:0;");

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(ta, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createIconBtn(String emoji) {
        JButton btn = new JButton(emoji);
        btn.setFont(FONT_EMOJI);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc:12; borderWidth:0; focusWidth:0; background:null; hoverBackground:#F0EBE5");
        return btn;
    }

    private String formatDateLabel(Timestamp ts) {
        if (ts == null) return "";
        LocalDate msgDate = ts.toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();
        if (msgDate.equals(today)) return "homnay";
        if (msgDate.equals(today.minusDays(1))) return "homqua";
        return msgDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String getDateLabel(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        if (raw.equals("homnay")) return "Hôm nay";
        if (raw.equals("homqua")) return "Hôm qua";
        if (raw.matches("\\d{2}/\\d{2}/\\d{4}"))
            return "Ngày " + raw;
        return raw;
    }

    private void refreshChatArea() {
        chatArea.revalidate();
        chatArea.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(
                scrollPane.getVerticalScrollBar().getMaximum()));
    }

    private static String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
