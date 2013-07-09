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

import org.apache.rave.model.Person;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.apache.rave.portal.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PersonTagTest {
    public static final String VALID_ID = "1234";
    public static final String VAR = "VAR";
    private PersonRepository repository;
    private WebApplicationContext wContext;
    private PageContext pageContext;
    private ServletContext servletContext;
    private JspWriter writer;
    private PersonTag tag;

    @Before
    public void setup() throws JspException {
        repository = createMock(PersonRepository.class);
        wContext = createNiceMock(WebApplicationContext.class);
        pageContext = createNiceMock(PageContext.class);
        servletContext = createNiceMock(ServletContext.class);
        writer = createNiceMock(JspWriter.class);

        tag = new PersonTag();
        tag.setPageContext(pageContext);
    }

    @Test
    public void doStartTag_allSet() throws IOException, JspException {
        Person p = new PersonImpl();

        expect(repository.get(VALID_ID)).andReturn(p);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PersonRepository.class)).andReturn(repository).anyTimes();
        pageContext.setAttribute(VAR, p, PageContext.REQUEST_SCOPE);
        expectLastCall();
        replay(repository, pageContext, servletContext, wContext, writer);


        tag.setId(VALID_ID);
        tag.setScope(PageContext.REQUEST_SCOPE);
        tag.setVar(VAR);
        int result = tag.doEndTag();

        assertThat(result, is(Tag.EVAL_PAGE));
        verify(repository, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_varSet() throws IOException, JspException {
        Person p = new PersonImpl();

        expect(repository.get(VALID_ID)).andReturn(p);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PersonRepository.class)).andReturn(repository).anyTimes();
        pageContext.setAttribute(VAR, p, PageContext.PAGE_SCOPE);
        expectLastCall();
        replay(repository, pageContext, servletContext, wContext, writer);


        tag.setId(VALID_ID);
        tag.setVar(VAR);
        int result = tag.doEndTag();

        assertThat(result, is(Tag.EVAL_PAGE));
        verify(repository, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_novar() throws IOException, JspException {
        Person p = new PersonImpl();
        p.setUsername("FOO");

        expect(repository.get(VALID_ID)).andReturn(p);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PersonRepository.class)).andReturn(repository).anyTimes();
        expect(pageContext.getOut()).andReturn(writer).anyTimes();
        writer.print(p.getUsername());
        expectLastCall();
        replay(repository, pageContext, servletContext, wContext, writer);

        tag.setId(VALID_ID);
        int result = tag.doEndTag();

        assertThat(result, is(Tag.EVAL_PAGE));
        verify(repository, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_noResult() throws IOException, JspException {
        Person p = new PersonImpl();

        expect(repository.get(null)).andReturn(null);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PersonRepository.class)).andReturn(repository).anyTimes();
        pageContext.removeAttribute(VAR);
        expectLastCall();
        replay(repository, pageContext, servletContext, wContext, writer);

        tag.setVar(VAR);
        int result = tag.doEndTag();

        assertThat(result, is(Tag.EVAL_PAGE));
        verify(repository, pageContext, servletContext, wContext, writer);
    }

    @Test
    public void doStartTag_noResult_ScopeSet() throws IOException, JspException {
        Person p = new PersonImpl();

        expect(repository.get(null)).andReturn(null);
        expect(pageContext.getServletContext()).andReturn(servletContext).anyTimes();
        expect(servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE)).andReturn(wContext).anyTimes();
        expect(wContext.getBean(PersonRepository.class)).andReturn(repository).anyTimes();
        pageContext.removeAttribute(VAR, PageContext.REQUEST_SCOPE);
        expectLastCall();
        replay(repository, pageContext, servletContext, wContext, writer);

        tag.setVar(VAR);
        tag.setScope(PageContext.REQUEST_SCOPE);
        int result = tag.doEndTag();

        assertThat(result, is(Tag.EVAL_PAGE));
        verify(repository, pageContext, servletContext, wContext, writer);
    }
}
