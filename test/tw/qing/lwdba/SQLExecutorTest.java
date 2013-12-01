package tw.qing.lwdba;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import junit.framework.TestCase;

public class SQLExecutorTest extends TestCase
{

    protected File createConfiguration(String name) throws URISyntaxException,
            IOException
    {
        File dir = new File(SQLExecutorTest.class.getResource("/").getFile());
        File f = new File(dir, String.format("%s.properties", name));
        f.createNewFile();
        return f;
    }

    protected void append(File cfg, String fmt, String poolName, Object value)
            throws IOException
    {
        FileWriter fw = new FileWriter(cfg, true);
        fw.write(String.format(fmt, poolName));
        fw.write(String.format("=%s\n", value));
        fw.close();
    }

    public void testReadConfiguration() throws Exception
    {
        final String poolName = "abc";
        final String cfgHint = "map://mycfg/abc";
        Properties prop = new Properties();
        SQLCfgManager.putConfiguration("mycfg", prop);
        prop.put(String.format(SQLExecutor.DATABASE_TYPE, poolName), "db_mem_type");
        prop.put(String.format(SQLExecutor.DRIVER_CLASSNAME, poolName),
                "com.mysql.jdbc.Driver");
        prop.put(String.format(SQLExecutor.DRIVER_URL, poolName), "db_mem_url");
        prop.put(String.format(SQLExecutor.ENCODING, poolName), "mem_encoding");
        prop.put(String.format(SQLExecutor.MAX_CONNECTION_COUNT, poolName), 35);
        prop.put(String.format(SQLExecutor.PASSWORD, poolName), "mem_password");
        prop.put(String.format(SQLExecutor.USERNAME, poolName), "mem_username");
        prop.put(String.format(SQLExecutor.SQL_FILE, poolName), "mem_sql_file");
        
        SQLExecutor executor = new SQLExecutor(cfgHint);
        
        assertEquals("db_mem_type", executor.databaseType);
        assertEquals("com.mysql.jdbc.Driver", executor.driverClassName);
        assertEquals("db_mem_url", executor.driverURL);
        assertEquals("mem_username", executor.userName);
        assertEquals("mem_password", executor.password);
        assertEquals(35, executor.maxConnectionCount);
        assertEquals("mem_encoding", executor.encoding);
        assertEquals("mem_sql_file", executor.sqlFile);
    }
    
    public void testReadConfigurationFile() throws Exception
    {

        final String cfgName = "mycfg" + System.currentTimeMillis();
        final String poolName = "abc";

        File cfg = createConfiguration(cfgName);
        append(cfg, SQLExecutor.DATABASE_TYPE, poolName, "db_type");
        append(cfg, SQLExecutor.DRIVER_CLASSNAME, poolName, "com.mysql.jdbc.Driver");
        append(cfg, SQLExecutor.DRIVER_URL, poolName, "db_url");
        append(cfg, SQLExecutor.ENCODING, poolName, "encoding");
        append(cfg, SQLExecutor.MAX_CONNECTION_COUNT, poolName, 3);
        append(cfg, SQLExecutor.PASSWORD, poolName, "password");
        append(cfg, SQLExecutor.USERNAME, poolName, "username");
        append(cfg, SQLExecutor.SQL_FILE, poolName, "sql_file");

        SQLExecutor executor = new SQLExecutor(String.format("file://%s/%s", cfgName, poolName));

        assertEquals("db_type", executor.databaseType);
        assertEquals("com.mysql.jdbc.Driver", executor.driverClassName);
        assertEquals("db_url", executor.driverURL);
        assertEquals("username", executor.userName);
        assertEquals("password", executor.password);
        assertEquals(3, executor.maxConnectionCount);
        assertEquals("encoding", executor.encoding);
        assertEquals("sql_file", executor.sqlFile);
    }

    public void testReadConfigurationInMemory() throws Exception
    {
        try
        {
            new SQLExecutor("map://abc/def");
            fail("sholud configuration key not found.");
        } catch (Exception e)
        {
            assertEquals(String.format(
                    "cannot find the memory configuration with hint: %s",
                    "map://abc/def"), e.getMessage());
        }
        
        try
        {
            new SQLExecutor("file://abc/def");
            fail("sholud configuration file not found.");
        } catch (Exception e)
        {
            assertEquals(String.format(
                    "cannot find the configuration file with hint: %s",
                    "file://abc/def"), e.getMessage());
        }

    }

}
