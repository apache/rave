package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaPortalPreference;
import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.model.impl.PortalPreferenceImpl;
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
public class JpaPortalPreferenceConverterTest {

    @Autowired
    JpaPortalPreferenceConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPortalPreference template = new JpaPortalPreference();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        PortalPreference template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        PortalPreference template = new PortalPreferenceImpl();
        template.setKey("KEY");
        template.setValue("VALUE");

        JpaPortalPreference jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPortalPreference.class)));
        assertThat(jpaTemplate.getKey(), is(equalTo(template.getKey())));
        assertThat(jpaTemplate.getValue(), is(equalTo(template.getValue())));
    }

}
