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
package org.apache.rave.portal.repository;

import org.apache.rave.portal.model.Page;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import static org.junit.Assert.assertThat;

/**
 * @author mfranklin
 *         Date: 4/19/11
 *         Time: 9:13 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/dataContext.xml", "file:src/main/webapp/WEB-INF/applicationContext.xml"})
public class JpaPageRepositoryTest {

    private static final String USER_ID = "canonical";
    private static final String INVALID_USER = "BadUserId";
    private static final String WIDGET_URL = "http://www.google.com/ig/modules/wikipedia.xml";

    @Autowired
    private PageRepository repository;

    @Test
    public void getAllPages_validUser_validPageSet() {
        List<Page> pages = repository.getAllPages(USER_ID);
        assertThat(pages, CoreMatchers.notNullValue());
        assertThat(pages.size(), CoreMatchers.equalTo(1));
        assertThat(pages.get(0).getRegions().size(), CoreMatchers.equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().size(), CoreMatchers.equalTo(2));
        assertThat(pages.get(0).getRegions().get(0).getRegionWidgets().get(0).getWidget().getUrl(), CoreMatchers.equalTo(WIDGET_URL));
    }
    @Test
    public void getAllPages_invalidUser_emptySet() {
        List<Page> pages = repository.getAllPages(INVALID_USER);
        assertThat(pages.isEmpty(), CoreMatchers.is(true));
    }
    @Test
    public void getAllPages_nullUser_emptySet() {
        List<Page> pages = repository.getAllPages(null);
        assertThat(pages.isEmpty(), CoreMatchers.is(true));
    }
}
