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

package org.apache.rave.portal.web.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link NavigationItem}
 */
public class NavigationItemTest {

    @Test
    public void testCreateEmptyNavigationItem() throws Exception {
        NavigationItem navigationItem = new NavigationItem();
        assertNotNull(navigationItem);
        assertNull(navigationItem.getName());
        assertNull(navigationItem.getUrl());
        assertNull(navigationItem.getNameParam());
        assertFalse(navigationItem.isExpanded());
        assertFalse(navigationItem.isSelected());
        assertTrue(navigationItem.getChildNavigationItems().isEmpty());
        assertFalse(navigationItem.isHasChildren());
    }

    @Test
    public void testNameUrlConstructor() throws Exception {
        String name = "Home";
        String nameParams = "test1";
        String url="/index.html";
        NavigationItem navigationItem = new NavigationItem(name, nameParams, url);
        assertNotNull(navigationItem);
        assertEquals(name, navigationItem.getName());
        assertEquals(url, navigationItem.getUrl());
        assertEquals(nameParams, navigationItem.getNameParam());
        assertFalse(navigationItem.isExpanded());
        assertFalse(navigationItem.isSelected());
        assertTrue(navigationItem.getChildNavigationItems().isEmpty());
        assertFalse(navigationItem.isHasChildren());
    }

    @Test
    public void testSettersGetters() throws Exception {
        String name = "Home";
        String nameParams = "test1";
        String url="/index.html";

        NavigationItem navigationItem = new NavigationItem();
        navigationItem.setName(name);
        navigationItem.setNameParam(nameParams);
        navigationItem.setUrl(url);
        navigationItem.setExpanded(true);
        navigationItem.setSelected(true);
        NavigationItem childItem = new NavigationItem("About", null, "/about/index.html");
        navigationItem.addChildNavigationItem(childItem);

        assertEquals(name, navigationItem.getName());
        assertEquals(url, navigationItem.getUrl());
        assertEquals(nameParams, navigationItem.getNameParam());
        assertTrue(navigationItem.isExpanded());
        assertTrue(navigationItem.isSelected());
        assertEquals(childItem, navigationItem.getChildNavigationItems().get(0));
        assertTrue(navigationItem.isHasChildren());

    }
}
