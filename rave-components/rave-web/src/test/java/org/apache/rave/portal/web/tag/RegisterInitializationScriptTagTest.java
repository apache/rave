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
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import static org.easymock.EasyMock.*;

public class RegisterInitializationScriptTagTest {
    private RegisterInitializationScriptTag tag;
    private PageContext pageContext;
    private BodyContent bodyContent;
    private ServletRequest request;
    private final StringBuilder VALID_SCRIPT = new StringBuilder("<script>abcd</script>");
    
    @Before
    public void setup() throws JspException {
        pageContext = createNiceMock(PageContext.class);
        bodyContent = createNiceMock(BodyContent.class);
        request = createNiceMock(ServletRequest.class);

        tag = new RegisterInitializationScriptTag();
        tag.setPageContext(pageContext);
    }
    
    @Test
    public void doAfterBody_beforeRave_noExistingAttribute() {
        tag.setLocation(ScriptLocation.BEFORE_RAVE);
        tag.setBodyContent(bodyContent);

        expect(bodyContent.getString()).andReturn(VALID_SCRIPT.toString());
        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.BEFORE_RAVE_INIT_SCRIPT)).andReturn(null);
        replay(bodyContent, pageContext, request);
        tag.doAfterBody();
        verify(bodyContent, pageContext, request);
    }

    @Test
    public void doAfterBody_beforeRave_existingAttribute() {
        StringBuilder existingSB = new StringBuilder("<script>first</script>");
        
        tag.setLocation(ScriptLocation.BEFORE_RAVE);
        tag.setBodyContent(bodyContent);

        expect(bodyContent.getString()).andReturn(VALID_SCRIPT.toString());
        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.BEFORE_RAVE_INIT_SCRIPT)).andReturn(existingSB);
        replay(bodyContent, pageContext, request);
        tag.doAfterBody();
        verify(bodyContent, pageContext, request);
    }

    @Test
    public void doAfterBody_afterRave_noExistingAttribute() {
        tag.setLocation(ScriptLocation.AFTER_RAVE);
        tag.setBodyContent(bodyContent);

        expect(bodyContent.getString()).andReturn(VALID_SCRIPT.toString());
        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.AFTER_RAVE_INIT_SCRIPT)).andReturn(null);
        replay(bodyContent, pageContext, request);
        tag.doAfterBody();
        verify(bodyContent, pageContext, request);
    }

    @Test
    public void doAfterBody_afterRave_existingAttribute() {
        StringBuilder existingSB = new StringBuilder("<script>first</script>");

        tag.setLocation(ScriptLocation.AFTER_RAVE);
        tag.setBodyContent(bodyContent);

        expect(bodyContent.getString()).andReturn(VALID_SCRIPT.toString());
        expect(pageContext.getRequest()).andReturn(request);
        expect(request.getAttribute(ModelKeys.AFTER_RAVE_INIT_SCRIPT)).andReturn(existingSB);
        replay(bodyContent, pageContext, request);
        tag.doAfterBody();
        verify(bodyContent, pageContext, request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doAfterBody_invalidLocation() {
        tag.setLocation(ScriptLocation.AFTER_LIB);
        tag.setBodyContent(bodyContent);

        expect(bodyContent.getString()).andReturn(VALID_SCRIPT.toString());
        replay(bodyContent, pageContext, request);
        tag.doAfterBody();
        verify(bodyContent, pageContext, request);
    }

}
