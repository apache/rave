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

package org.apache.rave.portal.service;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.service.impl.DefaultPageService;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;

public class PageServiceTest {
    private PageRepository pageRepository;
    private PageService pageService;

    @Before
    public void setup() {
        pageRepository = createNiceMock(PageRepository.class);
        pageService = new DefaultPageService(pageRepository);
    }

    @Test
    public void getAllPages() {
        final String VALID_USER_ID = "jcian";
        final List<Page> VALID_PAGES = new ArrayList<Page>();

        expect(pageRepository.getAllPages(VALID_USER_ID)).andReturn(VALID_PAGES);
        replay(pageRepository);

        assertThat(pageService.getAllPages(VALID_USER_ID), CoreMatchers.sameInstance(VALID_PAGES));
    }
}