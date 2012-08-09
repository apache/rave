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
package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.portal.model.JpaWidgetTag;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.model.conversion.JpaWidgetTagConverter;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;
import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaWidgetTagRepository implements WidgetTagRepository {

    @PersistenceContext
    EntityManager manager;
    
    @Autowired
    JpaWidgetTagConverter converter;

    @Override
    public JpaWidgetTag getByWidgetIdAndTag(String widgetId, String keyword) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        TypedQuery<JpaWidgetTag> query = manager.createNamedQuery(JpaWidgetTag.FIND_BY_WIDGET_AND_KEYWORD, JpaWidgetTag.class);
        query.setParameter("keyword", keyword);
        query.setParameter("widgetId", widgetId == null ? null : Long.parseLong(widgetId));
        return getSingleResult(query.getResultList());
    }

    @Override
    public Class<? extends WidgetTag> getType() {
        return JpaWidgetTag.class;
    }

    @Override
    public WidgetTag get(String id) {
        return manager.find(JpaWidgetTag.class, id);
    }

    @Override
    public WidgetTag save(WidgetTag item) {
        JpaWidgetTag widgetTag = converter.convert(item);
        //We know this cast will succeed since we are dealing with a JpaWidgetTag
        //since  this is a reciprocal relationship, we need to make sure we save one side of it first
        JpaTag tag = (JpaTag)widgetTag.getTag();
        item.setTag(saveOrUpdate(tag.getEntityId(), manager, tag));

        return saveOrUpdate(widgetTag.getEntityId(), manager, widgetTag);
    }

    @Override
    public void delete(WidgetTag item) {
        manager.remove(item instanceof JpaWidgetTag ? item: getByWidgetIdAndTag(item.getWidgetId(), item.getTag().getKeyword()));
    }
}
