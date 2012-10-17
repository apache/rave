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

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class MongoDbCategory extends CategoryImpl {

    @XmlTransient @JsonIgnore
    private UserRepository userRepository;

    @XmlTransient @JsonIgnore
    private WidgetRepository widgetTagRepository;

    private Long lastModifiedUserId;
    private Long createdUserId;

    private List<Long> widgetIds;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public WidgetRepository getWidgetTagRepository() {
        return widgetTagRepository;
    }

    public void setWidgetTagRepository(WidgetRepository widgetTagRepository) {
        this.widgetTagRepository = widgetTagRepository;
    }

    public List<Long> getWidgetIds() {
        return widgetIds;
    }

    public void setWidgetIds(List<Long> widgetIds) {
        this.widgetIds = widgetIds;
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
        if(creator == null) {
            creator = userRepository.get(createdUserId);
            super.setCreatedUser(creator);
        }
        return creator;
    }

    @Override
    public User getLastModifiedUser() {
        User lastModifier = super.getLastModifiedUser();
        if(lastModifier == null) {
            lastModifier = userRepository.get(lastModifiedUserId);
            super.setLastModifiedUser(lastModifier);
        }
        return lastModifier;
    }

    @Override
    public List<Widget> getWidgets() {
        List<Widget> widgets =  super.getWidgets();
        if(widgets == null) {
            widgets = Lists.newArrayList();
            for(Long widgetId : widgetIds) {
                widgets.add(widgetTagRepository.get(widgetId));
            }
        }
        return widgets;
    }
}
