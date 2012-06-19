package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-dataContext.xml"})
public class JpaPersonConverterTest {

    @Autowired
    JpaPersonConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaPerson template = new JpaPerson();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        Person template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        Person template = new PersonImpl();
        template.setUsername("TEST_A");
        template.setEmail("TEST_B");
        template.setDisplayName("TEST_C");
        template.setAdditionalName("TEST_D");
        template.setFamilyName("TEST_E");
        template.setGivenName("TEST_F");
        template.setHonorificPrefix("TEST_G");
        template.setHonorificSuffix("TEST_H");
        template.setPreferredName("TEST_I");
        template.setAboutMe("TEST_J");
        template.setStatus("TEST_K");
        template.setAddresses(new ArrayList<Address>());
        template.setOrganizations(new ArrayList<Organization>());
        template.setProperties(new ArrayList<PersonProperty>());
        template.setFriends(new ArrayList<Person>());

        JpaPerson jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaPerson.class)));
        assertThat(jpaTemplate.getUsername(), is(equalTo(template.getUsername())));
        assertThat(jpaTemplate.getEmail(), is(equalTo(template.getEmail())));
        assertThat(jpaTemplate.getDisplayName(), is(equalTo(template.getDisplayName())));
        assertThat(jpaTemplate.getUsername(), is(equalTo(template.getUsername())));
        assertThat(jpaTemplate.getFamilyName(), is(equalTo(template.getFamilyName())));
        assertThat(jpaTemplate.getGivenName(), is(equalTo(template.getGivenName())));
        assertThat(jpaTemplate.getHonorificPrefix(), is(equalTo(template.getHonorificPrefix())));
        assertThat(jpaTemplate.getHonorificSuffix(), is(equalTo(template.getHonorificSuffix())));
        assertThat(jpaTemplate.getPreferredName(), is(equalTo(template.getPreferredName())));
        assertThat(jpaTemplate.getAboutMe(), is(equalTo(template.getAboutMe())));
        assertThat(jpaTemplate.getStatus(), is(equalTo(template.getStatus())));
        assertThat(jpaTemplate.getAddresses(), is(equalTo(template.getAddresses())));
        assertThat(jpaTemplate.getOrganizations(), is(equalTo(template.getOrganizations())));
        assertThat(jpaTemplate.getProperties(), is(equalTo(template.getProperties())));
        assertThat(jpaTemplate.getFriends(), is(equalTo(template.getFriends())));
    }

}
