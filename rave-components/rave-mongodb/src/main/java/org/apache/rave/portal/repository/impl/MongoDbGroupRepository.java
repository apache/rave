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
import org.apache.rave.model.Group;
import org.apache.rave.portal.model.MongoDbGroup;
import org.apache.rave.portal.repository.MongoGroupOperations;
import org.apache.rave.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbGroupRepository implements GroupRepository {

    @Autowired
    private MongoGroupOperations template;

    @Override
    public Group findByTitle(String title) {
        return template.findOne(query(where("title").is(title)));
    }

    @Override
    public Class<? extends Group> getType() {
        return MongoDbGroup.class;
    }

    @Override
    public Group get(String id) {
        return template.get(id);
    }

    @Override
    public List<Group> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<Group> getLimitedList(int offset, int limit) {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public int getCountAll() {
        throw new NotSupportedException("This function is not yet implemented for this class.");
    }

    @Override
    public Group save(Group item) {
        return template.save(item);
    }

    @Override
    public void delete(Group item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    public void setTemplate(MongoGroupOperations template) {
        this.template = template;
    }
}
