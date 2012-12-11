package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.CategoryRepository;
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
 * Time: 1:49 PM
 */
public class MongoDbWidgetTest {

    private MongoDbWidget widget;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Before
    public void setup(){
        widget = new MongoDbWidget();
        categoryRepository = createMock(CategoryRepository.class);
        userRepository = createMock(UserRepository.class);
        widget.setCategoryRepository(categoryRepository);
        widget.setUserRepository(userRepository);
    }

    @Test
    public void getOwner_Null(){
        widget.setOwnerId((long)123);
        User user = new UserImpl();
        expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);
        assertThat(widget.getOwner(), is(sameInstance(user)));
    }

    @Test
    public void getCategories_Valid(){
        assertNotNull(widget.getCategories());
    }

    @Test
    public void equals_Same(){
        assertTrue(widget.equals(widget));
    }

    @Test
    public void equals_Not_Same_Instance(){
        Object o = new Object();
        assertFalse(widget.equals(o));

    }

    @Test
    public void equals_Null_Id(){
        widget.setId((long)123);
        Widget r = new MongoDbWidget();
        assertFalse(widget.equals(r));
        assertFalse(r.equals(widget));

    }

    @Test
    public void equals_Valid(){
        widget.setId((long)123);
        Widget r = new MongoDbWidget();
        ((MongoDbWidget)r).setId((long) 123);
        assertTrue(widget.equals(r));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(widget.hashCode());
    }

}
