/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.MongoDbActivityStreamsEntry;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import org.apache.rave.exception.NotSupportedException;

import java.util.List;

import static org.apache.rave.portal.repository.util.CollectionNames.ACTIVITIES;
import static org.apache.rave.util.CollectionUtils.toBaseTypedList;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 *  Placeholder repository
 */
@Repository
public class MongoDbActivityStreamsRepository implements ActivityStreamsRepository {
    private static final Class<? extends ActivityStreamsEntry> CLASS = MongoDbActivityStreamsEntry.class;

    private final MongoOperations template;
    private final HydratingConverterFactory converter;

    @Autowired
    public MongoDbActivityStreamsRepository(MongoOperations template, HydratingConverterFactory converter) {
        this.template = template;
        this.converter = converter;
    }

    @Override
    public List<ActivityStreamsEntry> getAll() {
        return toBaseTypedList(template.findAll(CLASS, ACTIVITIES));
    }

    @Override
    public List<ActivityStreamsEntry> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public List<ActivityStreamsEntry> getByUserId(String id) {
        return toBaseTypedList(template.find(query(where("actor._id").is(id)),CLASS, ACTIVITIES));
    }

    @Override
    public void deleteById(String id) {
        template.remove(query(where("_id").is(id)), ACTIVITIES);
    }

    @Override
    public Class<? extends ActivityStreamsEntry> getType() {
        return CLASS;
    }

    @Override
    public ActivityStreamsEntry get(String id) {
        return template.findById(id, CLASS, ACTIVITIES);
    }

    @Override
    public ActivityStreamsEntry save(ActivityStreamsEntry item) {
        ActivityStreamsEntryImpl converted = converter.convert(item, ActivityStreamsEntry.class);
        template.save(converted, ACTIVITIES);
        return converted;
    }

    @Override
    public void delete(ActivityStreamsEntry item) {
        if(item != null) deleteById(item.getId());
    }
}
