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

package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.WidgetTagImpl;
import org.apache.rave.portal.repository.TagRepository;
import org.apache.rave.portal.repository.UserRepository;

/**
 */
public class MongoDbWidgetTag extends WidgetTagImpl {

    private UserRepository userRepository;
    private TagRepository tagRepository;

    private Long userId;
    private String tagKeyword;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository repository) {
        this.userRepository = repository;
    }

    public TagRepository getTagRepository() {
        return tagRepository;
    }

    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public String getTagKeyword() {
        return tagKeyword;
    }

    public void setTagKeyword(String keyword) {
        this.tagKeyword = keyword;
    }

    @Override
    public Tag getTag() {
        Tag tag = super.getTag();
        if(tag == null) {
            tag = tagRepository.getByKeyword(tagKeyword);
            super.setTag(tag);
        }
        return tag;
    }

    @Override
    public User getUser() {
        User user = super.getUser();
        if(user == null) {
            user = userRepository.get(userId);
            super.setUser(user);
        }
        return user;
    }
}
