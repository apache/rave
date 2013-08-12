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

import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.conversion.JpaUserConverter;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.*;

/**
 */
@Repository
public class JpaUserRepository implements UserRepository {

    private final Logger log = LoggerFactory.getLogger(JpaUserRepository.class);

    @Autowired
    private JpaUserConverter converter;

    @PersistenceContext
    private EntityManager manager;

    @Override
    public User getByUsername(String username) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_BY_USERNAME, JpaUser.class);
        query.setParameter(JpaUser.PARAM_USERNAME, username);
        return getSingleResult(query.getResultList());
    }

    @Override
    public User getByUserEmail(String userEmail) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_BY_USER_EMAIL, JpaUser.class);
        query.setParameter(JpaUser.PARAM_EMAIL, userEmail);
        return getSingleResult(query.getResultList());
    }

    @Override
    public User getByOpenId(String openId) {
    	TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_BY_OPENID, JpaUser.class);
        query.setParameter(JpaUser.PARAM_OPENID, openId);
        return getSingleResult(query.getResultList());
    }

    @Override
    public List<User> getAll() {
        log.warn("Requesting potentially large resultset of Users. No pagesize set.");
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_ALL, JpaUser.class);
        return CollectionUtils.<User>toBaseTypedList(query.getResultList());

    }

    @Override
    public List<User> getLimitedList(int offset, int pageSize) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_ALL, JpaUser.class);
        return CollectionUtils.<User>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountAll() {
        Query query = manager.createNamedQuery(JpaUser.USER_COUNT_ALL);
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<User> findByUsernameOrEmail(String searchTerm, int offset, int pageSize) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_FIND_BY_USERNAME_OR_EMAIL, JpaUser.class);
        query.setParameter(JpaUser.PARAM_SEARCHTERM, "%" + searchTerm.toLowerCase() + "%");
        return CollectionUtils.<User>toBaseTypedList(getPagedResultList(query, offset, pageSize));
    }

    @Override
    public int getCountByUsernameOrEmail(String searchTerm) {
        Query query = manager.createNamedQuery(JpaUser.USER_COUNT_FIND_BY_USERNAME_OR_EMAIL);
        query.setParameter(JpaUser.PARAM_SEARCHTERM, "%" + searchTerm.toLowerCase() + "%");
        Number countResult = (Number) query.getSingleResult();
        return countResult.intValue();
    }

    @Override
    public List<User> getAllByAddedWidget(String widgetId) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_ALL_FOR_ADDED_WIDGET, JpaUser.class);
        query.setParameter(JpaUser.PARAM_WIDGET_ID, Long.parseLong(widgetId));
        return CollectionUtils.<User>toBaseTypedList(query.getResultList());
    }

    @Override
    public User getByForgotPasswordHash(String hash) {
        TypedQuery<JpaUser> query = manager.createNamedQuery(JpaUser.USER_GET_BY_FORGOT_PASSWORD_HASH, JpaUser.class);
        query.setParameter(JpaUser.PARAM_FORGOT_PASSWORD_HASH, hash);
        return getSingleResult(query.getResultList());
    }

    @Override
    public Class<? extends User> getType() {
        return JpaUser.class;
    }

    @Override
    public User get(String id) {
        long primaryKey = Long.parseLong(id);
        return manager.find(JpaUser.class, primaryKey);
    }

    @Override
    public User save(User item) {
        JpaUser converted = converter.convert(item);
        return saveOrUpdate(converted.getEntityId(), manager, converted);
    }

    @Override
    public void delete(User item) {
       manager.remove(converter.convert(item));
    }
}
