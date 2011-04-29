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

package org.apache.rave.portal.repository.impl.util;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carlucci
 */
public class JpaUtilTest {
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
    
    // Private helper functions for the tests
    private List<Object> generatePopulatedList(int size) {
        List<Object> list = new ArrayList<Object>();
        for (int i=0; i < size; i++) {
            list.add(new Object());
        }
        return list;
    }
}
