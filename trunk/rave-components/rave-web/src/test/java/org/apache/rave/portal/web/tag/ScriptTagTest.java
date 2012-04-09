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
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ScriptTagTest {
    public static final String SCRIPT = "SCRIPT";
    public static final String SCRIPT_2 = "SCRIPT2";
    private ScriptTag tag;
    private ScriptManager service;
    private PageContext pageContext;
    private RenderContext context;

    @Before
    public void setup() throws JspException {
        context = new RenderContext();
        context.setProperties(new HashMap());
        service = createNiceMock(ScriptManager.class);
        WebApplicationContext wContext = createNiceMock(WebApplicationContext.class);
        expect(wContext.getBean(ScriptManager.class)).andReturn(service).anyTimes();
        replay(wContext);
        ServletContext servletContext = createNiceMock(ServletContext.class);
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext);
        replay(servletContext);
        ServletRequest request = createNiceMock(ServletRequest.class);
        expect(request.getAttribute("_RENDER_CONTEXT")).andReturn(context).anyTimes();
        replay(request);
        pageContext = createNiceMock(PageContext.class);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(pageContext.getRequest()).andReturn(request).anyTimes();
        tag = new ScriptTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_valid() throws IOException, JspException {

        List<String> strings = new ArrayList<String>();
        strings.add(SCRIPT);
        expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context)).andReturn(strings);
        replay(service);

        JspWriter writer = createNiceMock(JspWriter.class);
        writer.print(SCRIPT);
        expectLastCall();
        replay(writer);

        expect(pageContext.getOut()).andReturn(writer);
        replay(pageContext);

        tag.setLocation(ScriptLocation.BEFORE_RAVE);
        int result = tag.doStartTag();
        assertThat(result, is(equalTo(1)));
        assertThat(tag.getLocation() == null, is(true));
        verify(writer);
    }

    @Test
    public void doStartTag_multi() throws IOException, JspException {

        List<String> strings = new ArrayList<String>();
        strings.add(SCRIPT);
        strings.add(SCRIPT_2);
        expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context)).andReturn(strings);
        replay(service);

        JspWriter writer = createNiceMock(JspWriter.class);
        writer.print(SCRIPT);
        expectLastCall();
        writer.print(SCRIPT_2);
        expectLastCall();
        replay(writer);

        expect(pageContext.getOut()).andReturn(writer).anyTimes();
        replay(pageContext);

        tag.setLocation(ScriptLocation.BEFORE_RAVE);
        int result = tag.doStartTag();
        assertThat(result, is(equalTo(1)));
        verify(writer);
    }

    @Test
    public void doStartTag_skip() throws IOException, JspException {

        List<String> strings = new ArrayList<String>();
        strings.add(SCRIPT);
        strings.add(SCRIPT_2);
        expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context)).andReturn(null);
        replay(service);

        JspWriter writer = createNiceMock(JspWriter.class);
        replay(writer);

        expect(pageContext.getOut()).andReturn(writer).anyTimes();
        replay(pageContext);

        tag.setLocation(ScriptLocation.BEFORE_RAVE);
        int result = tag.doStartTag();
        assertThat(result, is(equalTo(TagSupport.SKIP_BODY)));
        verify(writer);
    }

    @Test(expected = JspException.class)
    public void doStartTag_null_location() throws IOException, JspException {

        expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context)).andReturn(null);
        replay(service);

        JspWriter writer = createNiceMock(JspWriter.class);
        replay(writer);

        expect(pageContext.getOut()).andReturn(writer);
        replay(pageContext);

        tag.setLocation(null);
        int result = tag.doStartTag();
    }

}
