/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.PageLayout;
import org.apache.rave.portal.model.MongoDbPageLayout;
import org.apache.rave.portal.repository.util.CollectionNames;
import org.apache.rave.util.CollectionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * User: DSULLIVAN
 * Date: 12/5/12
 * Time: 11:17 AM
 */
public class MongoDbPageLayoutRepositoryTest {
    private MongoDbPageLayoutRepository pageLayoutRepository;
    private MongoOperations template;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        pageLayoutRepository = new MongoDbPageLayoutRepository();
        template = createMock(MongoOperations.class);
        pageLayoutRepository.setTemplate(template);
    }

    @Test
    public void getByPageLayoutCode_Valid() {
        String codename = "codename";
        MongoDbPageLayout found = new MongoDbPageLayout();
        expect(template.findOne(new Query(where("code").is(codename)), MongoDbPageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn(found);
        replay(template);

        assertThat((MongoDbPageLayout) pageLayoutRepository.getByPageLayoutCode(codename), is(sameInstance(found)));
    }

    @Test
    public void getAll_Valid() {
        List<MongoDbPageLayout> found = new ArrayList<MongoDbPageLayout>();
        expect(template.findAll(pageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn(found);
        replay(template);

        assertThat(pageLayoutRepository.getAll(), is(sameInstance(CollectionUtils.<PageLayout>toBaseTypedList(found))));
    }

    @Test
    public void getAllUserSelectable_Valid() {
        List<MongoDbPageLayout> userSelectable = new ArrayList<MongoDbPageLayout>();
        expect(template.find(new Query(where("userSelectable").is(true)), pageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn(userSelectable);
        replay(template);

        List<PageLayout> returned = pageLayoutRepository.getAllUserSelectable();

        assertThat(returned, is(sameInstance(CollectionUtils.<PageLayout>toBaseTypedList(userSelectable))));
    }

    @Test
    public void getType_Valid() {
        assertThat((Class<MongoDbPageLayout>) pageLayoutRepository.getType(), is(equalTo(MongoDbPageLayout.class)));
    }

    @Test
    public void get_Valid() {
        String id = "123";

        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("No use for an id");
        pageLayoutRepository.get(id);
    }

    @Test
    public void save_Valid() {
        PageLayout item1 = new MongoDbPageLayout();
        item1.setCode("blah1");
        expect(template.findOne(new Query(where("code").is(item1.getCode())), pageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn(null);
        template.save(isA(MongoDbPageLayout.class), eq(CollectionNames.PAGE_LAYOUT_COLLECTION));
        expectLastCall();
        replay(template);

        PageLayout saved = pageLayoutRepository.save(item1);

        assertThat(saved, is(instanceOf(MongoDbPageLayout.class)));
        assertThat(saved.getCode(), is(sameInstance(item1.getCode())));
        assertThat(saved.getNumberOfRegions(), is(sameInstance(item1.getNumberOfRegions())));
        assertThat(saved.getRenderSequence(), is(sameInstance(item1.getRenderSequence())));
        assertThat(saved.isUserSelectable(), is(sameInstance(item1.isUserSelectable())));
    }

    @Test
    public void save_Null(){
        PageLayout item1 = new MongoDbPageLayout();
        item1.setCode("blah1");
        item1.setNumberOfRegions((long)123);
        item1.setRenderSequence((long)432);
        item1.setUserSelectable(true);
        MongoDbPageLayout toSave = new MongoDbPageLayout();
        expect(template.findOne(new Query(where("code").is(item1.getCode())), pageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn(toSave);
        template.save(isA(MongoDbPageLayout.class), eq(CollectionNames.PAGE_LAYOUT_COLLECTION));
        expectLastCall();
        replay(template);

        PageLayout saved = pageLayoutRepository.save(item1);

        assertThat(saved.getCode(), is(sameInstance(item1.getCode())));
        assertThat(saved.getNumberOfRegions(), is(sameInstance(item1.getNumberOfRegions())));
        assertThat(saved.getRenderSequence(), is(sameInstance(item1.getRenderSequence())));
        assertThat(saved.isUserSelectable(), is(sameInstance(item1.isUserSelectable())));
    }

    @Test
    public void delete_Valid(){
        PageLayout item = new MongoDbPageLayout();
        item.setCode("123");
        expect(template.findOne(new Query(where("code").is(item.getCode())), pageLayoutRepository.CLASS, CollectionNames.PAGE_LAYOUT_COLLECTION)).andReturn((MongoDbPageLayout)item);

          template.remove(item);
        expectLastCall();
        replay(template);

        pageLayoutRepository.delete(item);
        verify(template);
    }
}
