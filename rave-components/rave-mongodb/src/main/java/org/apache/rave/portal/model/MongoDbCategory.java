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

import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.UserRepository;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MongoDbCategory extends CategoryImpl {

    @XmlTransient @JsonIgnore
    private UserRepository userRepository;

    @XmlTransient @JsonIgnore
    private MongoWidgetOperations widgetTemplate;

    private Long lastModifiedUserId;
    private Long createdUserId;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MongoWidgetOperations getWidgetRepository() {
        return widgetTemplate;
    }

    public void setWidgetRepository(MongoWidgetOperations widgetRepository) {
        this.widgetTemplate = widgetRepository;
    }

    public Long getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public void setLastModifiedUserId(Long lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    public Long getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Long createdUserId) {
        this.createdUserId = createdUserId;
    }

    @Override
    public User getCreatedUser() {
        User creator = super.getCreatedUser();
        if(creator == null && createdUserId != null && userRepository != null) {
            creator = userRepository.get(createdUserId);
            super.setCreatedUser(creator);
        }
        return creator;
    }

    @Override
    public User getLastModifiedUser() {
        User lastModifier = super.getLastModifiedUser();
        if(lastModifier == null && lastModifiedUserId != null && userRepository != null) {
            lastModifier = userRepository.get(lastModifiedUserId);
            super.setLastModifiedUser(lastModifier);
        }
        return lastModifier;
    }

    @Override
    public List<Widget> getWidgets() {
        List<Widget> widgets =  super.getWidgets();
        if(widgets == null) {
            widgets = widgetTemplate.find(query(where("categoryIds").is(this.getId())));
            super.setWidgets(widgets);
        }
        return widgets;
    }
}