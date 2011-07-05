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

import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.portal.repository.impl.util.JpaUtil.getSingleResult;
import static org.apache.rave.portal.repository.impl.util.JpaUtil.saveOrUpdate;

/**
 */
@Repository
public class JpaUserRepository implements UserRepository{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public User get(long userId) {
        return manager.find(User.class, userId);
    }

    @Override
    public User getByUsername(String username) {
        TypedQuery<User> query = manager.createNamedQuery("User.getByUsername", User.class);
        query.setParameter("username", username);
        return getSingleResult(query.getResultList());
    }

    @Override
    public User save(User user) {
		  return saveOrUpdate(user.getUserId(),manager,user);
    }
}
