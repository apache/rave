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

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.rave.portal.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.ActivityStreamsItem;
import org.apache.rave.portal.model.JpaActivityStreamsEntry;
import org.apache.rave.persistence.jpa.util.JpaUtil;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class JpaActivityStreamsRepository implements ActivityStreamsRepository {

	@PersistenceContext
    private EntityManager manager;

	@Transactional
	public <T extends ActivityStreamsEntry> T save(T e) {
		return JpaUtil.saveOrUpdate(e.getId(), manager, e);
	}
	
	@Transactional
	public Collection<? extends ActivityStreamsEntry> getAll() {
		TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery("JpaActivityStreamsEntry.findAll", JpaActivityStreamsEntry.class);
		return query.getResultList();
	}

	@Transactional
	public <T extends ActivityStreamsEntry> T getById(String id) {
		return (T)manager.find(JpaActivityStreamsEntry.class, id);
	}
	
	@Transactional
	public Collection<? extends ActivityStreamsEntry> getByUserId(String id) {
		TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery("JpaActivityStreamsEntry.findByUserId", JpaActivityStreamsEntry.class);
        query.setParameter("userId", id);
		return query.getResultList();
	}
	
	@Transactional
	public <T extends ActivityStreamsEntry> void delete(T e) {
		manager.remove(manager.merge(e));
	}
	
	@Transactional
	public <T extends ActivityStreamsEntry> void deleteById(String id) {
        T e = getById(id);
		manager.remove(manager.merge(e));
	}
}
