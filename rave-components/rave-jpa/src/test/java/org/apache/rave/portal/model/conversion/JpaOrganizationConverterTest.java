package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaOrganization;
import org.apache.rave.portal.model.Organization;
import org.apache.rave.portal.model.impl.OrganizationImpl;
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
public class JpaOrganizationConverterTest {

    @Autowired
    JpaOrganizationConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaOrganization template = new JpaOrganization();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        Organization template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        Organization template = new OrganizationImpl();

        JpaOrganization jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaOrganization.class)));
        //TODO: Add coverage for all methods
    }

}
