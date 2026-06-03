package hanabi.dao;

import hanabi.model.ChatMessage;
import hanabi.util.SupabaseUtil;
import hanabi.util.SupabaseUtil.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class ChatMessageDAO extends BaseDAO<ChatMessage> {

    @Override
    protected String tableName() { return "chat_messages"; }

    @Override
    protected String idColumn() { return "message_id"; }

    private final RowMapper<ChatMessage> mapper = this::mapRow;

    @Override
    protected RowMapper<ChatMessage> rowMapper() { return mapper; }

    private ChatMessage mapRow(ResultSet rs) throws SQLException {
        ChatMessage m = new ChatMessage();
        m.setMessageId(UUID.fromString(rs.getString("message_id")));
        m.setSenderId(UUID.fromString(rs.getString("sender_id")));
        m.setReceiverId(UUID.fromString(rs.getString("receiver_id")));
        m.setContent(rs.getString("content"));
        String type = rs.getString("message_type");
        if (type != null) m.setMessageType(ChatMessage.MessageType.valueOf(type));
        m.setFilePath(rs.getString("file_path"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }

    public List<ChatMessage> findConversation(UUID user1, UUID user2) {
        return SupabaseUtil.queryList(
                "SELECT * FROM chat_messages WHERE " +
                "(sender_id = ?::uuid AND receiver_id = ?::uuid) OR " +
                "(sender_id = ?::uuid AND receiver_id = ?::uuid) " +
                "ORDER BY created_at ASC",
                rowMapper(), user1, user2, user2, user1);
    }

    public List<ChatMessage> findNewMessages(UUID user1, UUID user2, Timestamp after) {
        return SupabaseUtil.queryList(
                "SELECT * FROM chat_messages WHERE " +
                "((sender_id = ?::uuid AND receiver_id = ?::uuid) OR " +
                "(sender_id = ?::uuid AND receiver_id = ?::uuid)) AND " +
                "created_at > ? " +
                "ORDER BY created_at ASC",
                rowMapper(), user1, user2, user2, user1, after);
    }

    public ChatMessage getLastMessageBetween(UUID user1, UUID user2) {
        return SupabaseUtil.querySingle(
                "SELECT * FROM chat_messages WHERE " +
                "(sender_id = ?::uuid AND receiver_id = ?::uuid) OR " +
                "(sender_id = ?::uuid AND receiver_id = ?::uuid) " +
                "ORDER BY created_at DESC LIMIT 1",
                rowMapper(), user1, user2, user2, user1);
    }

    public void save(ChatMessage msg) {
        SupabaseUtil.update(
                "INSERT INTO chat_messages (message_id, sender_id, receiver_id, content, message_type, file_path, created_at) " +
                "VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?, ?)",
                msg.getMessageId(), msg.getSenderId(), msg.getReceiverId(),
                msg.getContent(), msg.getMessageType() != null ? msg.getMessageType().name() : null,
                msg.getFilePath(), msg.getCreatedAt());
    }

    public void update(ChatMessage msg, UUID id) {
        SupabaseUtil.update(
                "UPDATE chat_messages SET sender_id=?::uuid, receiver_id=?::uuid, content=?, message_type=?, file_path=?, created_at=? " +
                "WHERE message_id=?::uuid",
                msg.getSenderId(), msg.getReceiverId(), msg.getContent(),
                msg.getMessageType() != null ? msg.getMessageType().name() : null,
                msg.getFilePath(), msg.getCreatedAt(), id);
    }
}
