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

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.web.renderer.RenderService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**

 */
public class RegionWidgetTagTest {

    private static final String WIDGET_TYPE = "TEST";
    private static final String RENDERED = "RENDERED";
    private RegionWidgetTag tag;
    private RenderService service;
    private PageContext pageContext;

    @Before
    public void setup() throws JspException {
        service = createNiceMock(RenderService.class);
        WebApplicationContext context = createNiceMock(WebApplicationContext.class);
        expect(context.getBean(RenderService.class)).andReturn(service).anyTimes();
        replay(context);
        ServletContext servletContext = createNiceMock(ServletContext.class);
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(context);
        replay(servletContext);
        pageContext = createNiceMock(PageContext.class);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        tag = new RegionWidgetTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_valid() throws IOException, JspException {
        RegionWidget regionWidget = new RegionWidget();
        Widget widget = new Widget();
        regionWidget.setWidget(widget);
        widget.setType(WIDGET_TYPE);

        Set<String> strings = new HashSet<String>();
        strings.add(WIDGET_TYPE);

        expect(service.getSupportedWidgetTypes()).andReturn(strings);
        expect(service.render(regionWidget)).andReturn(RENDERED);
        replay(service);

        JspWriter writer = createNiceMock(JspWriter.class);
        writer.print(RENDERED);
        expectLastCall();
        replay(writer);

        expect(pageContext.getOut()).andReturn(writer);
        replay(pageContext);

        tag.setRegionWidget(regionWidget);
        int result = tag.doStartTag();
        assertThat(result, is(equalTo(1)));
        verify(writer);
    }

    @Test(expected = JspException.class)
    public void doStartTag_nullWidget() throws JspException {
        replay(pageContext);
        tag.doStartTag();
    }

    @Test(expected = JspException.class)
    public void doStartTag_unsupportedWidget() throws JspException {
        replay(pageContext);

        RegionWidget regionWidget = new RegionWidget();
        Widget widget = new Widget();
        regionWidget.setWidget(widget);
        widget.setType("INVALID");

        Set<String> strings = new HashSet<String>();
        strings.add(WIDGET_TYPE);

        expect(service.getSupportedWidgetTypes()).andReturn(strings);
        replay(service);

        tag.setRegionWidget(regionWidget);
        tag.doStartTag();
    }
}
