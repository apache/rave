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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.WidgetRatingRepository}
 */
@Repository
public class JpaWidgetRatingRepository extends AbstractJpaRepository<WidgetRating> implements WidgetRatingRepository {

    public JpaWidgetRatingRepository() {
        super(WidgetRating.class);
    }

    @Override
    public WidgetRating getByWidgetIdAndUserId(Long widgetId, Long userId) {
        TypedQuery<WidgetRating> query =
                manager.createNamedQuery(WidgetRating.WIDGET_RATING_BY_WIDGET_AND_USER, WidgetRating.class);
        query.setParameter(WidgetRating.PARAM_WIDGET_ID, widgetId);
        query.setParameter(WidgetRating.PARAM_USER_ID, userId);
        final List<WidgetRating> resultList = query.getResultList();
        return getSingleResult(resultList);
    }

    @Override
    public int deleteAll(Long userId) {
        TypedQuery<WidgetRating> query = manager.createNamedQuery(WidgetRating.DELETE_ALL_BY_USER, WidgetRating.class);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }
}