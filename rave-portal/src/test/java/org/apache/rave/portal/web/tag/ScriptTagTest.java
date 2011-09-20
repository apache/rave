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
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class ScriptTagTest {
    public static final String SCRIPT = "SCRIPT";
    public static final String SCRIPT_2 = "SCRIPT2";
    private ScriptTag tag;
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
          tag = new ScriptTag();
          tag.setPageContext(pageContext);
      }

      @Test
      public void doStartTag_valid() throws IOException, JspException {

          List<String> strings = new ArrayList<String>();
          strings.add(SCRIPT);
          expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE)).andReturn(strings);
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
          verify(writer);
      }

      @Test
      public void doStartTag_multi() throws IOException, JspException {

          List<String> strings = new ArrayList<String>();
          strings.add(SCRIPT);
          strings.add(SCRIPT_2);
          expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE)).andReturn(strings);
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
          expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE)).andReturn(null);
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

          expect(service.getScriptBlocks(ScriptLocation.BEFORE_RAVE)).andReturn(null);
          replay(service);

          JspWriter writer = createNiceMock(JspWriter.class);
          replay(writer);

          expect(pageContext.getOut()).andReturn(writer);
          replay(pageContext);

          tag.setLocation(null);
          int result = tag.doStartTag();
      }

}
