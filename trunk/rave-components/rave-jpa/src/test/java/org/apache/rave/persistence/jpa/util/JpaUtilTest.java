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

package org.apache.rave.persistence.jpa.util;

import org.apache.rave.portal.model.BasicEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Test class for {@link JpaUtil}
 *
 * @author carlucci
 */
public class JpaUtilTest {

    private EntityManager mockManager;

    @Before
    public void setup() {
        mockManager = createNiceMock(EntityManager.class);
    }


    @Test
    public void getSingleResult_emptyList() {        
        List<Object> list = new ArrayList<Object>();        
        Object object = JpaUtil.getSingleResult(list);
        assertNull(object);
    }
    
    @Test
    public void getSingleResult_nullList() {            
        Object object = JpaUtil.getSingleResult(null);
        assertNull(object);
    }
    
    @Test
    public void getSingleResult_singleObjectPopulatedList() {                               
        List<Object> populatedList = generatePopulatedList(1);        
        Object object = JpaUtil.getSingleResult(populatedList);
        assertSame(object, populatedList.get(0));
    }
        
    @Test (expected = IncorrectResultSizeDataAccessException.class)
    public void getSingleResult_multipleObjectPopulatedList() {                               
        List<Object> populatedList = generatePopulatedList(5);        
        JpaUtil.getSingleResult(populatedList);
    }

    @Test
    public void saveOrUpdate_save() {
        Object o = new Object();
        mockManager.persist(o);
        expectLastCall();
        replay(mockManager);

        Object r =JpaUtil.saveOrUpdate(null, mockManager, o);
        assertThat(r, is(sameInstance(o)));
        verify(mockManager);
    }

    @Test
    public void saveOrUpdate_update() {
        Object o = new Object();
        expect(mockManager.merge(o)).andReturn(new Object());
        replay(mockManager);

        Object r = JpaUtil.saveOrUpdate(new Object(), mockManager, o);
        assertThat(r, is(not(sameInstance(o))));
        verify(mockManager);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getPagedResultList() {
        TypedQuery<BasicEntity> query = createNiceMock(TypedQuery.class);
        List<BasicEntity> emptyList = new ArrayList<BasicEntity>();
        int offset = 0;
        int pagesize = 10;
        // nothing to assert, but at least we know which methods are called in query
        expect(query.setFirstResult(offset)).andReturn(query);
        expect(query.setMaxResults(pagesize)).andReturn(query);
        expect(query.getResultList()).andReturn(emptyList);
        replay(query);
        JpaUtil.getPagedResultList(query, offset, pagesize);
        verify(query);
    }

    // Private helper functions for the tests
    private List<Object> generatePopulatedList(int size) {
        List<Object> list = new ArrayList<Object>();
        for (int i=0; i < size; i++) {
            list.add(new Object());
        }
        return list;
    }
}
