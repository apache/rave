package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaAuthority;
import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.model.impl.UserImpl;
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
public class JpaAuthorityConverterTest {

    @Autowired
    JpaAuthorityConverter converter;

    @Before
    public void setup() {

    }

    @Test
    public void testNoConversion() {
        JpaAuthority template = new JpaAuthority();
        assertThat(converter.convert(template), is(sameInstance(template)));
    }

    @Test
    public void nullConversion() {
        Authority template = null;
        assertThat(converter.convert(template), is(nullValue()));
    }


    @Test
    public void convertValid() {
        Authority template = new AuthorityImpl();
        template.setAuthority("FOO");
        template.setDefaultForNewUser(true);
        template.addUser(new UserImpl(42L));

        JpaAuthority jpaTemplate = converter.convert(template);

        assertThat(jpaTemplate, is(not(sameInstance(template))));
        assertThat(jpaTemplate, is(instanceOf(JpaAuthority.class)));
        assertThat(jpaTemplate.getAuthority(), is(equalTo(template.getAuthority())));
        assertThat(jpaTemplate.isDefaultForNewUser(), is(equalTo(template.isDefaultForNewUser())));
        assertThat(jpaTemplate.getUsers().iterator().next().getId(), is(equalTo(template.getUsers().iterator().next().getId())));

    }

}
