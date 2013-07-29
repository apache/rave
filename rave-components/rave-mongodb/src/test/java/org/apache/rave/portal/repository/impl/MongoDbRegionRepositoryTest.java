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

import com.google.common.collect.Lists;
import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Test for MongoDb Region Repository
 */
public class MongoDbRegionRepositoryTest {

    private MongoPageOperations template;
    private MongoDbRegionRepository repo;

    @Before
    public void setUp(){
        template = createMock(MongoPageOperations.class);
        repo = new MongoDbRegionRepository();
        repo.setTemplate(template);

    }

    @Test
    public void get(){
        String id = "1111L";
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region region = new RegionImpl(id);
        regions.add(region);
        page.setRegions(regions);

        expect(template.findOne(new Query(Criteria.where("regions").elemMatch(Criteria.where("_id").is(id))))).andReturn(page);
        replay(template);

        Region result = repo.get(id);
        assertNotNull(result);
        assertThat(result, is(equalTo(region)));
        assertThat(result.getId(), equalTo(id));

    }

    @Test
    public void get_null(){
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region region = new RegionImpl("1111L");
        regions.add(region);
        page.setRegions(regions);

        expect(template.findOne(new Query(Criteria.where("regions").elemMatch(Criteria.where("_id").is("2222L"))))).andReturn(page);
        replay(template);

        Region result = repo.get("2222L");
        assertNull(result);

    }

    @Test
    public void save(){
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region item = new RegionImpl("1111L");
        regions.add(item);
        page.setRegions(regions);

        expect(template.findOne(new Query(Criteria.where("regions").elemMatch(Criteria.where("_id").is("1111L"))))).andReturn(page);
        expect(template.save(isA(Page.class))).andReturn(page);
        replay(template);

        Region result = repo.save(item);
        assertThat(result.getId(), is(equalTo("1111L")));
        assertThat(result, is(sameInstance(item)));

    }

    @Test
    public void save_null(){
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region item = new RegionImpl();
        item.setPage(page);
        regions.add(item);
        page.setRegions(regions);

        expect(template.get("1234L")).andReturn(page);
        expect(template.save(isA(Page.class))).andReturn(page);
        replay(template);

        Region result = repo.save(item);
        assertNull(result.getId());

    }

    @Test
     public void delete(){
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region item = new RegionImpl("1111L");
        regions.add(item);
        page.setRegions(regions);

        expect(template.findOne(new Query(Criteria.where("regions").elemMatch(Criteria.where("_id").is("1111L"))))).andReturn(page);
        expect(template.save(isA(Page.class))).andReturn(page);
        replay(template);

        repo.delete(item);

    }

    @Test (expected = IllegalStateException.class)
    public void delete_null(){
        Page page = new PageImpl("1234L");
        List<Region> regions = Lists.newArrayList();
        Region item = new RegionImpl();
        regions.add(item);
        page.setRegions(regions);

        repo.delete(item);
    }

    @Test
    public void getAll(){
        String id = "1111L";
        String id2 = "2222L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        regions.add(region);
        regions.add(region2);
        pages.add(page);
        page.setRegions(regions);


        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        List<Region> result = repo.getAll();
        assertNotNull(result);
        assertThat(result, is(equalTo(regions)));
        assertThat(result.size(), equalTo(2));
    }

    @Test
    public void getLimitedList(){
        String id = "1111L";
        String id2 = "2222L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        regions.add(region);
        regions.add(region2);
        pages.add(page);
        page.setRegions(regions);

        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        List<Region> result = repo.getLimitedList(1, 1);
        assertNotNull(result);
        assertThat(result.size(), equalTo(1));
    }


    @Test
    public void getCount(){
        String id = "1111L";
        String id2 = "2222L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        regions.add(region);
        regions.add(region2);
        pages.add(page);
        page.setRegions(regions);

        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        int count = repo.getCountAll();
        assertNotNull(count);
        assertThat(count, equalTo(2));
    }
}
