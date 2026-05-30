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

    public List<ChatMessage> getChatHistory() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM ChatMessage ORDER BY createdAt ASC", ChatMessage.class)
                    .list();
        }
    }
}
