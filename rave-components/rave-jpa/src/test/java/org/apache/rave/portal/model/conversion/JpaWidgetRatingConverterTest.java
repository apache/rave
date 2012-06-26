package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-dataContext.xml"})
public class JpaWidgetRatingConverterTest {

    @Autowired
    JpaWidgetRatingConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaWidgetRating template = new JpaWidgetRating();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        WidgetRating template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        WidgetRating template = new WidgetRatingImpl();
        template.setScore(1);
        template.setUserId(42L);
        template.setWidgetId(24L);
        
        JpaWidgetRating jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaWidgetRating.class)));
        assertThat(jpaTemplate.getScore(), is(equalTo(template.getScore())));
        assertThat(jpaTemplate.getUserId(), is(equalTo(template.getUserId())));
        assertThat(jpaTemplate.getWidgetId(), is(equalTo(template.getWidgetId())));
    }

}
