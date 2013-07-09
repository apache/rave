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
package org.apache.rave.portal.web.tag;


import org.apache.rave.service.StaticContentFetcherService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StaticContentTagTest {
    private StaticContentFetcherService service;
    private StaticContentTag tag;
    private WebApplicationContext wContext;
    private PageContext pageContext;
    private ServletContext servletContext;
    private JspWriter writer;

    private final String VALID_CACHE_KEY = "myCacheKey";
    private final String VALID_STATIC_CONTENT = "the content of the static content artifact";
    
    @Before
    public void setup() throws JspException {        
        service = createMock(StaticContentFetcherService.class);
        wContext = createNiceMock(WebApplicationContext.class);
        pageContext = createNiceMock(PageContext.class);
        servletContext = createNiceMock(ServletContext.class);
        writer = createNiceMock(JspWriter.class);

        tag = new StaticContentTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_validKey() throws IOException, JspException {
        tag.setContentKey(VALID_CACHE_KEY);
        
        expect(service.getContent(VALID_CACHE_KEY)).andReturn(VALID_STATIC_CONTENT);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(StaticContentFetcherService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }

    @Test(expected = JspException.class)
    public void doStartTag_nullKey() throws IOException, JspException {
        tag.setContentKey(null);
        tag.doStartTag();
    }
    
    @Test
    public void getCacheKey() {
        assertThat(tag.getContentKey(), is(nullValue(String.class)));
        tag.setContentKey(VALID_CACHE_KEY);
        assertThat(tag.getContentKey(), is(VALID_CACHE_KEY));
    }
}
