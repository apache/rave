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
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 */

@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonAutoDetect(value = JsonMethod.FIELD)
public class MongoDbWidget extends WidgetImpl {

    @XmlTransient @JsonIgnore
    private CategoryRepository categoryRepository;

    @XmlTransient @JsonIgnore
    private UserRepository userRepository;

    private Long ownerId;
    private List<Long> categoryIds;

    public MongoDbWidget() { }

    public MongoDbWidget(long id) {
        super(id);
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getOwner() {
        User user = super.getOwner();
        if(user == null && ownerId != null) {
            user = userRepository.get(ownerId);
            super.setOwner(user);
        }
        return user;
    }

    @Override
    public List<Category> getCategories() {
        ensureCategoryIds();
        List<Category> categories = super.getCategories();
        if(categories == null || categories.isEmpty()) {
            categories = createCategoriesFromIds();
        }
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Widget)) return false;

        Widget that = (Widget) o;

        if (this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }

    private void ensureCategoryIds() {
        if(categoryIds == null) {
            categoryIds = Lists.newArrayList();
        }
    }

    private void addCategory(List<Category> categories, Long id) {
        Category category = categoryRepository.get(id);
        if(category != null) {
            categories.add(category);
        }
    }

    private List<Category> createCategoriesFromIds() {
        List<Category> categories;
        categories = Lists.newArrayList();
        for(Long id : categoryIds) {
            addCategory(categories, id);
        }
        super.setCategories(categories);
        return categories;
    }
}
