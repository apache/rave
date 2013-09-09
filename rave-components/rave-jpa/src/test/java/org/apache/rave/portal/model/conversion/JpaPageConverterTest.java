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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.Page;
import org.apache.rave.model.PageType;
import org.apache.rave.model.PageUser;
import org.apache.rave.model.Region;
import org.apache.rave.portal.model.JpaPage;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageConverterTest {

    @Autowired
    private JpaPageConverter pageConverter;

    @Test
    public void noConversion() {
        Page page = new JpaPage();
        assertThat(pageConverter.convert(page), is(sameInstance(page)));
    }

    @Test
    public void nullConversion() {
        Page page = null;
        assertThat(pageConverter.convert(page), is(nullValue()));
    }

    @Test
    public void newPage() {
        Page page = new PageImpl();
        page.setId("1");
        page.setMembers(new ArrayList<PageUser>());
        page.setName("name");
        page.setOwnerId("");
        page.setContextId("context");
        page.setPageLayout(new PageLayoutImpl());
        page.setPageType(PageType.USER.toString());
        page.setParentPage(new PageImpl("1"));
        page.setRegions(new ArrayList<Region>());
        page.setSubPages(new ArrayList<Page>());

        JpaPage converted = pageConverter.convert(page);
        assertThat(converted, is(not(sameInstance(page))));
        assertThat(converted, is(instanceOf(JpaPage.class)));
        assertThat(converted.getId(), is(equalTo(page.getId())));
        assertThat(converted.getParentPage().getId(), is(equalTo(page.getParentPage().getId())));
        assertThat(converted.getRegions(), is(equalTo(page.getRegions())));
        assertThat(converted.getMembers(), is(equalTo(page.getMembers())));
        assertThat(converted.getName(), is(equalTo(page.getName())));
        assertThat(converted.getOwnerId(), is(equalTo(page.getOwnerId())));
        assertThat(converted.getContextId(), is(equalTo(page.getContextId())));
        assertThat(converted.getPageLayout().getCode(), is(equalTo(page.getPageLayout().getCode())));
        assertThat(converted.getPageType(), is(equalTo(page.getPageType())));
        assertThat(converted.getSubPages(), is(equalTo(page.getSubPages())));
    }
}