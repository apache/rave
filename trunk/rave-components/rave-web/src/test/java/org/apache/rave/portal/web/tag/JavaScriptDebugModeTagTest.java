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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
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
import static org.junit.Assert.assertThat;

public class JavaScriptDebugModeTagTest {
    private PortalPreferenceService service;
    private JavaScriptDebugModeTag tag;
    private WebApplicationContext wContext;
    private PageContext pageContext;
    private ServletContext servletContext;
    private JspWriter writer;

    final String DEBUG_ON = "1";
    final String DEBUG_OFF = "0";

    @Before
    public void setup() throws JspException {
        service = createMock(PortalPreferenceService.class);
        wContext = createNiceMock(WebApplicationContext.class);
        pageContext = createNiceMock(PageContext.class);
        servletContext = createNiceMock(ServletContext.class);
        writer = createNiceMock(JspWriter.class);

        tag = new JavaScriptDebugModeTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_debugOn() throws IOException, JspException {
        PortalPreference portalPreference = new PortalPreferenceImpl(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE, DEBUG_ON);

        expect(service.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE)).andReturn(portalPreference);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PortalPreferenceService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_debugOff() throws IOException, JspException {
        PortalPreference portalPreference = new PortalPreferenceImpl(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE, DEBUG_OFF);

        expect(service.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE)).andReturn(portalPreference);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PortalPreferenceService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_exception() throws IOException, JspException {
        PortalPreference portalPreference = new PortalPreferenceImpl(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE, DEBUG_OFF);

        expect(service.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE)).andThrow(new RuntimeException("error"));
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PortalPreferenceService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_nullPreference() throws IOException, JspException {
        PortalPreference portalPreference = null;

        expect(service.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE)).andReturn(portalPreference);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PortalPreferenceService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_emptyPreference() throws IOException, JspException {
        PortalPreference portalPreference = new PortalPreferenceImpl(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE, "");

        expect(service.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE)).andReturn(portalPreference);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PortalPreferenceService.class)).andReturn(service).anyTimes();
        expect(pageContext.getOut()).andReturn(writer);
        replay(service, pageContext, servletContext, wContext, writer);

        int result = tag.doStartTag();

        assertThat(result, is(javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE));
        verify(service, pageContext, servletContext, wContext, writer);
    }
}
