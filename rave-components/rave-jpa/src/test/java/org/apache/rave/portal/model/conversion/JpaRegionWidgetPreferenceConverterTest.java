package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaRegionWidgetPreference;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.model.impl.RegionWidgetPreferenceImpl;
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
public class JpaRegionWidgetPreferenceConverterTest {

    @Autowired
    JpaRegionWidgetPreferenceConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaRegionWidgetPreference template = new JpaRegionWidgetPreference();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        RegionWidgetPreference template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        RegionWidgetPreference template = new RegionWidgetPreferenceImpl();
        template.setName("TEST_A");
        template.setRegionWidgetId(42L);
        template.setValue("TEST_B");

        JpaRegionWidgetPreference jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaRegionWidgetPreference.class)));
        assertThat(jpaTemplate.getName(), is(equalTo(template.getName())));
        assertThat(jpaTemplate.getRegionWidgetId(), is(equalTo(template.getRegionWidgetId())));
        assertThat(jpaTemplate.getValue(), is(equalTo(template.getValue())));
    }

}
