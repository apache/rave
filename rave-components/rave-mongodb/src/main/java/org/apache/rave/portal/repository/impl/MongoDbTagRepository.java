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

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.repository.TagRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbTagRepository implements TagRepository {
    @Override
    public List<Tag> getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountAll() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tag getByKeyword(String keyword) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Tag> getAvailableTagsByWidgetId(Long widgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends Tag> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tag get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tag save(Tag item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Tag item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
