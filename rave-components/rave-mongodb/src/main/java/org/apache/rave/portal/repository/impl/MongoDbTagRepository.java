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


import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbTagRepository implements TagRepository{

    @Autowired
    private MongoOperations template;

    @Override
    public List<Tag> getAll() {
        return null;
    }

    @Override
    public int getCountAll() {
        return 0;
    }

    @Override
    public Tag getByKeyword(String keyword) {
        return null;
    }

    @Override
    public List<Tag> getAvailableTagsByWidgetId(Long widgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Tag> getType() {
        return Tag.class;
    }

    @Override
    public Tag get(long id) {
        throw new NotSupportedException("Cannot access tags by Id");
    }

    @Override
    public Tag save(Tag item) {
        throw new NotSupportedException("Cannot save tags directly");
    }

    @Override
    public void delete(Tag item) {
        throw new NotSupportedException("Cannot delete tags directly");
    }
}
