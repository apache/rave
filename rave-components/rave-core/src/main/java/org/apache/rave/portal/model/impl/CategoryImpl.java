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
package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.*;

import java.util.Date;
import java.util.List;

public class CategoryImpl implements Category {
    private Long id;
    private String text;
    private User createdUser;
    private Date createdDate;
    private User lastModifiedUser;
    private Date lastModifiedDate;
    private List<Widget> widgets;

    public CategoryImpl() {

    }

    public CategoryImpl(Long id) {
        this.id = id;
    }

    public CategoryImpl(String text) {
        this.text = text;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public User getCreatedUser() {
        return createdUser;
    }

    @Override
    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public User getLastModifiedUser() {
        return lastModifiedUser;
    }

    @Override
    public void setLastModifiedUser(User lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public List<Widget> getWidgets() {
        return widgets;
    }

    @Override
    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CategoryImpl category = (CategoryImpl) obj;
        if (id != null ? !id.equals(category.getId()) : category.getId() != null) return false;
        return true;
    }
 }
