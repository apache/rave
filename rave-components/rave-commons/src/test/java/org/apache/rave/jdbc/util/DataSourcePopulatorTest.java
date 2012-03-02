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

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataSourcePopulatorTest {

    public static final String INSERT = "INSERT INTO BAR VALUES('FOO'); INSERT INTO FOO VALUES ('BAR'); ";
    private static final String CHECK_QUERY = "QUERY FOR TABLES";
    private static final String TABLE_NOT_FOUND_MSG = "TABLE \"BOO\" NOT FOUND";

    private DataSource dataSource;
    private Connection connection;
    private DataSourcePopulator populator;
    private List<Resource> resourceList;

    @Before
    public void setup() throws SQLException {
        dataSource = createNiceMock(DataSource.class);
        connection = createNiceMock(Connection.class);
        expect(dataSource.getConnection()).andReturn(connection);
        replay(dataSource);
        resourceList = createScriptList();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalid_config() {
        populator = new DataSourcePopulator(null, CHECK_QUERY);
        populator.initialize(dataSource);
    }

    @Test
    public void null_check() throws SQLException {
        Statement populate = createNiceMock(Statement.class);
        expect(populate.execute(INSERT)).andReturn(true).once();
        replay(populate);

        expect(connection.createStatement()).andReturn(populate).once();
        connection.close();
        expectLastCall();
        replay(connection);

        populator = new DataSourcePopulator(resourceList, null);
        populator.initialize(dataSource);
        verify(populate);
    }

    @Test
    public void valid_execute() throws SQLException {
        Statement check = createNiceMock(Statement.class);
        expect(check.executeQuery(CHECK_QUERY)).andThrow(new SQLException(TABLE_NOT_FOUND_MSG));
        replay(check);

        Statement populate = createNiceMock(Statement.class);
        expect(populate.execute(INSERT)).andReturn(true).once();
        replay(populate);

        expect(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)).andReturn(check).once();
        expect(connection.createStatement()).andReturn(populate).once();
        connection.close();
        expectLastCall();
        replay(connection);

        populator = new DataSourcePopulator(resourceList, CHECK_QUERY);
        populator.initialize(dataSource);
        verify(populate);
    }

    @Test
    public void valid_execute_nodata() throws SQLException {
        ResultSet rs = createNiceMock(ResultSet.class);
        expect(rs.first()).andReturn(false);
        replay(rs);

        Statement check = createNiceMock(Statement.class);
        expect(check.executeQuery(CHECK_QUERY)).andReturn(rs);
        replay(check);

        Statement populate = createNiceMock(Statement.class);
        expect(populate.execute(INSERT)).andReturn(true).once();
        replay(populate);

        expect(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)).andReturn(check).once();
        expect(connection.createStatement()).andReturn(populate).once();
        connection.close();
        expectLastCall();
        replay(connection);

        populator = new DataSourcePopulator(resourceList, CHECK_QUERY);
        populator.initialize(dataSource);
        verify(populate);
    }

    @Test
    public void invalid_SQL() throws SQLException {
        Statement check = createNiceMock(Statement.class);
        expect(check.executeQuery(CHECK_QUERY)).andThrow(new SQLException(TABLE_NOT_FOUND_MSG));
        replay(check);

        Statement populate = createNiceMock(Statement.class);
        expect(populate.execute(INSERT)).andThrow(new SQLException());
        replay(populate);

        expect(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)).andReturn(check).once();
        expect(connection.createStatement()).andReturn(populate).once();
        connection.close();
        expectLastCall();
        replay(connection);

        populator = new DataSourcePopulator(resourceList, CHECK_QUERY);
        try {
            populator.initialize(dataSource);
        } catch (Exception e) {
            verify(connection);
        }
    }

    @Test
    public void failed_close() throws SQLException {
        ResultSet rs = createNiceMock(ResultSet.class);
        expect(rs.first()).andReturn(true);
        replay(rs);

        Statement check = createNiceMock(Statement.class);
        expect(check.executeQuery(CHECK_QUERY)).andReturn(rs);
        replay(check);

        expect(connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)).andReturn(check).once();
        connection.close();
        expectLastCall().andThrow(new SQLException());
        replay(connection);

        populator = new DataSourcePopulator(resourceList, CHECK_QUERY);
        populator.initialize(dataSource);
        //Test passes because no exception was bubbled up on the failed connection closing
        assertThat(true, is(true));
    }

    @Test (expected = RuntimeException.class)
    public void failedConnection() throws SQLException {

        populator = new DataSourcePopulator();
        populator.setExecuteScriptQuery(CHECK_QUERY);
        populator.setScriptLocations(resourceList);

        dataSource = createNiceMock(DataSource.class);
        expect(dataSource.getConnection()).andThrow(new SQLException());
        replay(dataSource);

        populator.initialize(dataSource);

    }

    private List<Resource> createScriptList() {
        List<Resource> resourceList = new ArrayList<Resource>();
        resourceList.add(new ClassPathResource("test-data.sql"));
        return resourceList;
    }
}
