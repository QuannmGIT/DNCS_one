package hanabi.util;

public class global {
    public static final String assetsPath = "hanabi/";
    public static final String MONGO_URI = "mongodb+srv://stevegaming567_db_user:YG8cDUhQ9f1AQsyC@storemanagement.ku4zavy.mongodb.net/?appName=StoreManagement";
    public static final String DB_NAME = "StoreManagement";
    public static double COMMISSION_RATE = 0.01;
    public static final String BANKCODE = "MB"; // replace your Bank code
    public static final String ACCOUNTNUMBER = "33669917012007";
    public static final String ACCOUNTNAME = "HANABI CAFE";
    public static final String ADDINFO = "Hanabi Cafe Payment";

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