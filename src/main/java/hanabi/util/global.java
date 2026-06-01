package hanabi.util;

public class global {
    public static final String assetsPath = "hanabi/";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/StoreManagement";
    public static final String DB_NAME = "StoreManagement";
    public static final String USER = "root";
    public static final String PASSWORD = "";
    public static final int CHAT_SERVER_PORT = 12345;
    public static String chatServerHost = "localhost";
    public static double COMMISSION_RATE = 0.01;

    public static String getAssetsPath() {
        return assetsPath;
    }

    /**
     * @param ContentDir    : Path Dir of assets or resources
     * @param name_resource : Name of resource or file and file extension
     * @return String : Path of resources
     * @version 1.0.1 Alpha test ver
     * @author MinhCreator
     *         load content and assets resources
     */
    public static String getPathResource(String ContentDir, String name_resource) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + ContentDir + "/"
                + name_resource;
    }
}