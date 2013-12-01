package tw.qing.lwdba;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SQLCfgManager
{
    private static Map<String, Properties> cfg = new HashMap<String, Properties>();

    public static void putConfiguration(String key, Properties prop)
    {
        cfg.put(key, prop);
    }

    public static Properties getConfiguration(String key)
    {
        return cfg.get(key);
    }

    public static String addConfiguration(String key, String poolName,
            String dbType, String driverClass, String driverUrl,
            int maxConnection, String username, String password, String sqlFile)
    {
        Properties prop = new Properties();
        prop.put(String.format(SQLExecutor.DATABASE_TYPE, poolName), dbType);
        prop.put(String.format(SQLExecutor.DRIVER_CLASSNAME, poolName),
                driverClass);
        prop.put(String.format(SQLExecutor.DRIVER_URL, poolName), driverUrl);
        prop.put(String.format(SQLExecutor.ENCODING, poolName), "UTF-8");
        prop.put(String.format(SQLExecutor.MAX_CONNECTION_COUNT, poolName),
                maxConnection);
        prop.put(String.format(SQLExecutor.PASSWORD, poolName), password);
        prop.put(String.format(SQLExecutor.USERNAME, poolName), username);
        prop.put(String.format(SQLExecutor.SQL_FILE, poolName), sqlFile);
        putConfiguration(key, prop);
        return String.format("map://%s/%s", key, poolName);
    }
    
}
