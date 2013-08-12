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
import org.apache.rave.portal.model.MongoDbPage;
import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * User: DSULLIVAN
 * Date: 12/6/12
 * Time: 9:17 AM
 */
public class MongoDbRegionWidgetRepositoryTest {
    private MongoDbRegionWidgetRepository widgetRepository;
    private MongoPageOperations template;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        widgetRepository = new MongoDbRegionWidgetRepository();
        template = createMock(MongoPageOperations.class);
        widgetRepository.setTemplate(template);
    }

    @Test
    public void getType_Valid() {
        assertThat((Class)widgetRepository.getType(), is(equalTo((Class)RegionWidgetImpl.class)));
    }

    @Test
    public void get_DifferentId() {
        String id = "234";
        Page page = new PageImpl();
        Region region = new RegionImpl();
        RegionWidgetImpl widget = new RegionWidgetImpl();
        widget.setId("123");
        page.setRegions(Arrays.asList(region));
        region.setPage(page);
        region.setRegionWidgets(Arrays.<RegionWidget>asList(widget));
        widget.setRegion(region);

        expect(template.findOne(getQuery(id))).andReturn(page);
        replay(template);
        RegionWidget result = widgetRepository.get(id);
        assertNull(result);

    }

    @Test
    public void get_Valid() {
        String id = "123";
        Page found = new MongoDbPage();
        Region region = new RegionImpl();
        found.setRegions(Arrays.asList(region));
        RegionWidgetImpl widget = new RegionWidgetImpl();
        region.setRegionWidgets(Arrays.<RegionWidget>asList(widget));
        widget.setId("123");
        expect(template.findOne(getQuery(id))).andReturn(found);
        replay(template);

        assertThat(widget, is(sameInstance(widgetRepository.get(id))));
    }

    @Test
    public void get_Null() {
        String id = "321";
        Page found = new MongoDbPage();
        found.setRegions(new ArrayList<Region>());
        expect(template.findOne(getQuery(id))).andReturn(found);
        replay(template);
        assertNull(widgetRepository.get(id));
    }

    @Test
    public void save_Id_Valid() {
        RegionWidgetImpl widget = new RegionWidgetImpl();
        RegionWidgetImpl replaced = new RegionWidgetImpl();

        String id = "123";
        widget.setId(id);
        replaced.setId(id);
        replaced.setCollapsed(true);
        Page parent = new PageImpl();
        Region region = new RegionImpl();
        List<Region> regions = new ArrayList<Region>();
        regions.add(region);
        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();
        regionWidgets.add(replaced);
        parent.setRegions(regions);
        region.setRegionWidgets(regionWidgets);

        expect(template.findOne(getQuery(id))).andReturn(parent);
        expect(template.save(parent)).andReturn(parent);
        replay(template);

        RegionWidget savedWidget = widgetRepository.save(widget);

        assertTrue(region.getRegionWidgets().contains(widget));
        assertFalse(region.getRegionWidgets().contains(replaced));
        assertThat(savedWidget, is(sameInstance((RegionWidget)widget)));
    }

    @Test
    public void save_Id_Valid_Page_Null() {
        RegionWidgetImpl item = new RegionWidgetImpl();
        String id = "123";
        item.setId(id);
        Page page = new PageImpl();
        page.setRegions(new ArrayList<Region>());

        expect(template.findOne(getQuery(id))).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Widget does not exist in parent page regions");

        widgetRepository.save(item);
    }

    @Test
    public void save_Id_Null_Page_Valid_Regions_Valid() {
        RegionWidgetImpl widget = new RegionWidgetImpl();

        RegionImpl region = new RegionImpl();
        Page page = new PageImpl();
        String id = "321";
        page.setId(id);
        List<Region> regions = new ArrayList<Region>();
        regions.add(region);
        page.setRegions(regions);
        region.setPage(page);
        region.setId(id);
        widget.setRegion(region);
        List<RegionWidget> widgets = new ArrayList<RegionWidget>();
        widgets.add(widget);
        region.setRegionWidgets(widgets);

        expect(template.get(id)).andReturn(page);
        expect(template.save(page)).andReturn(page);
        replay(template);

        RegionWidget returned = widgetRepository.save(widget);
        assertThat(returned, is(sameInstance((RegionWidget)widget)));
    }

    @Test
    public void save_Id_Null_Page_Valid_Regions_Valid_Diff_Id() {
        RegionWidget item = new RegionWidgetImpl();
        RegionImpl region = new RegionImpl();
        item.setRegion(region);
        Page item_Page = new PageImpl();
        region.setPage(item_Page);
        item_Page.setId("3333");
        region.setId("2222");

        Page page = new PageImpl();
        ArrayList<Region> regions = new ArrayList<Region>();
        regions.add(new RegionImpl());
        page.setRegions(regions);
        expect(template.get(item.getRegion().getPage().getId())).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find parent for page");

        widgetRepository.save(item);
    }

    @Test
    public void save_Id_Null_Page_Null_Region_Null() {
        RegionWidget widget = new RegionWidgetImpl();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find page for region");

        widgetRepository.save(widget);
    }

    @Test
    public void save_Id_Null_Page_Null_Region_Valid_Page_Null() {
        RegionWidget widget = new RegionWidgetImpl();
        Region region = new RegionImpl();
        widget.setRegion(region);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find page for region");

        widgetRepository.save(widget);
    }

    @Test
    public void save_Id_Null_Page_Null_Region_Valid_Page_Valid_Id_Null() {
        RegionWidget widget = new RegionWidgetImpl();
        Region region = new RegionImpl();
        widget.setRegion(region);
        Page page = new PageImpl();
        region.setPage(page);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find page for region");

        widgetRepository.save(widget);
    }

    @Test
    public void save_Id_Null_Page_Valid_Region_Null() {
        RegionWidget item = new RegionWidgetImpl();
        Region region = new RegionImpl();
        item.setRegion(region);
        String id = "123";
        Page page = new PageImpl();
        region.setPage(page);
        page.setId(id);
        page.setRegions(new ArrayList<Region>());

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find parent for page");

        expect(template.get(id)).andReturn(page);
        replay(template);

        widgetRepository.save(item);
    }

    @Test
    public void delete_Valid() {
        RegionWidgetImpl widget = new RegionWidgetImpl();
        String id = "123";
        widget.setId(id);
        Page found = new PageImpl();
        Region region = new RegionImpl();
        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();
        regionWidgets.add(widget);
        List<Region> regions = new ArrayList<Region>();
        regions.add(region);
        found.setRegions(regions);
        region.setRegionWidgets(regionWidgets);

        expect(template.findOne(getQuery(id))).andReturn(found);
        expect(template.save(found)).andReturn(null);
        replay(template);

        widgetRepository.delete(widget);

        assertFalse(region.getRegionWidgets().contains(widget));
        verify(template);
    }

    @Test
    public void delete_Null() {
        RegionWidgetImpl item = new RegionWidgetImpl();
        String id = "123";
        item.setId(id);
        Page page = new PageImpl();
        page.setRegions(new ArrayList<Region>());

        expect(template.findOne(getQuery(id))).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Widget does not exist in parent page regions");

        widgetRepository.delete(item);
    }

    private Query getQuery(String id) {
        return new Query(new Criteria().orOperator(where("subPages").elemMatch(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))),where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))));
    }

    @Test
    public void delete_DifferentId() {
        RegionWidgetImpl widget = new RegionWidgetImpl();
        Region region = new RegionImpl();
        Page page = new PageImpl();
        page.setRegions(Arrays.asList(region));
        region.setRegionWidgets(Arrays.<RegionWidget>asList(widget));
        widget.setId("345345");

        RegionWidgetImpl item = new RegionWidgetImpl();
        String id = "4344";
        item.setId(id);

        expect(template.findOne(getQuery(id))).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Widget does not exist in parent page regions");

        widgetRepository.delete(item);

    }

    @Test
    public void getAll(){
        String id = "1111L";
        String id2 = "2222L";
        String regionWidgetId1 = "9876L";
        String regionWidgetId2 = "6543L";
        String regionWidgetId3 = "2874L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        List<RegionWidget> regionWidgets = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        RegionWidget rw1 = new RegionWidgetImpl(regionWidgetId1);
        RegionWidget rw2 = new RegionWidgetImpl(regionWidgetId2);
        RegionWidget rw3 = new RegionWidgetImpl(regionWidgetId3);
        regions.add(region);
        regions.add(region2);
        regionWidgets.add(rw1);
        regionWidgets.add(rw2);
        regionWidgets.add(rw3);
        region.setRegionWidgets(regionWidgets);
        pages.add(page);
        page.setRegions(regions);


        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        List<RegionWidget> result = widgetRepository.getAll();
        assertNotNull(result);
        assertThat(result, is(equalTo(regionWidgets)));
        assertThat(result.size(), equalTo(3));
    }

    @Test
    public void getLimitedList(){
        String id = "1111L";
        String id2 = "2222L";
        String regionWidgetId1 = "9876L";
        String regionWidgetId2 = "6543L";
        String regionWidgetId3 = "2874L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        List<RegionWidget> regionWidgets = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        RegionWidget rw1 = new RegionWidgetImpl(regionWidgetId1);
        RegionWidget rw2 = new RegionWidgetImpl(regionWidgetId2);
        RegionWidget rw3 = new RegionWidgetImpl(regionWidgetId3);
        regions.add(region);
        regions.add(region2);
        regionWidgets.add(rw1);
        regionWidgets.add(rw2);
        regionWidgets.add(rw3);
        region.setRegionWidgets(regionWidgets);
        pages.add(page);
        page.setRegions(regions);


        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        List<RegionWidget> result = widgetRepository.getLimitedList(1,2);
        assertNotNull(result);
        assertThat(result, is(equalTo(regionWidgets.subList(1,3))));
        assertThat(result.size(), equalTo(2));
    }


    @Test
    public void getCount(){
        String id = "1111L";
        String id2 = "2222L";
        String regionWidgetId1 = "9876L";
        String regionWidgetId2 = "6543L";
        String regionWidgetId3 = "2874L";
        Page page = new PageImpl("1234L");
        List<Page> pages = Lists.newArrayList();
        List<Region> regions = Lists.newArrayList();
        List<RegionWidget> regionWidgets = Lists.newArrayList();
        Region region = new RegionImpl(id);
        Region region2 = new RegionImpl(id2);
        RegionWidget rw1 = new RegionWidgetImpl(regionWidgetId1);
        RegionWidget rw2 = new RegionWidgetImpl(regionWidgetId2);
        RegionWidget rw3 = new RegionWidgetImpl(regionWidgetId3);
        regions.add(region);
        regions.add(region2);
        regionWidgets.add(rw1);
        regionWidgets.add(rw2);
        regionWidgets.add(rw3);
        region.setRegionWidgets(regionWidgets);
        pages.add(page);
        page.setRegions(regions);


        Query q = new Query();
        expect(template.find(q)).andReturn(pages);
        replay(template);

        int result = widgetRepository.getCountAll();
        assertNotNull(result);
        assertThat(result, equalTo(3));
    }

}
