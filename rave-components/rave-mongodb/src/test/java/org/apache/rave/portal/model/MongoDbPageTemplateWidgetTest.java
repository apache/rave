package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:28 PM
 */
public class MongoDbPageTemplateWidgetTest {
    private WidgetRepository widgetRepository;
    private MongoDbPageTemplateWidget templateWidget;

    @Before
    public void setup() {
        widgetRepository = createMock(WidgetRepository.class);
        templateWidget = new MongoDbPageTemplateWidget();
        templateWidget.setWidgetRepository(widgetRepository);
        templateWidget.setWidgetId((long) 123);
    }

    @Test
    public void getWidget_Null() {
        Widget widget = new WidgetImpl();
        expect(widgetRepository.get((long) 123)).andReturn(widget);
        replay(widgetRepository);

        assertThat(templateWidget.getWidget(), is(sameInstance(widget)));
    }
}
