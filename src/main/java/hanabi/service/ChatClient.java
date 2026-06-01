package hanabi.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ChatClient {

    private static final String UPLOAD_DIR = "src/main/resources/hanabi/assets/uploads";

    private final String host;
    private final int port;
    private final UUID myId;
    private final String myName;
    private final BiConsumer<String, String> onTextReceived;
    private final BiConsumer<String, String> onImageReceived;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private volatile boolean running;

    public ChatClient(String host, int port, UUID myId, String myName,
            BiConsumer<String, String> onTextReceived,
            BiConsumer<String, String> onImageReceived) {
        this.host = host;
        this.port = port;
        this.myId = myId;
        this.myName = myName;
        this.onTextReceived = onTextReceived;
        this.onImageReceived = onImageReceived;
    }

    public void start() {
        running = true;
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF("CONNECT|" + myId.toString());
                out.writeUTF(myName);
                out.flush();

                while (running) {
                    try {
                        String header = in.readUTF();
                        if (header == null) break;

                        String[] parts = header.split("\\|", 7);
                        String type = parts[0];

                        if ("MESSAGE".equals(type)) {
                            String senderId = parts[1];
                            String content = in.readUTF();

                            if (onTextReceived != null) {
                                onTextReceived.accept(senderId, content);
                            }
                        } else if ("IMAGE".equals(type)) {
                            String senderId = parts[1];
                            String fileName = parts[5];
                            long fileSize = Long.parseLong(parts[6]);

                            byte[] imageData = new byte[(int) fileSize];
                            in.readFully(imageData);

                            String savedPath = saveImageFile(imageData, fileName);

                            if (onImageReceived != null) {
                                onImageReceived.accept(senderId, savedPath);
                            }
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }, "ChatClient-Read").start();
    }

    public void sendMessage(String receiverId, String content) {
        try {
            out.writeUTF("MESSAGE|" + myId.toString() + "|" + myName + "|" + receiverId + "|" + System.currentTimeMillis());
            out.writeUTF(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(String receiverId, File file) {
        try {
            byte[] imageData = Files.readAllBytes(file.toPath());
            out.writeUTF("IMAGE|" + myId.toString() + "|" + myName + "|" + receiverId + "|"
                    + System.currentTimeMillis() + "|" + file.getName() + "|" + imageData.length);
            out.write(imageData);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        try {
            if (out != null) {
                out.writeUTF("DISCONNECT|" + myId.toString());
                out.flush();
            }
        } catch (IOException e) {
            // ignore
        }
        disconnect();
    }

    private void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            // ignore
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

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
