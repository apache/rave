/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetCategory;
import org.apache.rave.portal.repository.WidgetCategoryRepository;
import org.apache.rave.portal.service.WidgetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class DefaultWidgetCategoryService implements WidgetCategoryService {

    private final WidgetCategoryRepository widgetCategoryRepository;

    @Autowired
    public DefaultWidgetCategoryService(WidgetCategoryRepository widgetCategoryRepository) {
        this.widgetCategoryRepository = widgetCategoryRepository;
    }

    @Override
    public WidgetCategory get(long entityId) {
        return widgetCategoryRepository.get(entityId);
    }

    @Override
    public List<WidgetCategory> getAll() {
        return widgetCategoryRepository.getAll();
    }

    @Override
    @Transactional
    public WidgetCategory create(String text, User createdUser) {
        WidgetCategory widgetCategory = new WidgetCategory();
        Date now = new Date();
        widgetCategory.setText(text);
        widgetCategory.setCreatedDate(now);
        widgetCategory.setCreatedUser(createdUser);
        widgetCategory.setLastModifiedDate(now);
        widgetCategory.setLastModifiedUser(createdUser);
        widgetCategoryRepository.save(widgetCategory);
        return widgetCategory;
    }

    @Override
    @Transactional
    public WidgetCategory update(long widgetCategoryId, String text, User lastModifiedUser) {
        WidgetCategory widgetCategory = widgetCategoryRepository.get(widgetCategoryId);
        widgetCategory.setText(text);
        widgetCategory.setLastModifiedDate(new Date());
        widgetCategory.setLastModifiedUser(lastModifiedUser);
        widgetCategoryRepository.save(widgetCategory);
        return widgetCategory;
    }

    @Override
    @Transactional
    public void delete(WidgetCategory widgetCategory) {
        widgetCategoryRepository.delete(widgetCategory);
    }
}
