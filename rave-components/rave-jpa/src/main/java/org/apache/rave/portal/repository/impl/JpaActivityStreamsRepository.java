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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.persistence.jpa.util.JpaUtil;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.JpaActivityStreamsEntry;
import org.apache.rave.portal.model.conversion.JpaActivityStreamsEntryConverter;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class JpaActivityStreamsRepository implements ActivityStreamsRepository {

	@PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaActivityStreamsEntryConverter converter;

    @Override
    public Class<? extends ActivityStreamsEntry> getType() {
        return JpaActivityStreamsEntry.class;
    }

    @Transactional
	public ActivityStreamsEntry save(ActivityStreamsEntry e) {
        JpaActivityStreamsEntry entry = converter.convert(e);

        if(entry.getUserId() == null && entry.getActor() != null) {
            entry.setUserId(entry.getActor().getId());
        }
        return JpaUtil.saveOrUpdate(entry.getId(), manager, entry);
	}

    public List<ActivityStreamsEntry> getAll() {
		TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery(JpaActivityStreamsEntry.FIND_ALL, JpaActivityStreamsEntry.class);
		return CollectionUtils.<ActivityStreamsEntry>toBaseTypedList(query.getResultList());
	}

    @Override
    public List<ActivityStreamsEntry> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    public ActivityStreamsEntry get(String id) {
        TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery(JpaActivityStreamsEntry.FIND_BY_ID, JpaActivityStreamsEntry.class);
        query.setParameter("id", id);
		return CollectionUtils.getSingleValue(query.getResultList());
	}

	public List<ActivityStreamsEntry> getByUserId(String id) {
		TypedQuery<JpaActivityStreamsEntry> query = manager.createNamedQuery(JpaActivityStreamsEntry.FIND_BY_USERID, JpaActivityStreamsEntry.class);
        query.setParameter("userId", id);
		return CollectionUtils.<ActivityStreamsEntry>toBaseTypedList(query.getResultList());
	}

    @Transactional
	public void delete(ActivityStreamsEntry e) {
		deleteById(e.getId());
	}
	
	@Transactional
	public void deleteById(String id) {
        ActivityStreamsEntry e = get(id);
		manager.remove(e);
	}
}
