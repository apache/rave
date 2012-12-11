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

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbWidgetRatingRepository implements WidgetRatingRepository {

    @Autowired
    private MongoWidgetOperations template;

    @Override
    public WidgetRating getByWidgetIdAndUserId(Long widgetId, Long userId) {
        Widget widget = template.get(widgetId);
        return getRatingByUserId(widget, userId);
    }

    @Override
    public int deleteAll(Long userId) {
        int count = 0;
        List<Widget> widgets = template.find(query(where("ratings").elemMatch(where("userId").is(userId))));
        for (Widget widget : widgets) {
            count += updateWidgetRating(userId, widget);
        }
        return count;
    }

    @Override
    public Class<? extends WidgetRating> getType() {
        return WidgetRating.class;
    }

    @Override
    public WidgetRating get(long id) {
        Widget widget = template.findOne(query(where("ratings").elemMatch(where("_id").is(id))));
        return getWidgetRatingById(widget, id);
    }

    @Override
    public WidgetRating save(WidgetRating item) {
        Widget widget = template.get(item.getWidgetId());
        if (item.getId() == null) {
            item.setId(generateId());
            widget.getRatings().add(item);
        } else {
            updateRating(widget, item);
        }
        Widget saved = template.save(widget);
        return getRatingById(saved, item.getId());
    }

    @Override
    public void delete(WidgetRating item) {
        Widget widget = template.get(item.getWidgetId());
        removeRating(item.getId(), widget);
        template.save(widget);
    }

    private void removeRating(Long commentId, Widget widget) {
        Iterator<WidgetRating> iterator = widget.getRatings().iterator();
        while(iterator.hasNext()) {
            WidgetRating comment = iterator.next();
            if(comment.getId().equals(commentId)) {
                iterator.remove();
                return;
            }
        }
    }

    private void updateRating(Widget widget, WidgetRating item) {
        for(WidgetRating rating : widget.getRatings()) {
            if(rating.getId().equals(item.getId())) {
                rating.setUserId(item.getUserId());
                rating.setScore(item.getScore());
                return;
            }
        }
    }

    private WidgetRating getWidgetRatingById(Widget widget, long id) {
        for (WidgetRating rating : widget.getRatings()) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null;
    }

    private WidgetRating getRatingById(Widget widget, long id) {
        for (WidgetRating rating : widget.getRatings()) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null;
    }

    private int updateWidgetRating(Long userId, Widget widget) {
        int count = 0;
        Iterator<WidgetRating> iterator = widget.getRatings().iterator();
        while (iterator.hasNext()) {
            WidgetRating rating = iterator.next();
            if (rating.getUserId().equals(userId)) {
                iterator.remove();
                count++;
            }
        }
        if (count > 0) {
            template.save(widget);
        }
        return count;
    }

    private WidgetRating getRatingByUserId(Widget widget, Long userId) {
        for (WidgetRating rating : widget.getRatings()) {
            if (rating.getUserId().equals(userId)) {
                return rating;
            }
        }
        return null;
    }

    public void setTemplate(MongoWidgetOperations template) {
        this.template = template;
    }
}
