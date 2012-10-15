package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public class MongoDbUserRepository implements UserRepository {
    @Override
    public User getByUsername(String username) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getByUserEmail(String userEmail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getByOpenId(String openId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getLimitedList(int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountAll() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountByUsernameOrEmail(String searchTerm) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getAllByAddedWidget(long widgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getByForgotPasswordHash(String hash) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends User> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User save(User item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(User item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
