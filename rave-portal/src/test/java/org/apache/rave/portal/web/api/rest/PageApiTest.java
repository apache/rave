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
package org.apache.rave.portal.web.api.rest;

import org.springframework.util.ClassUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.apache.rave.portal.service.PageService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author CARLUCCI
 */
public class PageApiTest {    
    private PageApi pageApi;    
    private PageService pageService;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    
    private final long PAGE_ID = 1L;
    
    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        pageService = createMock(PageService.class);
        pageApi = new PageApi(pageService);     
    }
    
    @Test
    public void testDeletePage() {
        pageService.deletePage(PAGE_ID);
        expectLastCall();
        replay(pageService);
        
        pageApi.deletePage(PAGE_ID, response);
        
        assertThat(response.getStatus(), is(HttpStatus.NO_CONTENT.value()));   
        verify(pageService);
    }
    
    @Test
    public void tesHandleException() {
        RuntimeException re = new RuntimeException("error");        
        String value = pageApi.handleException(re, request, response);
        
        assertThat(value, is(ClassUtils.getShortName(re.getClass())));
        assertThat(response.getStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));   
    }
}
