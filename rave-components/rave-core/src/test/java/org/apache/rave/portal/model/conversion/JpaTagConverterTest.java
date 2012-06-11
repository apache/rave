package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.portal.model.TagImpl;
import org.apache.rave.portal.model.WidgetTag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaTagConverterTest {
    
    @Autowired
    JpaTagConverter jpaTagConverter;

    @Test
    public void convert_valid_tagImpl(){
        TagImpl tag = new TagImpl("blazer", new ArrayList<WidgetTag>());
        JpaTag jpaTag = jpaTagConverter.convert(tag);
        assertNotNull(jpaTag);
        assertEquals(tag.getKeyword(), jpaTag.getKeyword()); 
        assertEquals(tag.getWidgets().size(), jpaTag.getWidgets().size());
    }


    @Test
    public void convert_valid_jpaTag(){
        JpaTag tag = new JpaTag();
        tag.setKeyword("blazer");
        tag.setWidgets(new ArrayList<WidgetTag>());
        tag.setEntityId(387L);
        JpaTag jpaTag = jpaTagConverter.convert(tag);
        assertNotNull(jpaTag);
        assertSame(tag, jpaTag);
    }
    
}
