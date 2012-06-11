package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaWidgetTagConverterTest {
  
    @Autowired 
    JpaWidgetTagConverter jpaWidgetTagConverter;
    
    @Test
    public void convert_valid_widgetTaq(){
        WidgetTag widgetTag = new WidgetTagImpl();
        widgetTag.setCreatedDate(new Date());
        widgetTag.setUser(new JpaUser());
        widgetTag.setWidgetId(3L);
        widgetTag.setTag(new JpaTag(1L, "news"));
        JpaWidgetTag jpaWidgetTag = jpaWidgetTagConverter.convert(widgetTag);
        assertNotNull(jpaWidgetTag);
        assertEquals(widgetTag.getCreatedDate(), jpaWidgetTag.getCreatedDate());
        assertSame(widgetTag.getTag(), jpaWidgetTag.getTag());
        assertSame(widgetTag.getUser(), jpaWidgetTag.getUser());
        assertEquals(widgetTag.getWidgetId(), jpaWidgetTag.getWidgetId());
    }

    @Test
    public void convert_valid_jpaWidgetTaq(){
        JpaWidgetTag widgetTag = new JpaWidgetTag();
        widgetTag.setCreatedDate(new Date());
        widgetTag.setUser(new JpaUser());
        widgetTag.setWidgetId(3L);
        widgetTag.setTag(new JpaTag(1L, "news"));
        JpaWidgetTag jpaWidgetTag = jpaWidgetTagConverter.convert(widgetTag);
        assertNotNull(jpaWidgetTag);
        assertSame(widgetTag, jpaWidgetTag);
    }
}
