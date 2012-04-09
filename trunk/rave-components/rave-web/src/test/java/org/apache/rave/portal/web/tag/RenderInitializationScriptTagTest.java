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

import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.util.ModelKeys;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static org.easymock.EasyMock.*;

public class RenderInitializationScriptTagTest {
    private RenderInitializationScriptTag tag;
    private PageContext pageContext;
    private ServletRequest request;
    private JspWriter writer;

    private final StringBuilder VALID_SCRIPT = new StringBuilder("<script>abcd</script>");

    @Before
    public void setup() throws JspException {
        pageContext = createNiceMock(PageContext.class);
        request = createNiceMock(ServletRequest.class);
        writer = createNiceMock(JspWriter.class);

        tag = new RenderInitializationScriptTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_beforeRave_existingAttribute() throws Exception {
        tag.setLocation(ScriptLocation.BEFORE_RAVE);

        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.BEFORE_RAVE_INIT_SCRIPT)).andReturn(VALID_SCRIPT);
        expect(pageContext.getOut()).andReturn(writer);
        writer.print(VALID_SCRIPT.toString());
        expectLastCall();
        replay(pageContext, request, writer);

        int returnValue = tag.doStartTag();
        assertThat(returnValue, is(TagSupport.EVAL_BODY_INCLUDE));

        verify(pageContext, request, writer);
    }

    @Test
    public void doStartTag_afterRave_existingAttribute() throws Exception {
        tag.setLocation(ScriptLocation.AFTER_RAVE);

        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.AFTER_RAVE_INIT_SCRIPT)).andReturn(VALID_SCRIPT);
        expect(pageContext.getOut()).andReturn(writer);
        writer.print(VALID_SCRIPT.toString());
        expectLastCall();
        replay(pageContext, request, writer);

        int returnValue = tag.doStartTag();
        assertThat(returnValue, is(TagSupport.EVAL_BODY_INCLUDE));

        verify(pageContext, request, writer);
    }

    @Test
    public void doStartTag_afterRave_noExistingAttribute() throws Exception {
        tag.setLocation(ScriptLocation.AFTER_RAVE);

        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.AFTER_RAVE_INIT_SCRIPT)).andReturn(null);
        replay(pageContext, request, writer);

        int returnValue = tag.doStartTag();
        assertThat(returnValue, is(TagSupport.EVAL_BODY_INCLUDE));

        verify(pageContext, request, writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doStartTag_invalidLocation() throws Exception {
        tag.setLocation(ScriptLocation.AFTER_LIB);
        tag.doStartTag();
    }

    @Test(expected = JspException.class)
    public void doStartTag_beforeRave_existingAttribute_renderException() throws Exception {
        tag.setLocation(ScriptLocation.BEFORE_RAVE);

        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.BEFORE_RAVE_INIT_SCRIPT)).andReturn(VALID_SCRIPT);
        expect(pageContext.getOut()).andReturn(writer);
        writer.print(VALID_SCRIPT.toString());
        expectLastCall().andThrow(new IOException("error"));
        replay(pageContext, request, writer);

        tag.doStartTag();

        verify(pageContext, request, writer);
    }
}
