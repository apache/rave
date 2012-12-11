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
 * Time: 2:02 PM
 */
public class MongoDbWidgetTagTest {
    private UserRepository userRepository;
    private MongoDbWidgetTag widgetTag;

    @Before
    public void setup(){
        userRepository = createMock(UserRepository.class);
        widgetTag = new MongoDbWidgetTag();
        widgetTag.setUserRepository(userRepository);
    }

    @Test
    public void getUser_Null_User(){
        User user = new UserImpl();
        widgetTag.setUserId((long)123);
         expect(userRepository.get((long)123)).andReturn(user);
        replay(userRepository);

        assertThat(widgetTag.getUser(), is(sameInstance(user)));
    }
}
