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

import org.apache.rave.portal.model.JpaWidgetComment;
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.model.conversion.JpaWidgetCommentConverter;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.saveOrUpdate;

@Repository
public class JpaWidgetCommentRepository implements WidgetCommentRepository {
    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private JpaWidgetCommentConverter widgetCommentConverter;

    @Override
    public Class<? extends WidgetComment> getType() {
        return JpaWidgetComment.class;
    }

    @Override
    public WidgetComment get(String id) {
        return manager.find(JpaWidgetComment.class, id);
    }

    @Override
    public WidgetComment save(WidgetComment item) {
        JpaWidgetComment category = widgetCommentConverter.convert(item);
        return saveOrUpdate(category.getEntityId(), manager, category);
    }

    @Override
    public void delete(WidgetComment item) {
        manager.remove(item instanceof JpaWidgetComment ? item : get(item.getId()));
    }

    @Override
    public int deleteAll(String userId) {
        TypedQuery<JpaWidgetComment> query = manager.createNamedQuery(JpaWidgetComment.DELETE_ALL_BY_USER, JpaWidgetComment.class);
        query.setParameter("userId", userId == null ? null : Long.parseLong(userId));
        return query.executeUpdate();
    }
}
