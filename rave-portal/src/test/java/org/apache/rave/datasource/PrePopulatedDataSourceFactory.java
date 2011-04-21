package org.apache.rave.datasource;

import org.apache.rave.datasource.util.SqlFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * {@inheritDoc}
 * <p/>
 * <p/>
 * Creates a {@link javax.sql.DataSource} that is fully initialized with the schema and data referenced in the
 * application context.
 * <p/>
 * <p/>
 * Usage:
 * <code>
 *  <bean id="dataSource" class="org.apache.rave.datasource.PrePopulatedDataSourceFactory">
 *      <property name="jdbcUrl" value="jdbc://h2:mem:dbname" />
 *      <property name="driver"  value="org.h2.Driver" />
 *      <property name="user"    value="username" />
 *      <property name="password" value="password" />
 *      <property name="executeScriptQuery" value="SELECT * FROM widgets" />
 *      <property name="scriptLocations" >
 *          <list>
 *              <value>file:db/sequences/create_all_seq.sql</value>
 *              <value>file:db/tables/create_all_tables.sql</value>
 *              <value>classpath:test-data.sql</value>
 *          </list>
 *      </property>
 *  </bean>
 * </code>
 * <p/>
 */
public class PrePopulatedDataSourceFactory implements FactoryBean {

    private static Logger logger = LoggerFactory.getLogger(PrePopulatedDataSourceFactory.class);

    protected String jdbcUrl;
    protected String driver;
    protected String user;
    protected String password;
    protected String executeScriptQuery;
    protected List<Resource> scriptLocations;

    /**
     * The DataSource singleton returned by the factory.
     */
    protected DataSource dataSource;

    /**
     * Creates a new factory with no initial values for required properties.
     * <p/>
     * NOTE: If the factory is initialized using the default constructor, the required properties must be set prior to use
     */
    public PrePopulatedDataSourceFactory() {
    }

    /**
     * Creates a new factory and sets teh properties to the passed in parameters
     *
     * @param scriptLocations    {@see setScriptLocations}
     * @param jdbcUrl            {@see setJdbcUrl}
     * @param driver             {@see setDriver}
     * @param user               {@see setUser}
     * @param password           {@see setPassword}
     * @param executeScriptQuery {@see setExecuteScriptQuery}
     */
    public PrePopulatedDataSourceFactory(List<Resource> scriptLocations, String jdbcUrl, String driver, String user, String password, String executeScriptQuery) {
        setJdbcUrl(jdbcUrl);
        setDriver(driver);
        setUser(user);
        setPassword(password);
        setScriptLocations(scriptLocations);
        setExecuteScriptQuery(executeScriptQuery);
    }


    /**
     * Optional Property
     * <p/>
     * Set the query used to determine whether or not to execute the scripts on initialization
     *
     * @param executeScriptQuery the query to execute.  If there are no results of the query, the scripts referenced
     *                           in setScriptLocations will be executed.  The statement must be a select statement.
     */
    public void setExecuteScriptQuery(String executeScriptQuery) {
        this.executeScriptQuery = executeScriptQuery;
    }

    /**
     * Optional Property
     * <p/>
     * Set the user to use for the JDBC Connection
     *
     * @param user the username to use for authentication
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Optional Property
     * <p/>
     * Set the password to use for the JDBC Connection
     *
     * @param password the password to use for authentication
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Required Property
     * <p/>
     * The JDBC url of the database to connect to (or instantiate for in-memory)
     *
     * @param jdbcUrl valid JDBC URL
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Required Property
     * <p/>
     * The JDBC driver to use for the DataSource
     * <p/>
     * NOTE: The driver must be in the classpath
     *
     * @param driver The fully qualified classname
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Required Property
     * <p/>
     * Sets the locations of the files containing DDL to be executed against the database
     * <p/>
     * NOTE: Files are executed in order
     *
     * @param scriptLocations list of {@link Resource} compatible location strings
     */
    public void setScriptLocations(List<Resource> scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    @PostConstruct
    public void initializeFactory() {
        validateProperties();
    }

    public Object getObject() throws Exception {
        return getDataSource();
    }

    public Class getObjectType() {
        return DataSource.class;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Gets the singleton DataSource if created. Initializes if not already created.
     *
     * @return the DataSource
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource;
    }

    /*
      Helper methods
    */
    protected void validateProperties() {
        if (scriptLocations == null) {
            throw new IllegalArgumentException("The path to the database schema DDL is required");
        }
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("The JDBC URL of the database is required");
        }
        if (driver == null) {
            throw new IllegalArgumentException("The JDBC driver of the database connection is required");
        }
        if (user == null) {
            user = "sa";
        }
        if (password == null) {
            user = "";
        }
    }

    protected void initializeDataSource() {
        this.dataSource = createDataSource();
        populateDataSourceIfNecessary();
    }

    protected DataSource createDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(jdbcUrl);
        ds.setUsername(user);
        ds.setPassword(password);
        logger.debug("Created dataSource: " + ds.toString());
        return ds;
    }

    protected String getConnectionString() {
        return jdbcUrl;
    }

    protected void populateDataSourceIfNecessary() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            if (testExecuteScriptQuery(conn, executeScriptQuery)) {
                logger.debug("Database is empty.  Loading script files");
                executeScripts(conn, scriptLocations);
            }
        } catch (SQLException e) {
            logger.error("Error querying or populating database", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection(conn);
        }
    }

    /*
      Static Helper methods
    */
    protected static void executeScripts(Connection connection, List<Resource> resources) {
        for (Resource script : resources) {
            try {
                String sql = new SqlFileParser(script).getSQL();
                logger.debug("Executing sql:\n" + sql);
                executeSql(sql, connection);
                logger.debug("Successfully executed statement");

            } catch (IOException e) {
                throw new RuntimeException("File IO Exception while loading " + script.getFilename(), e);
            } catch (SQLException e) {
                throw new RuntimeException("SQL exception occurred loading data from " + script.getFilename(), e);
            }
        }
    }

    protected static boolean testExecuteScriptQuery(Connection conn, String executeScriptQuery) {
        boolean result;
        try {
            //If the ResultSet has any rows, the first method will return true
            result = !executeQuery(conn, executeScriptQuery).first();
        } catch (SQLException e) {
            //Only return true if the execption we got is that the table was not found
            result = e.getMessage().toLowerCase().matches("table \".*\" not found.*\n*.*");
        }
        logger.debug("Executed query " + executeScriptQuery + " with result " + result);
        return result;
    }

    protected static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Error closing connection to database", e);
            }
        }
    }

    protected static ResultSet executeQuery(Connection conn, String executeScriptQuery) throws SQLException {
        Statement statement = conn.createStatement();
        return statement.executeQuery(executeScriptQuery);
    }


    protected static void executeSql(String sql, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
    }


}