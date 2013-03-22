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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.model.Category;
import org.apache.rave.portal.model.JpaCategory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts a Category to a JpaCategory
 */
@Component
public class JpaCategoryConverter implements ModelConverter<Category, JpaCategory> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Category> getSourceType() {
        return Category.class;
    }

    @Override
    public JpaCategory convert(Category source) {
        return source instanceof JpaCategory ? (JpaCategory) source : createEntity(source);
    }

    private JpaCategory createEntity(Category source) {
        JpaCategory converted = null;
        if (source != null) {
            converted = source.getId() == null ? new JpaCategory() : manager.find(JpaCategory.class, Long.parseLong(source.getId()));
            if(converted == null) {
                converted = new JpaCategory();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(Category source, JpaCategory converted) {
        converted.setId(source.getId());
        converted.setCreatedDate(source.getCreatedDate());
        converted.setCreatedUserId(source.getCreatedUserId());
        converted.setLastModifiedDate(source.getLastModifiedDate());
        converted.setLastModifiedUserId(source.getLastModifiedUserId());
        converted.setText(source.getText());
        converted.setWidgets(source.getWidgets());
    }
}
