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


import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class AbstractContextAwareSingletonBeanDependentTagTest {
    private static final String TEST_STRING = "TEST STRING";
    private final Integer TEST_OBJ_1 = 42;
    private PageContext pageContext;
    private AbstractContextAwareSingletonBeanDependentTag tag;
    private WebApplicationContext context;

    @Before
    public void setup() throws JspException {
        context = createNiceMock(WebApplicationContext.class);
        expect(context.getBean(Integer.class)).andReturn(TEST_OBJ_1).once();
        replay(context);
        ServletContext servletContext = createNiceMock(ServletContext.class);
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(context);
        replay(servletContext);
        pageContext = createNiceMock(PageContext.class);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        tag = new AbstractContextAwareSingletonBeanDependentTag<Integer>(Integer.class){ };
        tag.setPageContext(pageContext);
    }

    @Test
    public void getFromContext_single() throws JspException {
        replay(pageContext);
        assertThat((Integer)tag.getBean(), is(sameInstance(TEST_OBJ_1)));
        //Verify the context was called once
        verify(context);
    }
    @Test
    public void getFromContext_three() throws JspException {
        replay(pageContext);
        assertThat((Integer)tag.getBean(), is(sameInstance(TEST_OBJ_1)));
        assertThat((Integer)tag.getBean(), is(sameInstance(TEST_OBJ_1)));
        assertThat((Integer)tag.getBean(), is(sameInstance(TEST_OBJ_1)));
        //Verify the context was called once
        verify(context);
    }

    @Test
    public void validWrite() throws IOException, JspException {
        JspWriter outStream = createNiceMock(JspWriter.class);
        expect(pageContext.getOut()).andReturn(outStream).once();
        replay(pageContext);
        outStream.print(TEST_STRING);
        expectLastCall().once();
        replay(outStream);

        tag.writeString(TEST_STRING);
        verify(outStream);
    }

    @Test(expected = JspException.class)
    public void invalidWrite_io() throws IOException, JspException {
        JspWriter outStream = createNiceMock(JspWriter.class);
        expect(pageContext.getOut()).andReturn(outStream).once();
        replay(pageContext);
        outStream.print(TEST_STRING);
        expectLastCall().andThrow(new IOException());
        replay(outStream);

        tag.writeString(TEST_STRING);
    }

    @Test
    public void getContext() {
        pageContext = createNiceMock(PageContext.class);
        ServletRequest request = createNiceMock(ServletRequest.class);
        replay(request);
        expect(pageContext.getRequest()).andReturn(request).anyTimes();
        replay(pageContext);
        tag.setPageContext(pageContext);

        RenderContext context = tag.getContext();
        assertThat(context, is(not(nullValue())));
        assertThat(context.getProperties(), is(not(nullValue())));
    }
}
