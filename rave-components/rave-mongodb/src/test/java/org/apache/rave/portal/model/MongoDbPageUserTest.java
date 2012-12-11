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
 * Time: 3:31 PM
 */
public class MongoDbPageUserTest {
    private MongoDbPageUser pageUser = new MongoDbPageUser();
    private UserRepository userRepository;

    @Before
    public void setup(){
         pageUser = new MongoDbPageUser();
        userRepository = createMock(UserRepository.class);
        pageUser.setUserRepository(userRepository);
    }

    @Test
    public void getUser_Valid(){
        pageUser.setUserId((long)123);
        User user = new UserImpl();
        expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);

        assertThat(pageUser.getUser(), is(sameInstance(user)));
    }

    @Test
    public void equals_Same(){
        assertTrue(pageUser.equals(pageUser));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(pageUser.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        pageUser.setId((long)123);
        MongoDbPageUser r = new MongoDbPageUser();
        assertFalse(pageUser.equals(r));
        assertFalse(r.equals(pageUser));

    }

    @Test
    public void equals_Valid(){
        pageUser.setId((long)123);
        MongoDbPageUser r = new MongoDbPageUser();
        r.setId((long) 123);
        assertTrue(pageUser.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(pageUser.hashCode());
    }
}
