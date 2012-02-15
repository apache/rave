/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getPagedResultList;
import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 */
@Repository
public class JpaUserRepository extends AbstractJpaRepository<User> implements UserRepository {

    public JpaUserRepository() {
        super(User.class);
    }

    @Override
    public User getByUsername(String username) {
        TypedQuery<User> query = manager.createNamedQuery(User.USER_GET_BY_USERNAME, User.class);
        query.setParameter(User.PARAM_USERNAME, username);
        return getSingleResult(query.getResultList());
    }

    @Override
    public User getByUserEmail(String userEmail) {
        TypedQuery<User> query = manager.createNamedQuery(User.USER_GET_BY_USER_EMAIL, User.class);
        query.setParameter(User.PARAM_EMAIL, userEmail);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<User> getLimitedList(int offset, int pageSize) {
        TypedQuery<User> query = manager.createNamedQuery(User.USER_GET_ALL, User.class);
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(User.USER_COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize) {
        TypedQuery<User> query = manager.createNamedQuery(User.USER_FIND_BY_USERNAME_OR_EMAIL, User.class);
        query.setParameter(User.PARAM_SEARCHTERM, "%" + searchTerm.toLowerCase() + "%");
        return getPagedResultList(query, offset, pageSize);
    }

    @Override
    public int getCountByUsernameOrEmail(String searchTerm) {
        Query query = manager.createNamedQuery(User.USER_COUNT_FIND_BY_USERNAME_OR_EMAIL);
        query.setParameter(User.PARAM_SEARCHTERM, "%" + searchTerm.toLowerCase() + "%");
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<User> getAllByAddedWidget(long widgetId) {
        TypedQuery<User> query = manager.createNamedQuery(User.USER_GET_ALL_FOR_ADDED_WIDGET, User.class);
        query.setParameter(User.PARAM_WIDGET_ID, widgetId);
        return query.getResultList();
    }
}
