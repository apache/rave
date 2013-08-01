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

package org.apache.rave.portal.service.impl;

import org.apache.rave.model.Tag;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultTagService implements TagService {


    private final TagRepository repository;

    @Autowired
    public DefaultTagService(TagRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tag getTagById(String entityId) {
        return repository.get(entityId);
    }


    @Override
    public List<Tag> getAllTagsList() {
        return repository.getAll();
    }

    @Override
    public SearchResult<Tag> getAll() {
        final int count = repository.getCountAll();
        final List<Tag> tags = repository.getAll();
        return new SearchResult<Tag>(tags, count);
    }

    @Override
    public SearchResult<Tag> getLimitedList(int offset, int pageSize) {
        final int count = repository.getCountAll();
        final List<Tag> tags = repository.getLimitedList(offset, pageSize);
        final SearchResult<Tag> searchResult = new SearchResult<Tag>(tags, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public Tag getTagByKeyword(String keyword) {
        return repository.getByKeyword(keyword);

    }

    @Override
    @Transactional
    public Tag save(Tag tag) {
        return repository.save(tag);
    }

//    @Override
//    public List<Tag> getAvailableTagsByWidgetId(String widgetId) {
//
//        return repository.getAvailableTagsByWidgetId(widgetId);
//
//    }


}
