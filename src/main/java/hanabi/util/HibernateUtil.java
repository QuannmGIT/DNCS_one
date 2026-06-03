package hanabi.util;

public class HibernateUtil {

    static {
        DatabaseInitializer.initialize();
    }

    public static void init() {}
}