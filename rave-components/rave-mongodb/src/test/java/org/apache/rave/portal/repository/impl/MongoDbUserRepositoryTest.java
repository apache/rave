package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.repository.MongoUserOperations;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: DSULLIVAN
 * Date: 12/6/12
 * Time: 2:13 PM
 */
public class MongoDbUserRepositoryTest {
    private MongoDbUserRepository userRepository;
    private MongoUserOperations template;
    private MongoPageTemplate pageTemplate;

    @Before
    public void setup() {
        userRepository = new MongoDbUserRepository();
        template = createMock(MongoUserOperations.class);
        pageTemplate = createMock(MongoPageTemplate.class);
        userRepository.setTemplate(template);
        userRepository.setPageTemplate(pageTemplate);
    }

    @Test
    public void getByUsername_Valid() {
        String username = "username";
        User found = new UserImpl();
        expect(template.findOne(query(where("username").is(username)))).andReturn(found);
        replay(template);
        assertThat(found, is(sameInstance(userRepository.getByUsername(username))));
    }

    @Test
    public void getByUserEmail_Valid(){
        String userEmail = "userEmail";
        User found = new UserImpl();
        expect(template.findOne(query(where("email").is(userEmail)))).andReturn(found);
        replay(template);
        assertThat(found, is(sameInstance(userRepository.getByUserEmail(userEmail))));
    }

    @Test
    public void getByOpenId_Valid(){
        String openId = "openId";
        User found = new UserImpl();
        expect(template.findOne(query(where("openId").is(openId)))).andReturn(found);
        replay(template);
        assertThat(found, is(sameInstance(userRepository.getByOpenId(openId))));
    }

    @Test
    public void getLimitedList_Valid(){
        int offset = 234;
        int pageSize = 123;
        List<User> found = new ArrayList<User>();
        expect(template.find(isA(Query.class))).andReturn(found);
        replay(template);
        assertThat(found, is(sameInstance(userRepository.getLimitedList(offset, pageSize))));
    }

    @Test
    public void getCountAll_Valid(){
        long doubleOseven = 007;
        expect(template.count(new Query())).andReturn(doubleOseven);
        replay(template);
        assertThat((int)doubleOseven, is(sameInstance(userRepository.getCountAll())));
    }

    @Test
    public void findByUsernameOrEmail_Valid(){
        String searchTerm = "searchTerm";
        int offset = 5;
        int pageSize = 3;
        List<User> found = new ArrayList<User>();
        expect(template.find(isA(Query.class))).andReturn(found);
        replay(template);

        assertThat(found, is(sameInstance(userRepository.findByUsernameOrEmail(searchTerm,offset,pageSize))));
    }

    @Test
    public void getCountByUsernameOrEmail_Valid(){
        String searchTerm = "searchTerm";
        long count = 70;
        expect(template.count(isA(Query.class))).andReturn(count);
        replay(template);

        assertThat((int)count, is(sameInstance(userRepository.getCountByUsernameOrEmail(searchTerm))));
    }

    @Test
    public void getAllByAddedWidget_Valid(){
          long widgetId = 123;
        List<Page> pages = new ArrayList<Page>();
        Page page = new PageImpl();
        User owner = new UserImpl();
        page.setOwner(owner);
        pages.add(page);
        expect(pageTemplate.find(query(where("regions").elemMatch(where("regionWidgets").elemMatch(where("widgetId").is(widgetId)))))).andReturn(pages);
        replay(pageTemplate);

        List<User> users = userRepository.getAllByAddedWidget(widgetId);

        assertTrue(users.size() == pages.size());
        assertTrue(users.contains(owner));
    }

    @Test
    public void getByForgotPasswordHash_Valid(){
        String hash = "hashbrown";
        User found = new UserImpl();
        expect(template.findOne(query(where("forgotPasswordHash").is(hash)))).andReturn(found);
        replay(template);

        assertThat(found, is(sameInstance(userRepository.getByForgotPasswordHash(hash))));
    }

    @Test
    public void getType_Valid(){
        assertThat((Class<MongoDbUser>)userRepository.getType(), is(equalTo(MongoDbUser.class)));
    }

    @Test
    public void get_Valid(){
        long id = 123;
        User user = new UserImpl();
        expect(template.get(id)).andReturn(user);
        replay(template);
        assertThat(user, is(sameInstance(userRepository.get(id))));
    }

    @Test
    public void save_Valid(){
        User item = new UserImpl();
        User saved = new UserImpl();
        expect(template.save(item)).andReturn(saved);
        replay(template);
        assertThat(saved, is(sameInstance(userRepository.save(item))));
    }

    @Test
    public void delete_Valid(){
        User item = new UserImpl();
        ((UserImpl)item).setId((long)777);
        template.remove(query(where("_id").is(item.getId())));
        expectLastCall();
        replay(template);

        userRepository.delete(item);
        verify(template);
    }
}
