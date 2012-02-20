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
package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.PageType;
import org.apache.rave.portal.repository.PageTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaPageTypeRepositoryTest {
    private final Long USER_PAGE_TYPE_ENTITY_ID = 1L;
    private final String USER_PAGE_TYPE_CODE = "USER";
    private final String USER_PAGE_TYPE_DESCRIPTION = "Standard user pages which are only accessible by the page owner";

    private final Long PERSON_PROFILE_PAGE_TYPE_ENTITY_ID = 2L;
    private final String PERSON_PROFILE_PAGE_TYPE_CODE = "PERSON_PROFILE";
    private final String PERSON_PROFILE_PAGE_TYPE_DESCRIPTION = "Person Profile pages which are accessible by anyone and contain Person Profile information";

    private final Long SUB_PAGE_PAGE_TYPE_ENTITY_ID = 3L;
    private final String SUB_PAGE_PAGE_TYPE_CODE = "SUB_PAGE";
    private final String SUB_PAGE_PAGE_TYPE_DESCRIPTION = "Sub pages are contained within another page";


    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PageTypeRepository repository;

    @Test
    public void getAll() {
        List<PageType> allPageTypes = repository.getAll();
        assertThat(allPageTypes.size(), is(3));
        assertThat(allPageTypes.get(0).getCode(), is(PERSON_PROFILE_PAGE_TYPE_CODE));
        assertThat(allPageTypes.get(1).getCode(), is(SUB_PAGE_PAGE_TYPE_CODE));
        assertThat(allPageTypes.get(2).getCode(), is(USER_PAGE_TYPE_CODE));
    }
    
    @Test
    public void getByCode_UserType() {
        PageType userPageType = repository.getByCode(USER_PAGE_TYPE_CODE);
        assertThat(userPageType.getEntityId(), is(USER_PAGE_TYPE_ENTITY_ID));
        assertThat(userPageType.getCode(), is(USER_PAGE_TYPE_CODE));
        assertThat(userPageType.getDescription(), is(USER_PAGE_TYPE_DESCRIPTION));
    }

    @Test
    public void getByCode_PersonPageType() {
        PageType personPageType = repository.getByCode(PERSON_PROFILE_PAGE_TYPE_CODE);
        assertThat(personPageType.getEntityId(), is(PERSON_PROFILE_PAGE_TYPE_ENTITY_ID));
        assertThat(personPageType.getCode(), is(PERSON_PROFILE_PAGE_TYPE_CODE));
        assertThat(personPageType.getDescription(), is(PERSON_PROFILE_PAGE_TYPE_DESCRIPTION));
    }

    @Test
    public void getByCode_SubPageType() {
        PageType subPageType = repository.getByCode(SUB_PAGE_PAGE_TYPE_CODE);
        assertThat(subPageType.getEntityId(), is(SUB_PAGE_PAGE_TYPE_ENTITY_ID));
        assertThat(subPageType.getCode(), is(SUB_PAGE_PAGE_TYPE_CODE));
        assertThat(subPageType.getDescription(), is(SUB_PAGE_PAGE_TYPE_DESCRIPTION));
    }
}
