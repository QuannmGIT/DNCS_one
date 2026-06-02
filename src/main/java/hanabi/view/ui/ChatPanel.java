package hanabi.view.ui;

import hanabi.Main;
import hanabi.dao.ChatMessageDAO;
import hanabi.dao.StaffDAO;
import hanabi.model.ChatMessage;
import hanabi.model.Staff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ChatPanel extends JPanel {

    private final ChatMessageDAO messageDAO;
    private final StaffDAO staffDAO;
    private final ChatConversationPanel conversationPanel;
    private final ChatContactPanel contactPanel;

    public ChatPanel() {
        this.messageDAO = new ChatMessageDAO();
        this.staffDAO = new StaffDAO();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Khởi tạo Panel Nhắn tin (Bên phải)
        conversationPanel = new ChatConversationPanel(msg -> {
            messageDAO.save(msg);
            loadContacts();
        });

        // Khởi tạo Panel Danh sách liên hệ (Bên trái)
        contactPanel = new ChatContactPanel(new ArrayList<>(), item -> {
            new SwingWorker<List<ChatMessage>, Void>() {
                @Override
                protected List<ChatMessage> doInBackground() {
                    UUID currentUserId = Main.authService.getCurrentUser().getStaffId();
                    return messageDAO.findConversation(currentUserId, item.staff.getStaffId());
                }

                @Override
                protected void done() {
                    try {
                        UUID currentUserId = Main.authService.getCurrentUser().getStaffId();
                        String displayName = item.staff.getFullName() != null
                                ? item.staff.getFullName()
                                : item.staff.getStaffName();
                        List<ChatMessage> msgs = get();
                        conversationPanel.loadConversation(
                                item.staff.getStaffId(), displayName, msgs, currentUserId);
                    } catch (Exception e) {
                        System.err.println("[ChatPanel] load conversation failed: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }.execute();
        });

        // Chia đôi màn hình
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, contactPanel, conversationPanel);
        splitPane.setDividerSize(1); // Nét chia siêu mỏng
        splitPane.setDividerLocation(320);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setContinuousLayout(true);

        add(splitPane, BorderLayout.CENTER);
    }

    public void loadContacts() {
        new SwingWorker<List<ChatContactItem>, Void>() {
            @Override
            protected List<ChatContactItem> doInBackground() {
                if (Main.authService.getCurrentUser() == null) return List.of();
                UUID currentUserId = Main.authService.getCurrentUser().getStaffId();
                List<Staff> allStaff = staffDAO.findAll();
                List<ChatContactItem> items = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                for (Staff s : allStaff) {
                    if (!s.getStaffId().equals(currentUserId)) {
                        ChatMessage lastMsg = messageDAO.getLastMessageBetween(
                                currentUserId, s.getStaffId());
                        String lastContent = "Chưa có tin nhắn";
                        String lastTime = "";

                        if (lastMsg != null) {
                            lastContent = lastMsg.getMessageType() == ChatMessage.MessageType.FILE
                                    ? "[Tệp đính kèm]" : lastMsg.getContent();
                            lastTime = sdf.format(lastMsg.getCreatedAt());
                        }
                        items.add(new ChatContactItem(s, lastContent, lastTime));
                    }
                }
                return items;
            }

            @Override
            protected void done() {
                try {
                    List<ChatContactItem> items = get();
                    contactPanel.refreshContacts(items);
                } catch (Exception e) {
                    System.err.println("[ChatPanel] loadContacts failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    static class ChatContactItem {
        Staff staff;
        String lastMessage;
        String lastTime;

        ChatContactItem(Staff staff, String lastMessage, String lastTime) {
            this.staff = staff;
            this.lastMessage = lastMessage;
            this.lastTime = lastTime;
        }
    }
}