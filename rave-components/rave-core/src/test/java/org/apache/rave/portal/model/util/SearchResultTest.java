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

package org.apache.rave.portal.model.util;

import org.apache.rave.rest.model.SearchResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link org.apache.rave.rest.model.SearchResult}
 */
public class SearchResultTest {
    SearchResult searchResult;

    @Test
    public void testGetNumberOfPages_noFractal() throws Exception {
        searchResult.setPageSize(15);
        assertEquals("Total results is 1 * page size", 1, searchResult.getNumberOfPages());
    }

    @Test
    public void testGetNumberOfPages_addPage() throws Exception {
        searchResult.setPageSize(10);
        assertEquals("Total results is 1.5 * page size", 2, searchResult.getNumberOfPages());
    }

    @Test
    public void testGetNumberOfPages_noPageSize() throws Exception {
        searchResult.setPageSize(0);
        assertEquals("Division by 0", 0, searchResult.getNumberOfPages());
    }

    @Test
    public void testGetCurrentPage() {
        assertEquals(0, searchResult.getOffset());
        assertEquals("No pagesize, current page is 1", 1, searchResult.getCurrentPage());
        searchResult.setPageSize(50);
        assertEquals("Pagesize larger than total results", 1, searchResult.getCurrentPage());
        searchResult.setPageSize(10);
        assertEquals("First page", 1, searchResult.getCurrentPage());
        searchResult.setOffset(10);
        assertEquals(10, searchResult.getOffset());
        assertEquals("Second page", 2, searchResult.getCurrentPage());
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        List<? extends String> results = new ArrayList<String>();
        searchResult = new SearchResult(results, 15);
    }

}

