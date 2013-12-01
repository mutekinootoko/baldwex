package tw.qing.lwdba;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import tw.qing.sys.StringManager;

public class SQLExecutor
{
    private static Logger log = Logger.getLogger(SQLExecutor.class);
    //
    static String DATABASE_TYPE = "lwdba.pool.%s.type";
    static String DRIVER_CLASSNAME = "lwdba.pool.%s.driverClassName";
    static String DRIVER_URL = "lwdba.pool.%s.driverURL";
    static String USERNAME = "lwdba.pool.%s.userName";
    static String PASSWORD = "lwdba.pool.%s.password";
    static String MAX_CONNECTION_COUNT = "lwdba.pool.%s.maxConnectionCount";
    static String ENCODING = "lwdba.pool.%s.encoding";
    static String SQL_FILE = "lwdba.pool.%s.sqlFile";
    // DBCP objects
    private GenericObjectPool connectionPool;
    private ConnectionFactory connectionFactory;
    @SuppressWarnings("unused")
    private PoolableConnectionFactory poolableConnectionFactory;
    protected PoolingDriver driver;
    //
    protected String databaseType;
    protected String driverClassName;
    protected String driverURL;
    protected String userName;
    protected String password;
    protected int maxConnectionCount = 16;
    protected String encoding;
    protected String sqlFile;
    //
    private Properties props = new Properties();
    @SuppressWarnings("rawtypes")
    private HashMap hmStatement = new HashMap();
    @SuppressWarnings("rawtypes")
    private HashMap hmConnection = new HashMap();

    /**
     * poolName should not be modified after the instance created.
     * */
    final protected String poolName;
    final protected String configuration;

    /**
     * use the `default' pool name to create the SQLExecutor instance.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException 
     */
    SQLExecutor() throws ClassNotFoundException, SQLException, FileNotFoundException
    {
        this("default");
    }

    /**
     * use the user defined pool name to create a SQLExecutor instance. the
     * <b>pool name</b> define in the system.properties file, for example:
     *
     * <pre>
     * lwdba.pool.your_pool_name.type=mysql
     * lwdba.pool.your_pool_name.driverClassName=com.mysql.jdbc.Driver
     * lwdba.pool.your_pool_name.driverURL=jdbc:mysql://localhost:3306/lwdba
     * lwdba.pool.your_pool_name.userName=root
     * lwdba.pool.your_pool_name.password=
     * lwdba.pool.your_pool_name.maxConnectionCount=32
     * lwdba.pool.your_pool_name.encoding=UTF-8
     * lwdba.pool.your_pool_name.sqlFile=sql
     * </pre>
     *
     * the pool name will be <b>`your_pool_name'</b>.
     * <br />
     * extra features:
     *  <li> poolname => use 'poolname' and 'system' as your pool name and configuration file base name
     *  <li> map://cfg-key/poolname => use 'poolname' and 'cfg-key' as your pool name and configuration map (SQLCfgManager.getConfiguration('cfg-key')) 
     *  <li> file://cfg-name/poolname => use 'poolname' and 'cfg-name.properties' as your pool name and configuration file.
     *
     * @param _poolName
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws FileNotFoundException 
     */
    SQLExecutor(String _poolName) throws ClassNotFoundException, SQLException, FileNotFoundException
    {
        boolean inMemoryConfiguration = false;
        if (_poolName == null)
        {
            poolName = "default";
            configuration = "system";
        }
        else
        {
            Pattern patt = Pattern.compile("(\\w+)://([^/]+)/(.+)$");
            Matcher m = patt.matcher(_poolName.trim());
            
            if(m.find())
            {
                boolean validPrefix = "map".equalsIgnoreCase(m.group(1)) || "file".equalsIgnoreCase(m.group(1));
                inMemoryConfiguration = "map".equalsIgnoreCase(m.group(1));
                if (!validPrefix)
                {
                    throw new IllegalArgumentException(String.format("%s is not a valid prefix in poolname", m.group(1)));
                }
                poolName = m.group(3);
                configuration = m.group(2);

                if ("file".equalsIgnoreCase(m.group(1)))
                {
                    String dir = SQLExecutor.class.getResource("/").getFile();
                    File file = new File(dir, configuration + ".properties");
                    if(!file.exists())
                    {
                        throw new FileNotFoundException(
                                String.format("cannot find the configuration file with hint: %s", _poolName));
                    }
                    log.info("set configuration file as " + configuration);
                }
                
                if ("map".equalsIgnoreCase(m.group(1)))
                {
                    if (SQLCfgManager.getConfiguration(configuration) == null)
                    {
                        throw new IllegalArgumentException(
                                String.format("cannot find the memory configuration with hint: %s", _poolName));
                    }
                    log.info("set memory configuration key as " + configuration);
                }
            }
            else
            {
                poolName = _poolName;
                configuration = "system";
            }
        }
        
        //
        if (inMemoryConfiguration)
        {
         
            readConfiguration(props);
        }
        else
            readConfigurationFile(props);
        //
        Class.forName(driverClassName);
        //
        log.info("Connection Pool - " + poolName + " is creating.");
        log.info("Connection Pool - " + poolName + ": databaseType: " + databaseType);
        log.info("Connection Pool - " + poolName + ": driverClassName: " + driverClassName);
        log.info("Connection Pool - " + poolName + ": driverURL: " + driverURL);
        log.info("Connection Pool - " + poolName + ": maxConnectionCount: " + maxConnectionCount);
        log.info("Connection Pool - " + poolName + ": encoding: " + encoding);
        //
        Class.forName("org.apache.commons.dbcp.PoolingDriver");
        driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        boolean f = poolExists(driver, poolName);
        if (f == false)
        {
            connectionPool = new GenericObjectPool(null);
            connectionFactory = new DriverManagerConnectionFactory(driverURL, props);
            poolableConnectionFactory =
                    new PoolableConnectionFactory(connectionFactory, connectionPool, null, null,
                            false, true);
            driver.registerPool(poolName, connectionPool);
        }
    }

