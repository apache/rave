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
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class JpaWidgetCommentRepository extends AbstractJpaRepository<WidgetComment> implements WidgetCommentRepository {
    
    public JpaWidgetCommentRepository() {
        super(WidgetComment.class);
    }

    @Override
    public int deleteAll(Long userId) {
        TypedQuery<WidgetComment> query = manager.createNamedQuery(WidgetComment.DELETE_ALL_BY_USER, WidgetComment.class);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }
}
