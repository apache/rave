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


import org.apache.rave.model.Tag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.repository.MongoTagOperations;
import org.apache.rave.portal.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.bson.types.ObjectId.massageToObjectId;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbTagRepository implements TagRepository {

    @Autowired
    private MongoTagOperations template;

    @Override
    public List<Tag> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<Tag> getLimitedList(int offset, int pageSize){
        Query query = new Query().skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query());
    }

    @Override
    public Tag getByKeyword(String keyword) {
        return template.findOne(query(where("keyword").is(keyword)));
    }

    @Override
    public Class<? extends Tag> getType() {
        return TagImpl.class;
    }

    @Override
    public Tag get(String id) {
        return template.get(id);
    }

    @Override
    public Tag save(Tag item) {
        return template.count(query(where("keyword").is(item.getKeyword()))) == 0 ? template.save(item) : item;
    }

    @Override
    public void delete(Tag item) {
        template.remove(query(where("_id").is(item.getId())));
    }

    public void setWidgetTemplate(MongoTagOperations tagTemplate) {
        this.template = tagTemplate;
    }
}
