package hanabi.util;

//
public class global {
    public static final String assetsPath = "hanabi/";
    public static final String SUPABASE_USER = "postgres";
    public static final String SUPABASE_PASSWORD = "FslfcEfkXdmHuEaC";
    public static final String SUPABASE_HOST = "aws-1-ap-southeast-2.pooler.supabase.com";
    public static final String SUPABASE_PORT = "5432"; // 5432 direct / 6543 pooler
    public static final String SUPABASE_URL = "jdbc:postgresql://" + SUPABASE_HOST + ":" + SUPABASE_PORT
            + "/postgres?user=postgres.hzqkraytekfrahbsgdvy&password=" + SUPABASE_PASSWORD;
    // jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?user=postgres.hzqkraytekfrahbsgdvy&password=[YOUR-PASSWORD]
    public static final String DB_NAME = "StoreManagement";
    public static double COMMISSION_RATE = 0.01;
    public static final String BANKCODE = "MB";
    public static final String ACCOUNTNUMBER = "33669917012007";
    public static final String ACCOUNTNAME = "HANABI CAFE";
    public static final String ADDINFO = "Hanabi Cafe Payment";

    public static String getAssetsPath() {
        return assetsPath;
    }

    public static String getPathResource(String ContentDir, String name_resource) {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + ContentDir + "/"
                + name_resource;
    }
}
