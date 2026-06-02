package hanabi.dao;

import hanabi.model.ChatMessage;
import hanabi.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;

public class ChatMessageDAO extends BaseDAO<ChatMessage, UUID> {

    public ChatMessageDAO() {
        super(ChatMessage.class);
    }

    public List<ChatMessage> findConversation(UUID user1, UUID user2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM ChatMessage WHERE (senderId = :u1 AND receiverId = :u2) "
                    + "OR (senderId = :u2 AND receiverId = :u1) ORDER BY createdAt ASC",
                    ChatMessage.class)
                    .setParameter("u1", user1)
                    .setParameter("u2", user2)
                    .list();
        }
    }

    public List<ChatMessage> findNewMessages(UUID user1, UUID user2, java.sql.Timestamp after) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM ChatMessage WHERE ((senderId = :u1 AND receiverId = :u2) "
                    + "OR (senderId = :u2 AND receiverId = :u1)) AND createdAt > :after ORDER BY createdAt ASC",
                    ChatMessage.class)
                    .setParameter("u1", user1)
                    .setParameter("u2", user2)
                    .setParameter("after", after)
                    .list();
        }
    }

    public ChatMessage getLastMessageBetween(UUID user1, UUID user2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM ChatMessage WHERE (senderId = :u1 AND receiverId = :u2) "
                    + "OR (senderId = :u2 AND receiverId = :u1) ORDER BY createdAt DESC",
                    ChatMessage.class)
                    .setParameter("u1", user1)
                    .setParameter("u2", user2)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}