    /**
     * show the connection pool's status
     *
     * @throws SQLException
     */
    public void printStatus() throws SQLException
    {
        System.out.println("ActiveCount: " + connectionPool.getNumActive());
        System.out.println("IdleCount: " + connectionPool.getNumIdle());
        System.out.println("MaxActiveCount: " + connectionPool.getMaxActive());
        System.out.println("MaxIdleCount: " + connectionPool.getMaxIdle());
    }

    /**
     * should close SQLExecutor when finish the sql operator
     *
     * @throws SQLException
     */
    public void close() throws SQLException
    {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.closePool(poolName);
    }

    /**
     * check if the active connection reach the allowed maxium connection limit.
     *
     * @return
     */
    public boolean isMaxConnectionReached()
    {
        return connectionPool.getNumActive() >= connectionPool.getMaxActive();
    }

    /**
     * the current active connections in the pool
     *
     * @return
     */
    public int getConnectionCount()
    {
        return connectionPool.getNumActive();
    }

    /**
     * set the limit of connections
     *
     * @param count
     */
    public void setMaxConnectionCount(int count)
    {
        connectionPool.setMaxActive(count);
    }

    public String getDatabaseType()
    {
        return databaseType;
    }

    /**
     * get a connection from the pool
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:" + poolName);
    }

    /**
     * recycle the used connection
     *
     * @param conn
     * @throws SQLException
     */
    public void recycleConnection(Connection conn) throws SQLException
    {
        conn.close();
    }

    /**
     * run the sql query opertaion
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql) throws SQLException
    {
        Connection conn = getConnection();
        //
        ResultSet rs = null;
        //
        if (conn != null)
        {
            Statement stmt = null;
            try
            {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            } catch (SQLException e)
            {
                log.error("Exception at SQL Query: " + sql, e);
                throw e;
            } finally
            {
                try
                {
                    if (stmt != null)
                    {
                        if (openResultSet(rs, stmt, conn) == false)
                        {
                            stmt.close();
                            recycleConnection(conn);
                        }
                    }
                } catch (Exception e2)
                {
                    log.error("Error while closing a statement.", e2);
                }
            }
        }
        return rs;
    }

    /**
     * run the sql update operation
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public int executeUpdate(String sql) throws SQLException
    {
        int nModified = -1;
        //
        Connection conn = getConnection();
        //
        if (conn != null)
        {
            Statement stmt = null;
            try
            {
                stmt = conn.createStatement();
                nModified = stmt.executeUpdate(sql);
            } catch (SQLException e)
            {
                log.error("Exception at SQL Update: " + sql, e);
                throw e;
            } finally
            {
                if (stmt != null)
                {
                    try
                    {
                        stmt.close();
                    } catch (Exception e2)
                    {
                        log.error("Error while closing a statement.", e2);
                    }
                }
                recycleConnection(conn);
            }
        }
        return nModified;
    }

    /**
     * get the name of sql file
     *
     * @return
     */
    public String getSQLFile()
    {
        return sqlFile;
    }

