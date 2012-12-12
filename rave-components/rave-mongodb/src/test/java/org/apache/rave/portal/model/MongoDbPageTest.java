package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 2:37 PM
 */
public class MongoDbPageTest {
    private MongoDbPage page;
    private UserRepository repository;

    @Before
    public void setup() {
        page = new MongoDbPage();
        repository = createMock(UserRepository.class);
        page.setUserRepository(repository);
    }

    @Test
    public void getOwner_Null_Owner(){
        User user = new UserImpl();
        page.setOwnerId((long)123);
        expect(repository.get((long) 123)).andReturn(user);
        replay(repository);

        assertThat(page.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void getOwner_Owner_Set(){
        User user = new UserImpl();
        page.setOwner(user);

        assertThat(page.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void equals_Same(){
        assertTrue(page.equals(page));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(page.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        page.setId((long)123);
        Widget r = new MongoDbWidget();
        assertFalse(page.equals(r));
        assertFalse(r.equals(page));

    }

    @Test
    public void equals_Valid(){
        page.setId((long)123);
        MongoDbPage r = new MongoDbPage();
        r.setId((long) 123);
        assertTrue(page.equals(r));
    }

    @Test
    public void equals_Valid_Null_Id(){
        page.setId(null);
        MongoDbPage r = new MongoDbPage();
        r.setId(null);
        assertTrue(page.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(page.hashCode());
    }

}
