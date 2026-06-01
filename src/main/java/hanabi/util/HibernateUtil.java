package hanabi.util;

import hanabi.model.Invoice;
import hanabi.model.Order;
import hanabi.model.OrderDetail;
import hanabi.model.Product;
import hanabi.model.ChatMessage;
import hanabi.model.Salary;
import hanabi.model.Staff;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            DBInitializer.initialize();
            TableDBInitializer.initialize();

            SessionFactory sf = new Configuration()
                    .configure("hanabi/backend/hibernate.cfg.xml")
                    .addAnnotatedClass(Staff.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Order.class)
                    .addAnnotatedClass(Invoice.class)
                    .addAnnotatedClass(OrderDetail.class)
                    .addAnnotatedClass(Salary.class)
                    .addAnnotatedClass(ChatMessage.class)
                    .buildSessionFactory();

            DataInitializer.initialize(sf);
            return sf;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
