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
    public void convertValid() {
        WidgetRating template = new WidgetRatingImpl();

        JpaWidgetRating jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaWidgetRating.class)));
        //TODO: Add coverage for all methods
    }

}
