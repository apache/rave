/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.jdbc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * {@inheritDoc}
 * <p/>
 * <p/>
 * Executes a given set of SQL scripts against a javax.sql.DataSource
 * <p/>
 * <p/>
 * Usage:
 * <code>
 *    <bean id="dataSourcePopulator" class="org.apache.rave.jdbc.DataSourcePopulator">
 *       <property name="executeScriptQuery" value="SELECT * FROM widgets" />
 *       <property name="scriptLocations" >
 *          <list>
 *              <value>file:db/sequences/create_all_seq.sql</value>
 *              <value>file:db/tables/create_all_tables.sql</value>
*               <value>classpath:test-data.sql</value>
 *          </list>
 *       </property>
 *    </bean>
 * </code>
 * <p/>
 */
public class DataSourcePopulator implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(DataSourcePopulator.class);

    protected String executeScriptQuery;
    protected List<Resource> scriptLocations;

    /**
     * Creates a new populator initial values for required properties.
     * <p/>
     * NOTE: If the populator is initialized using the default constructor, the required properties must be set prior to use
     */
    public DataSourcePopulator() {
    }

    /**
     * Creates a new populator and sets the properties to the passed in parameters
     *
     * @param scriptLocations    {@see setScriptLocations}
     * @param executeScriptQuery {@see setExecuteScriptQuery}
     */
    public DataSourcePopulator(List<Resource> scriptLocations, String executeScriptQuery) {
        this.scriptLocations = scriptLocations;
        this.executeScriptQuery = executeScriptQuery;

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
     * Required Property
     * <p/>
     * Sets the locations of the files containing DDL to be executed against the database
     * <p/>
     * NOTE: Files are executed in order
     *
     * @param scriptLocations list of {@link org.springframework.core.io.Resource} compatible location strings
     */
    public void setScriptLocations(List<Resource> scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    public void initialize(DataSource dataSource) {
        validateProperties();
        populateDataSourceIfNecessary(dataSource);
    }

    /*
      Helper methods
    */
    protected void validateProperties() {
        if (scriptLocations == null) {
            throw new IllegalArgumentException("The path to the database schema DDL is required");
        }
    }

    protected void populateDataSourceIfNecessary(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            if (testExecuteScriptQuery(conn, executeScriptQuery)) {
                logger.debug("Database is empty.  Loading script files");
                executeScripts(conn, scriptLocations);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying or populating database", e);
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
            result = executeScriptQuery == null || !executeQuery(conn, executeScriptQuery).first();
        } catch (SQLException e) {
            //Only return true if the execption we got is that the table was not found
        	logger.warn("SQL Exception while running the query used to determine if the data should be loaded: "+e.getMessage());
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
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(executeScriptQuery);
    }


    protected static void executeSql(String sql, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
    }


}