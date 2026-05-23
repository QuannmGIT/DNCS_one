package hanabi.util;

public class global {
    public static String assetsPath = "hanabi/";

    public static String getAssetsPath() {
        return assetsPath;
    }

    public static void setAssetsPath(String assetsPath) {
        global.assetsPath = assetsPath;
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