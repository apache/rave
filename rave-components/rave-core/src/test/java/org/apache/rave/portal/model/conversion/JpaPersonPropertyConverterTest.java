package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaPersonProperty;
import org.apache.rave.portal.model.PersonProperty;
import org.apache.rave.portal.model.impl.PersonPropertyImpl;
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
public class JpaPersonPropertyConverterTest {

    @Autowired
    JpaPersonPropertyConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPersonProperty template = new JpaPersonProperty();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void convertValid() {
        PersonProperty template = new PersonPropertyImpl();

        JpaPersonProperty jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPersonProperty.class)));
        //TODO: Add coverage for all methods
    }

}
