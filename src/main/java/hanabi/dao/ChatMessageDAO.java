package hanabi.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import hanabi.model.ChatMessage;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatMessageDAO extends BaseDAO<ChatMessage> {

    public ChatMessageDAO() {
        super(ChatMessage.class, "chat_messages");
    }

    public List<ChatMessage> findConversation(UUID user1, UUID user2) {
        return getCollection().find(
                Filters.or(
                        Filters.and(Filters.eq("senderId", user1), Filters.eq("receiverId", user2)),
                        Filters.and(Filters.eq("senderId", user2), Filters.eq("receiverId", user1))
                )
        ).sort(Sorts.ascending("createdAt")).into(new ArrayList<>());
    }

    public List<ChatMessage> findNewMessages(UUID user1, UUID user2, Timestamp after) {
        return getCollection().find(
                Filters.and(
                        Filters.or(
                                Filters.and(Filters.eq("senderId", user1), Filters.eq("receiverId", user2)),
                                Filters.and(Filters.eq("senderId", user2), Filters.eq("receiverId", user1))
                        ),
                        Filters.gt("createdAt", after)
                )
        ).sort(Sorts.ascending("createdAt")).into(new ArrayList<>());
    }

    public ChatMessage getLastMessageBetween(UUID user1, UUID user2) {
        return getCollection().find(
                Filters.or(
                        Filters.and(Filters.eq("senderId", user1), Filters.eq("receiverId", user2)),
                        Filters.and(Filters.eq("senderId", user2), Filters.eq("receiverId", user1))
                )
        ).sort(Sorts.descending("createdAt")).first();
    }
}