    @SuppressWarnings("unchecked")
    private boolean openResultSet(ResultSet rs, Statement stmt, Connection conn)
            throws SQLException
    {
        if (rs == null)
            return false;
        hmStatement.put(rs, stmt);
        hmConnection.put(rs, conn);
        return true;
    }

    void closeResultSet(ResultSet rs) throws SQLException
    {
        if (rs == null)
            return;
        Statement stmt = (Statement) hmStatement.remove(rs);
        if (stmt != null)
            stmt.close();
        Connection conn = (Connection) hmConnection.remove(rs);
        if (conn != null)
            conn.close();
    }
    
    
    
    /**
     * read configuration from the SQLCfgManager
     *
     * @param props
     */
    protected void readConfiguration(Properties props)
    {
        Properties cfgProp = SQLCfgManager.getConfiguration(configuration); 
        
        String _poolName = poolName;
        //
        if (_poolName == null)
            _poolName = "default";
        //
        
        databaseType = cfgProp.getProperty(String.format(DATABASE_TYPE, _poolName));
        driverClassName = cfgProp.getProperty(String.format(DRIVER_CLASSNAME, _poolName));
        driverURL = cfgProp.getProperty(String.format(DRIVER_URL, _poolName));
        userName = cfgProp.getProperty(String.format(USERNAME, _poolName));
        password = cfgProp.getProperty(String.format(PASSWORD, _poolName));
        
        Object _maxConnectionCount =
                cfgProp.get(String.format(MAX_CONNECTION_COUNT, _poolName));
        if (_maxConnectionCount != null)
        {
            try
            {
                int _mx = Integer.parseInt("" + _maxConnectionCount);
                maxConnectionCount = _mx > 0 ? _mx : 1;
            } catch (Exception ignored)
            {
                log.warn("cannot parse max connection: " + _maxConnectionCount);
            } 
        }
        encoding = cfgProp.getProperty(String.format(ENCODING, _poolName));
        sqlFile = cfgProp.getProperty(String.format(SQL_FILE, _poolName));
        
        if (sqlFile == null)
            sqlFile = "sql";
        // for MySQL
        if (encoding != null)
            props.put("characterEncoding", encoding);
        props.put("useUnicode", "true");
        props.put("user", userName);
        props.put("password", password);
    }
    /**
     * read configuration from the system.properties
     *
     * @param props
     */
    protected void readConfigurationFile(Properties props)
    {
        StringManager sm = StringManager.getManager(configuration);
        //
        String _poolName = poolName;
        //
        if (_poolName == null)
            _poolName = "default";
        //
        
        databaseType = sm.getString(String.format(DATABASE_TYPE, _poolName));
        driverClassName = sm.getString(String.format(DRIVER_CLASSNAME, _poolName));
        driverURL = sm.getString(String.format(DRIVER_URL, _poolName));
        userName = sm.getString(String.format(USERNAME, _poolName));
        password = sm.getString(String.format(PASSWORD, _poolName));
        
        String _maxConnectionCount =
                sm.getString(String.format(MAX_CONNECTION_COUNT, _poolName));
        if (_maxConnectionCount != null)
        {
            try
            {
                int _mx = Integer.parseInt(_maxConnectionCount);
                maxConnectionCount = _mx > 0 ? _mx : 1;
            } catch (Exception ignored)
            {
                log.warn("cannot parse max connection: " + _maxConnectionCount);
            } 
        }
            
        encoding = sm.getString(String.format(ENCODING, _poolName));
        sqlFile = sm.getString(String.format(SQL_FILE, _poolName));
        
        if (sqlFile == null)
            sqlFile = "sql";
        // for MySQL
        if (encoding != null)
            props.put("characterEncoding", encoding);
        props.put("useUnicode", "true");
        props.put("user", userName);
        props.put("password", password);
    }

    private boolean poolExists(PoolingDriver driver, String name) throws SQLException
    {
        String s[] = driver.getPoolNames();
        for (int i = 0; i < s.length; i++)
        {
            if (s[i].equals(name))
                return true;
        }
        return false;
    }
}
