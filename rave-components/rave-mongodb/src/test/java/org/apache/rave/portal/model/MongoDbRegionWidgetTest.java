package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:38 PM
 */
public class MongoDbRegionWidgetTest {
    private MongoDbRegionWidget regionWidget;
    private WidgetRepository widgetRepository;

    @Before
    public void setup(){
        regionWidget = new MongoDbRegionWidget();
        widgetRepository = createMock(WidgetRepository.class);
        regionWidget.setWidgetRepository(widgetRepository);
    }

    @Test
    public void getWidget_Widget_Null(){
        regionWidget.setWidgetId((long)123);
        Widget widget = new WidgetImpl();
        expect(widgetRepository.get((long)123)).andReturn(widget);
        replay(widgetRepository);

        assertThat(widget, is(sameInstance(regionWidget.getWidget())));

    }

    @Test
    public void equals_Same(){
        assertTrue(regionWidget.equals(regionWidget));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(regionWidget.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        regionWidget.setId((long)123);
        RegionWidget r = new MongoDbRegionWidget();
        assertFalse(regionWidget.equals(r));
        assertFalse(r.equals(regionWidget));

    }

    @Test
    public void equals_Valid(){
        regionWidget.setId((long)123);
        RegionWidget r = new MongoDbRegionWidget();
        r.setId((long)123);
        assertTrue(regionWidget.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(regionWidget.hashCode());
    }
}
