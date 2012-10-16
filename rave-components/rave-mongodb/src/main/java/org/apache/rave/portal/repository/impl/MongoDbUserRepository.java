/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.MongoDbUser;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.conversion.MongoDbConverter;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository
public class MongoDbUserRepository implements UserRepository {

    @Autowired
    private MongoOperations template;

    @Autowired
    private MongoDbConverter converter;

    @Override
    public User getByUsername(String username) {
        return null;
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
        return template.findById(id, MongoDbUser.class);
    }

    @Override
    public User save(User item) {
        template.save(item);
        return item;
    }

    @Override
    public void delete(User item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
