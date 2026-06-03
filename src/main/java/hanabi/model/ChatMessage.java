package hanabi.model;

import java.sql.Timestamp;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;

public class ChatMessage {

    public enum MessageType {
        TEXT, FILE
    }

    private UUID messageId;
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private MessageType messageType;
    private String filePath;
    private Timestamp createdAt;

    public ChatMessage() {}

    public UUID getMessageId() { return messageId; }
    public void setMessageId(UUID messageId) { this.messageId = messageId; }

    public UUID getSenderId() { return senderId; }
    public void setSenderId(UUID senderId) { this.senderId = senderId; }

    public UUID getReceiverId() { return receiverId; }
    public void setReceiverId(UUID receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}