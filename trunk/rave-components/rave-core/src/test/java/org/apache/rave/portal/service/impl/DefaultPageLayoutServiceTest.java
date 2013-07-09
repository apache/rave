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

package org.apache.rave.portal.service.impl;

import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.service.PageLayoutService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class DefaultPageLayoutServiceTest {
    private PageLayoutService pageLayoutService;
    private PageLayoutRepository pageLayoutRepository;
    
    private final String VALID_LAYOUT_CODE = "columns_1";
    private final String INVALID_LAYOUT_CODE = "qqqqqq";
    private PageLayout validPageLayout;

    @Before
    public void setup() {
        pageLayoutRepository = createMock(PageLayoutRepository.class);      
        pageLayoutService = new DefaultPageLayoutService(pageLayoutRepository);
        
        validPageLayout = new PageLayoutImpl();
        validPageLayout.setCode(VALID_LAYOUT_CODE);
    }

    @Test
    public void getAll() {
        final List<PageLayout> VALID_PAGE_LAYOUTS = new ArrayList<PageLayout>();

        expect(pageLayoutRepository.getAll()).andReturn(VALID_PAGE_LAYOUTS);
        replay(pageLayoutRepository);

        assertThat(pageLayoutService.getAll(), sameInstance(VALID_PAGE_LAYOUTS));

        verify(pageLayoutRepository);
    }

    @Test
    public void getAllUserSelectable() {
        final List<PageLayout> VALID_PAGE_LAYOUTS = new ArrayList<PageLayout>();

        expect(pageLayoutRepository.getAllUserSelectable()).andReturn(VALID_PAGE_LAYOUTS);
        replay(pageLayoutRepository);

        assertThat(pageLayoutService.getAllUserSelectable(), sameInstance(VALID_PAGE_LAYOUTS));

        verify(pageLayoutRepository);
    }
    
    @Test
    public void getPageLayoutByCode() {
        expect(pageLayoutRepository.getByPageLayoutCode(VALID_LAYOUT_CODE)).andReturn(validPageLayout);
        replay(pageLayoutRepository);
        assertThat(pageLayoutService.getPageLayoutByCode(VALID_LAYOUT_CODE), sameInstance(validPageLayout));
        verify(pageLayoutRepository);
    }

    @Test
    public void getPageLayoutByCode_invalidPageLayout() {
        expect(pageLayoutRepository.getByPageLayoutCode(INVALID_LAYOUT_CODE)).andReturn(null);
        replay(pageLayoutRepository);
        assertThat(pageLayoutService.getPageLayoutByCode(INVALID_LAYOUT_CODE), is(nullValue()));
        verify(pageLayoutRepository);
    }    
}