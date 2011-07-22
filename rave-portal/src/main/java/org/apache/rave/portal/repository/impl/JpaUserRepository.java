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
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 */
@Repository
public class JpaUserRepository extends AbstractJpaRepository<User> implements UserRepository{

    public JpaUserRepository() {
        super(User.class);
    }

    @Override
    public User getByUsername(String username) {
        TypedQuery<User> query = manager.createNamedQuery("User.getByUsername", User.class);
        query.setParameter("username", username);
        return getSingleResult(query.getResultList());
    }
}
