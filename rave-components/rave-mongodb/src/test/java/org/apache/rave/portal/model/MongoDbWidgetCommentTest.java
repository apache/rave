package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 3:40 PM
 */
public class MongoDbWidgetCommentTest {
    private MongoDbWidgetComment comment;
    private UserRepository userRepository;

    @Before
    public void setup(){
        comment = new MongoDbWidgetComment();
        userRepository = createMock(UserRepository.class);
        comment.setUserRepository(userRepository);
    }

    @Test
    public void getUser_Valid(){
        comment.setUserId((long)123);
        User user = new UserImpl();
        expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);

        assertThat(comment.getUser(), is(sameInstance(user)));
    }
}
