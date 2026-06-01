package hanabi.dao;

import hanabi.model.ChatMessage;
import hanabi.util.HibernateUtil;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class ChatMessageDAO extends BaseDAO<ChatMessage, UUID> {

    public ChatMessageDAO() {
        super(ChatMessage.class);
    }

    public void saveMessage(ChatMessage msg) {
        save(msg);
    }

    public List<ChatMessage> getConversation(UUID user1, UUID user2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM ChatMessage WHERE (senderId = :u1 AND receiverId = :u2)"
                    + " OR (senderId = :u2 AND receiverId = :u1)"
                    + " ORDER BY createdAt ASC", ChatMessage.class)
                    .setParameter("u1", user1)
                    .setParameter("u2", user2)
                    .list();
        }
    }
}