/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model;

import org.apache.rave.model.PageLayout;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 12:39 PM
 */
public class MongoDbPageLayoutTest {
    private MongoDbPageLayout pageLayout;
    private String id;

    @Before
    public void setup(){
        pageLayout = new MongoDbPageLayout();
        id = "123";
        pageLayout.setId(id);
    }

    @Test
    public void equals_Same(){
          assertTrue(pageLayout.equals(pageLayout));
    }

    @Test
    public void equals_Diff_Instance(){
        Object o = new Object();
        assertFalse(pageLayout.equals(o));
    }

    @Test
    public void equals_Super(){
        PageLayout p = new MongoDbPageLayout();
        p.setCode("code");
        assertFalse(pageLayout.equals(p));
    }

    @Test
    public void equals_Null_Id(){
        MongoDbPageLayout p_1 = new MongoDbPageLayout();
        MongoDbPageLayout p_2 = new MongoDbPageLayout();
        p_1.setId("321");
        assertFalse(p_1.equals(p_2));
        assertFalse(p_2.equals(p_1));
    }

    @Test
    public void equals_Both_Null(){
        pageLayout.setId(null);
        MongoDbPageLayout p = new MongoDbPageLayout();
        assertTrue(pageLayout.equals(p));
    }

    @Test
    public void equals_Same_Id(){
        MongoDbPageLayout p_1 = new MongoDbPageLayout();
        MongoDbPageLayout p_2 = new MongoDbPageLayout();
        p_1.setId("321");
        p_2.setId("321");
        assertTrue(p_1.equals(p_2));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(pageLayout.hashCode());
    }

    @Test
    public void hashCode_Id_Null(){
        pageLayout.setId(null);
        assertNotNull(pageLayout.hashCode());
    }
}
