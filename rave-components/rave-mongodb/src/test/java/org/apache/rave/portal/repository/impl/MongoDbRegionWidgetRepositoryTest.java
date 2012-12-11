package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.MongoPageOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
        assertThat((Class<MongoDbRegionWidget>) widgetRepository.getType(), is(equalTo(MongoDbRegionWidget.class)));
    }

    @Test
    public void get_Valid() {
        long id = 123;
        Page found = new MongoDbPage();
        Region region = new RegionImpl();
        found.setRegions(Arrays.asList(region));
        RegionWidget widget = new RegionWidgetImpl();
        region.setRegionWidgets(Arrays.asList(widget));
        widget.setId((long) 123);
        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))))).andReturn(found);
        replay(template);

        assertThat(widget, is(sameInstance(widgetRepository.get(id))));
    }

    @Test
    public void get_Null() {
        long id = 321;
        Page found = new MongoDbPage();
        found.setRegions(new ArrayList<Region>());
        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))))).andReturn(found);
        replay(template);
        assertNull(widgetRepository.get(id));
    }

    @Test
    public void save_Id_Valid() {
        RegionWidget widget = new RegionWidgetImpl();
        RegionWidget replaced = new RegionWidgetImpl();

        long id = 123;
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

        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))))).andReturn(parent);
        expect(template.save(parent)).andReturn(parent);
        replay(template);

        RegionWidget savedWidget = widgetRepository.save(widget);

        assertTrue(region.getRegionWidgets().contains(widget));
        assertFalse(region.getRegionWidgets().contains(replaced));
        assertThat(savedWidget, is(sameInstance(widget)));
    }

    @Test
    public void save_Id_Valid_Page_Null(){
        RegionWidget item = new RegionWidgetImpl();
        long id = 123;
        item.setId(id);
        Page page = new PageImpl();
        page.setRegions(new ArrayList<Region>());

        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))))).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Widget does not exist in parent page regions");

        widgetRepository.save(item);
    }

    @Test
    public void save_Id_Null_Page_Valid_Regions_Valid() {
        RegionWidget widget = new RegionWidgetImpl();

        Region region = new RegionImpl();
        Page page = new PageImpl();
        long id = 321;
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
        assertThat(returned, is(sameInstance(widget)));
    }

    @Test
    public void save_Id_Null_Page_Null_Region_Null() {
        RegionWidget widget = new RegionWidgetImpl();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Unable to find page for region");

        widgetRepository.save(widget);
    }

    @Test
    public void save_Id_Null_Page_Valid_Region_Null() {
        RegionWidget item = new RegionWidgetImpl();
        Region region = new RegionImpl();
        item.setRegion(region);
        long id = 123;
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
        RegionWidget widget = new RegionWidgetImpl();
        widget.setId((long) 123);
        Page found = new PageImpl();
        Region region = new RegionImpl();
        List<RegionWidget> regionWidgets = new ArrayList<RegionWidget>();
        regionWidgets.add(widget);
        List<Region> regions = new ArrayList<Region>();
        regions.add(region);
        found.setRegions(regions);
        region.setRegionWidgets(regionWidgets);

        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(123)))))).andReturn(found);
        expect(template.save(found)).andReturn(null);
        replay(template);

        widgetRepository.delete(widget);

        assertFalse(region.getRegionWidgets().contains(widget));
        verify(template);
    }

    @Test
    public void delete_Null(){
        RegionWidget item = new RegionWidgetImpl();
        long id = 123;
        item.setId(id);
        Page page = new PageImpl();
        page.setRegions(new ArrayList<Region>());

        expect(template.findOne(new Query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("_id").is(id)))))).andReturn(page);
        replay(template);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Widget does not exist in parent page regions");

        widgetRepository.delete(item);
    }

}
