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

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Repository
public class JpaWidgetTagRepository extends AbstractJpaRepository<WidgetTag> implements WidgetTagRepository {

    JpaWidgetTagRepository() {
        super(WidgetTag.class);
    }

    @Override
    public WidgetTag getByWidgetIdAndTag(Long widgetId, String keyword) {
        if (keyword != null) {
            keyword = keyword.trim();
        }
        TypedQuery<WidgetTag> query = manager.createNamedQuery(WidgetTag.FIND_BY_WIDGET_AND_KEYWORD, WidgetTag.class);
        query.setParameter("keyword", keyword);
        query.setParameter("widgetId", widgetId);
        return getSingleResult(query.getResultList());
    }


}
