package hanabi.view.Category;

import hanabi.dao.ChatMessageDAO;
import hanabi.dao.StaffDAO;
import hanabi.model.ChatMessage;
import hanabi.model.Staff;
import hanabi.service.ChatClient;
import hanabi.service.ChatServer;
import hanabi.util.FileUtil;
import hanabi.util.global;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class ChatPanel extends JPanel {

    private static final Color DIVIDER_COLOR = new Color(224, 216, 208);

    private final ChatConversationPanel conversationPanel;
    private final ChatContactPanel contactPanel;
    private final UUID myId;
    private final String myName;
    private final boolean isAdmin;
    private ChatServer server;
    private ChatClient client;
    private UUID adminId;

    public ChatPanel(UUID myId, String myName, boolean isAdmin) {
        this.myId = myId;
        this.myName = myName;
        this.isAdmin = isAdmin;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        conversationPanel = new ChatConversationPanel();
        conversationPanel.init(myId, isAdmin, this::handleSendMessage, this::handleSendImage);

        List<ChatContactPanel.ContactData> contacts = buildContacts();
        contactPanel = new ChatContactPanel(contacts, this::handleContactSelected);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactPanel, conversationPanel);
        splitPane.setDividerLocation(380);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(1);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);
        splitPane.setBackground(DIVIDER_COLOR);
        splitPane.putClientProperty("FlatLaf.style", "dividerColor: " + colorToHex(DIVIDER_COLOR));

        add(splitPane, BorderLayout.CENTER);

        initNetwork();
    }

    private void initNetwork() {
        StaffDAO staffDAO = new StaffDAO();
        Optional<Staff> adminOpt = staffDAO.findAdmin();
        adminId = adminOpt.map(Staff::getStaffId).orElse(null);

        if (isAdmin) {
            server = new ChatServer(global.CHAT_SERVER_PORT, myId,
                    (senderId, content) -> {
                        SwingUtilities.invokeLater(() -> {
                            Staff sender = staffDAO.findById(UUID.fromString(senderId));
                            if (sender != null) {
                                conversationPanel.showConversation(sender.getStaffId(), sender.getFullName());
                                conversationPanel.appendMessage(content, false);
                            }
                        });
                    },
                    (senderId, filePath) -> {
                        SwingUtilities.invokeLater(() -> {
                            Staff sender = staffDAO.findById(UUID.fromString(senderId));
                            if (sender != null) {
                                conversationPanel.showConversation(sender.getStaffId(), sender.getFullName());
                                conversationPanel.appendImage(filePath, false);
                            }
                        });
                    });
            server.start();
            System.out.println("ChatServer started on port " + global.CHAT_SERVER_PORT);
        } else {
            String host = JOptionPane.showInputDialog(this,
                    "Nhập địa chỉ IP của máy chủ Admin:",
                    global.chatServerHost);
            if (host == null || host.trim().isEmpty()) {
                host = "localhost";
            }
            global.chatServerHost = host.trim();

            client = new ChatClient(host, global.CHAT_SERVER_PORT, myId, myName,
                    (senderId, content) -> {
                        SwingUtilities.invokeLater(() -> {
                            if (adminId != null) {
                                Staff admin = staffDAO.findById(adminId);
                                String adminName = admin != null ? admin.getFullName() : "Admin";
                                conversationPanel.showConversation(adminId, adminName);
                                conversationPanel.appendMessage(content, false);
                            }
                        });
                    },
                    (senderId, filePath) -> {
                        SwingUtilities.invokeLater(() -> {
                            if (adminId != null) {
                                Staff admin = staffDAO.findById(adminId);
                                String adminName = admin != null ? admin.getFullName() : "Admin";
                                conversationPanel.showConversation(adminId, adminName);
                                conversationPanel.appendImage(filePath, false);
                            }
                        });
                    });
            client.start();
            System.out.println("ChatClient connected to " + host + ":" + global.CHAT_SERVER_PORT);
        }
    }

    private List<ChatContactPanel.ContactData> buildContacts() {
        List<ChatContactPanel.ContactData> result = new ArrayList<>();
        StaffDAO staffDAO = new StaffDAO();
        ChatMessageDAO msgDAO = new ChatMessageDAO();

        if (isAdmin) {
            List<Staff> staffList = staffDAO.findAll();
            for (Staff s : staffList) {
                if (s.getStaffId().equals(myId)) continue;
                if (!Boolean.TRUE.equals(s.getStatus())) continue;

                List<ChatMessage> msgs = msgDAO.getConversation(myId, s.getStaffId());
                String lastMsg = "";
                String lastTime = "";
                if (msgs != null && !msgs.isEmpty()) {
                    ChatMessage last = msgs.get(msgs.size() - 1);
                    lastMsg = last.getMessageType() == ChatMessage.MessageType.FILE
                            ? "[Hình ảnh]" : last.getContent();
                    lastTime = formatTime(last.getCreatedAt());
                }

                result.add(new ChatContactPanel.ContactData(
                        s.getStaffId(), s.getFullName(), lastMsg, lastTime, 0));
            }
        } else {
            if (adminId != null) {
                Staff admin = staffDAO.findById(adminId);
                String adminName = admin != null ? admin.getFullName() : "Admin";

                List<ChatMessage> msgs = msgDAO.getConversation(myId, adminId);
                String lastMsg = "";
                String lastTime = "";
                if (msgs != null && !msgs.isEmpty()) {
                    ChatMessage last = msgs.get(msgs.size() - 1);
                    lastMsg = last.getMessageType() == ChatMessage.MessageType.FILE
                            ? "[Hình ảnh]" : last.getContent();
                    lastTime = formatTime(last.getCreatedAt());
                }

                result.add(new ChatContactPanel.ContactData(
                        adminId, adminName, lastMsg, lastTime, 0));
            }
        }

        return result;
    }

    private void handleContactSelected(UUID contactId, String contactName) {
        conversationPanel.showConversation(contactId, contactName);
    }

    private void handleSendMessage(UUID receiverId, String content) {
        if (isAdmin && server != null) {
            server.sendToStaff(receiverId, content);
        } else if (client != null) {
            client.sendMessage(receiverId.toString(), content);
        } else {
            ChatMessage msg = new ChatMessage();
            msg.setMessageId(UUID.randomUUID());
            msg.setSenderId(myId);
            msg.setReceiverId(receiverId);
            msg.setContent(content);
            msg.setMessageType(ChatMessage.MessageType.TEXT);
            msg.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            new ChatMessageDAO().saveMessage(msg);
        }
    }

    private void handleSendImage(UUID receiverId, File imageFile) {
        if (isAdmin && server != null) {
            server.sendImageToStaff(receiverId, imageFile);
        } else if (client != null) {
            client.sendImage(receiverId.toString(), imageFile);
        } else {
            try {
                String savedPath = FileUtil.uploadFile(imageFile);
                ChatMessage msg = new ChatMessage();
                msg.setMessageId(UUID.randomUUID());
                msg.setSenderId(myId);
                msg.setReceiverId(receiverId);
                msg.setContent(savedPath);
                msg.setMessageType(ChatMessage.MessageType.FILE);
                msg.setFilePath(savedPath);
                msg.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                new ChatMessageDAO().saveMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        if (server != null) server.stop();
        if (client != null) client.stop();
    }

    private String formatTime(Timestamp ts) {
        if (ts == null) return "";
        LocalDateTime dt = ts.toLocalDateTime();
        if (dt.toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
            return dt.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return dt.format(DateTimeFormatter.ofPattern("dd/MM"));
    }

    private String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
