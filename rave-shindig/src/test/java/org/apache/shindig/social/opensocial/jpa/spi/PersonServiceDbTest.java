/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.shindig.social.opensocial.jpa.spi;

import org.apache.shindig.protocol.model.FilterOperation;
import org.apache.shindig.protocol.model.SortOrder;
import org.apache.shindig.social.opensocial.jpa.api.FilterCapability;
import org.apache.shindig.social.opensocial.jpa.api.FilterSpecification;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Test for {@link PersonServiceDb}
 */
public class PersonServiceDbTest {
    PersonServiceDb service;

    @Before
    public void setUp() throws Exception {
        service = new PersonServiceDb();
    }

    /**
     * Should return 0 as position to add (or actually ignore) if the filter is invalid
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testAddInvalidFilterClause() throws Exception {
        final String nullString = null;
        assertFalse(FilterSpecification.isValid(nullString));

        StringBuilder sb = new StringBuilder();
        FilterCapability filterable = createMock(FilterCapability.class);
        CollectionOptions co = new CollectionOptions();
        int lastPost = 20;
        expect(filterable.findFilterableProperty(co.getFilter(), co.getFilterOperation())).andReturn(nullString);

        replay(filterable);
        int returnPos = service.addFilterClause(sb, filterable, co, lastPost);
        assertEquals(0, returnPos);
        verify(filterable);
    }

    /**
     * Shows that a special filter will never return a String in case it's a special operation and
     * returns position 0 ("do not add filter")
     *
     * @throws Exception if something goes wrong
     */
    @Test
    public void testAddSpecialFilterClause() throws Exception {
        final String specialOperation = FilterSpecification.SPECIAL_OPERATION;
        assertTrue(FilterSpecification.isSpecial(specialOperation));

        StringBuilder sb = new StringBuilder();
        CollectionOptions co = new CollectionOptions();
        co.setFilter(specialOperation);
        co.setFilterOperation(FilterOperation.contains);
        int lastPos = 20;
        FilterCapability filterable = createMock(FilterCapability.class);
        expect(filterable.findFilterableProperty(co.getFilter(), co.getFilterOperation())).andReturn(specialOperation);
        replay(filterable);

        int returnPos = service.addFilterClause(sb, filterable, co, lastPos);
        assertEquals(0, returnPos);
        assertEquals("", sb.toString());

        verify(filterable);
    }


    @Test
    public void testAddLikeFilterClause() throws Exception {
        final String myProperty = "myProperty";
        StringBuilder sb = new StringBuilder();
        FilterCapability filterable = createMock(FilterCapability.class);
        CollectionOptions co = new CollectionOptions();
        co.setFilterOperation(FilterOperation.contains);
        co.setFilterValue("myValue");
        co.setFilter(myProperty);
        int lastPost = 0;

        expect(filterable.findFilterableProperty(myProperty, FilterOperation.contains))
                .andReturn(myProperty);
        replay(filterable);
        int returnPos = service.addFilterClause(sb, filterable, co, lastPost);

        assertEquals(1, returnPos);
        assertEquals("p.myProperty like  ?1", sb.toString());
        assertEquals("%myValue%", co.getFilter());
        verify(filterable);
    }

    @Test
    public void testAddOrderClause() throws Exception {
        StringBuilder sb = new StringBuilder();
        CollectionOptions co = new CollectionOptions();

        service.addOrderClause(sb, co);
        assertEquals("", sb.toString());

        co.setSortBy(PersonService.TOP_FRIENDS_SORT);
        service.addOrderClause(sb, co);
        assertEquals(" order by f.score ", sb.toString());

        co.setSortBy("name");
        sb = new StringBuilder();
        service.addOrderClause(sb, co);
        assertEquals(" order by p.name.familyName, p.name.givenName ", sb.toString());

        sb = new StringBuilder();
        co.setSortOrder(SortOrder.ascending);
        service.addOrderClause(sb, co);
        assertEquals(" order by p.name.familyName, p.name.givenName  asc ", sb.toString());

        sb = new StringBuilder();
        co.setSortOrder(SortOrder.descending);
        service.addOrderClause(sb, co);
        assertEquals(" order by p.name.familyName, p.name.givenName  desc ", sb.toString());

        co.setSortBy("randomProperty");
        co.setSortOrder(null);
        sb = new StringBuilder();
        service.addOrderClause(sb, co);
        assertEquals(" order by p.randomProperty", sb.toString());

    }
}
