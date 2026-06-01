package hanabi.service;

import hanabi.model.ChatMessage;
import hanabi.dao.ChatMessageDAO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class ChatServer {

    private static final String UPLOAD_DIR = "src/main/resources/hanabi/assets/uploads";

    private final int port;
    private final UUID adminId;
    private final BiConsumer<String, String> onTextReceived;
    private final BiConsumer<String, String> onImageReceived;
    private ServerSocket serverSocket;
    private volatile boolean running;
    private final Map<UUID, ClientHandler> clients = new ConcurrentHashMap<>();
    private final ChatMessageDAO messageDAO = new ChatMessageDAO();

    public ChatServer(int port, UUID adminId,
            BiConsumer<String, String> onTextReceived,
            BiConsumer<String, String> onImageReceived) {
        this.port = port;
        this.adminId = adminId;
        this.onTextReceived = onTextReceived;
        this.onImageReceived = onImageReceived;
    }

    public void start() {
        running = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (running) {
                    try {
                        Socket socket = serverSocket.accept();
                        new Thread(new ClientHandler(socket)).start();
                    } catch (IOException e) {
                        if (running) e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "ChatServer-Accept").start();
    }

    public void sendToStaff(UUID staffId, String content) {
        ClientHandler handler = clients.get(staffId);
        if (handler != null) {
            handler.sendMessage(adminId.toString(), "Admin", staffId.toString(), content);
        }
        saveMessage(adminId, staffId, content, ChatMessage.MessageType.TEXT, null);
    }

    public void sendImageToStaff(UUID staffId, File imageFile) {
        byte[] imageData;
        try {
            imageData = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String fileName = imageFile.getName();
        String savedPath = saveImageFile(imageData, fileName);

        ClientHandler handler = clients.get(staffId);
        if (handler != null) {
            handler.sendImage(adminId.toString(), "Admin", staffId.toString(), savedPath, imageData);
        }
        saveMessage(adminId, staffId, savedPath, ChatMessage.MessageType.FILE, savedPath);
    }

    public void stop() {
        running = false;
        for (ClientHandler handler : clients.values()) {
            handler.disconnect();
        }
        clients.clear();
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            // ignore
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void saveMessage(UUID senderId, UUID receiverId, String content,
            ChatMessage.MessageType type, String filePath) {
        try {
            ChatMessage msg = new ChatMessage();
            msg.setMessageId(UUID.randomUUID());
            msg.setSenderId(senderId);
            msg.setReceiverId(receiverId);
            msg.setContent(content);
            msg.setMessageType(type);
            msg.setFilePath(filePath);
            msg.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            messageDAO.saveMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveImageFile(byte[] data, String originalName) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String newName = UUID.randomUUID().toString() + "_" + originalName;
            Path targetPath = uploadPath.resolve(newName);
            Files.write(targetPath, data);
            return "uploads/" + newName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private UUID staffId;
        private DataInputStream in;
        private DataOutputStream out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                String header = in.readUTF();
                String[] parts = header.split("\\|", 2);
                if (!"CONNECT".equals(parts[0])) return;

                staffId = UUID.fromString(parts[1]);
                String name = in.readUTF();

                ClientHandler existing = clients.remove(staffId);
                if (existing != null) existing.disconnect();
                clients.put(staffId, this);

                while (running) {
                    try {
                        header = in.readUTF();
                        if (header == null) break;

                        parts = header.split("\\|", 7);
                        String type = parts[0];

                        if ("MESSAGE".equals(type)) {
                            String senderIdStr = parts[1];
                            String senderName = parts[2];
                            String receiverIdStr = parts[3];
                            String content = in.readUTF();

                            saveMessage(UUID.fromString(senderIdStr), UUID.fromString(receiverIdStr),
                                    content, ChatMessage.MessageType.TEXT, null);

                            if (onTextReceived != null) {
                                onTextReceived.accept(senderIdStr, content);
                            }
                        } else if ("IMAGE".equals(type)) {
                            String senderIdStr = parts[1];
                            String senderName = parts[2];
                            String receiverIdStr = parts[3];
                            String fileName = parts[5];
                            long fileSize = Long.parseLong(parts[6]);

                            byte[] imageData = new byte[(int) fileSize];
                            in.readFully(imageData);

                            String savedPath = saveImageFile(imageData, fileName);

                            saveMessage(UUID.fromString(senderIdStr), UUID.fromString(receiverIdStr),
                                    savedPath, ChatMessage.MessageType.FILE, savedPath);

                            if (onImageReceived != null) {
                                onImageReceived.accept(senderIdStr, savedPath);
                            }
                        } else if ("DISCONNECT".equals(type)) {
                            clients.remove(staffId);
                            break;
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(staffId);
                disconnect();
            }
        }

        void sendMessage(String senderId, String senderName, String receiverId, String content) {
            try {
                out.writeUTF("MESSAGE|" + senderId + "|" + senderName + "|" + receiverId + "|" + System.currentTimeMillis());
                out.writeUTF(content);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void sendImage(String senderId, String senderName, String receiverId, String fileName, byte[] imageData) {
            try {
                out.writeUTF("IMAGE|" + senderId + "|" + senderName + "|" + receiverId + "|"
                        + System.currentTimeMillis() + "|" + fileName + "|" + imageData.length);
                out.write(imageData);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void disconnect() {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
