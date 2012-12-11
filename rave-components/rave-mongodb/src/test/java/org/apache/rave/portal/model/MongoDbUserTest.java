package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:49 PM
 */
public class MongoDbUserTest {

    private MongoDbUser user;
    private PageLayoutRepository pageLayoutRepository;

    @Before
    public void setup(){
        user = new MongoDbUser();
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        user.setPageLayoutRepository(pageLayoutRepository);
    }

    @Test
    public void setAuthorities_Null(){
        ArrayList<String> array = new ArrayList<String>();
        array.add("string");
        user.setAuthorityCodes(array);
           user.setAuthorities(null);
        assertTrue(user.getAuthorityCodes().isEmpty());
    }

    @Test
    public void setAuthorities_Valid(){
        Authority auth = new AuthorityImpl();
        auth.setAuthority("auth");
        user.setAuthorities(Arrays.asList(auth));

        assertNotNull(user.getAuthorityCodes());
        assertThat(user.getAuthorityCodes().get(0), is(sameInstance(auth.getAuthority())));
    }

    @Test
    public void equals_Same(){
        assertTrue(user.equals(user));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(user.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        user.setId((long)123);
        MongoDbUser r = new MongoDbUser();
        assertFalse(user.equals(r));
        assertFalse(r.equals(user));

    }

    @Test
    public void equals_Valid(){
        user.setId((long)123);
        MongoDbUser r = new MongoDbUser();
        r.setId((long) 123);
        assertTrue(user.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(user.hashCode());
    }

    @Test
    public void getAuthorities_Valid(){
        ArrayList<String> array = new ArrayList<String>();
        array.add("string");
        user.setAuthorityCodes(array);

        Collection<GrantedAuthority> granted = user.getAuthorities();

        assertTrue(granted.size() == 1);
    }

    @Test
    public void removeAuthority_Valid(){
        Authority auth = new AuthorityImpl();
        auth.setAuthority("stinky");
        user.setAuthorityCodes(new ArrayList<String>());
        user.getAuthorityCodes().add("stinky");

        user.removeAuthority(auth);

        assertFalse(user.getAuthorityCodes().contains("stinky"));
    }

    @Test
    public void getDefaultPageLayout_Valid(){
        PageLayout layout = new PageLayoutImpl();
        user.setDefaultPageLayoutCode("dingus");
        expect(pageLayoutRepository.getByPageLayoutCode("dingus")).andReturn(layout);
        replay(pageLayoutRepository);

        assertThat(user.getDefaultPageLayout(), is(sameInstance(layout)));
    }

}
