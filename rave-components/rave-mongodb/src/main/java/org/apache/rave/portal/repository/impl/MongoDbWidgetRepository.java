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

import com.google.common.collect.Maps;
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoTagOperations;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.bson.types.ObjectId.massageToObjectId;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;


/**
 */
@Repository
public class MongoDbWidgetRepository implements WidgetRepository {

    @Autowired
    private MongoWidgetOperations template;

    @Autowired
    private MongoTagOperations tagTemplate;

    @Autowired
    private StatisticsAggregator statsAggregator;

    @Override
    public List<Widget> getAll() {
        return template.find(addSort(new Query()));
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return template.find(addSort(new Query().skip(offset).limit(pageSize)));
    }

    @Override
    public int getCountAll() {
        return (int) template.count(new Query());
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        Query query = new Query(addFreeTextClause(searchTerm, new Criteria())).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return (int) template.count(new Query(addFreeTextClause(searchTerm, new Criteria())));
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        Query query = new Query(where("widgetStatus").is(getWidgetStatusString(widgetStatus))).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return (int) template.count(new Query(where("widgetStatus").is(getWidgetStatusString(widgetStatus))));
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        Query query = getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm).limit(pageSize).skip(offset);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return (int) template.count(getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm));
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        Query query = getQueryByOwner(owner).skip(offset).limit(pageSize);
        return template.find(addSort(query));
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return (int) template.count(getQueryByOwner(owner));
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return template.findOne(new Query(where("url").is(widgetUrl)));
    }

    @Override
    public WidgetStatistics getWidgetStatistics(String widget_id, String user_id) {
        return statsAggregator.getWidgetStatistics(widget_id, user_id);
    }

    @Override
    public Map<String, WidgetStatistics> getAllWidgetStatistics(String userId) {
        return statsAggregator.getAllWidgetStatistics(userId);
    }

    @Override
    public Map<String, WidgetRating> getUsersWidgetRatings(String userId) {
        Query q = query(where("ratings").elemMatch(where("userId").is(userId)));
        List<Widget> widgets = template.find(q);
        Map<String, WidgetRating> ratings = Maps.newHashMap();
        for (Widget widget : widgets) {
            for (WidgetRating rating : widget.getRatings()) {
                if (rating.getUserId().equals(userId)) {
                    ratings.put(widget.getId(), rating);
                    break;
                }
            }
        }
        return ratings;
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {
        return template.find(addSort(getTagQuery(tagKeyWord).limit(pageSize).skip(offset)));
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        return (int) template.count(getTagQuery(tagKeyword));
    }

    @Override
    public int unassignWidgetOwner(String userId) {
        Query query = query(where("ownerId").is(userId));
        return template.update(query, update("ownerId", null));
    }

    @Override
    public Class<? extends Widget> getType() {
        return MongoDbWidget.class;
    }

    @Override
    public Widget get(String id) {
        return template.get(id);
    }

    @Override
    public Widget save(Widget item) {
        return template.save(item);
    }

    @Override
    public void delete(Widget item) {
        template.remove(new Query(where("_id").is(item.getId())));
    }

    /*
     * Begin WidgetRatingRepository Methods
     *
     */
    @Override
    public WidgetRating getWidgetRatingsByWidgetIdAndUserId(String widgetId, String userId) {
        Widget widget = template.get(widgetId);
        return getRatingByUserId(widget, userId);
    }

    @Override
    public int deleteAllWidgetRatings(String userId) {
        int count = 0;
        List<Widget> widgets = template.find(query(where("ratings").elemMatch(where("userId").is(userId))));
        for (Widget widget : widgets) {
            count += removeUserRatings(userId, widget);
        }
        return count;
    }

    @Override
    public WidgetRating getRatingById(String widgetId, String id) {
        Widget widget = template.get(widgetId);
        return getWidgetRatingById(widget, id);
    }

    @Override
    public WidgetRating createWidgetRating(String widgetId, WidgetRating rating) {
        Widget widget = template.get(widgetId);
        widget.getRatings().add(rating);
        save(widget);
        return rating;
    }

    @Override
    public WidgetRating updateWidgetRating(String widgetId, WidgetRating rating) {
        Widget widget = template.get(widgetId);
        WidgetRating updated = updateRating(widget, rating);
        save(widget);
        return updated;
    }

    @Override
    public void deleteWidgetRating(String widgetId, WidgetRating item) {
        Widget widget = template.get(widgetId);
        removeRating(item.getId(), widget);
        template.save(widget);
    }

    private WidgetRating updateRating(Widget widget, WidgetRating rating) {
        for(WidgetRating currentRating : widget.getRatings()) {
            if(currentRating.getId().equals(rating.getId())) {
                currentRating.setScore(rating.getScore());
                currentRating.setUserId(rating.getUserId());
                return currentRating;
            }
        }
        return null;
    }

    private void removeRating(String ratingId, Widget widget) {
        Iterator<WidgetRating> iterator = widget.getRatings().iterator();
        while (iterator.hasNext()) {
            WidgetRating comment = iterator.next();
            if (comment.getId().equals(ratingId)) {
                iterator.remove();
                return;
            }
        }
    }

    private WidgetRating getWidgetRatingById(Widget widget, String id) {
        for (WidgetRating rating : widget.getRatings()) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null;
    }

    private int removeUserRatings(String userId, Widget widget) {
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

    private WidgetRating getRatingByUserId(Widget widget, String userId) {
        for (WidgetRating rating : widget.getRatings()) {
            if (rating.getUserId().equals(userId)) {
                return rating;
            }
        }
        return null;
    }

    /*
     * End WidgetRating Repository Methods
     */

    /*
     * Begin WidgetTagRepository Methods
     */
    @Override
    public WidgetTag getTagByWidgetIdAndKeyword(String widgetId, String keyword) {
        Widget widget = template.get(widgetId);
        return getTagByKeyword(keyword, widget);
    }

    @Override
    public WidgetTag getTagById(String id) {
        throw new NotSupportedException("Widget tags are not stored by ID");
    }

    @Override
    public WidgetTag saveWidgetTag(String widgetId, WidgetTag item) {
        Widget widget = template.get(widgetId);
        updateOrAddTag(widget, item);
        Widget saved = template.save(widget);
        return getWidgetTagByTagId(saved, item.getTagId());
    }

    @Override
    public void deleteWidgetTag(WidgetTag item) {
        List<Widget> widgets = template.find(query(where("tags").elemMatch(where("tagId").is(item.getTagId()).and("userId").is(item.getUserId()).and("createdDate").is(item.getCreatedDate()))));
        if(widgets.size() > 1 || widgets.size() == 0) {
            throw new IllegalArgumentException("Unable to delete tag.  Indistinguishable from a tag on another widget or the tag doesn't exist");
        } else {
            Widget widget = widgets.get(0);
            removeTag(item.getTagId(), widget);
            save(widget);
        }
    }

    private void updateOrAddTag(Widget widget, WidgetTag item) {
        //Tags can only be created once.  No reason to update the tag if it has already been made.
        WidgetTag tag = getWidgetTagByTagId(widget, item.getTagId());
        if (tag == null) {
            widget.getTags().add(item);
        }
    }

    private void removeTag(String id, Widget widget) {
        Iterator<WidgetTag> iterator = widget.getTags().iterator();
        while (iterator.hasNext()) {
            WidgetTag widgetTag = iterator.next();
            if (widgetTag.getTagId().equals(id)) {
                iterator.remove();
                return;
            }
        }
    }

    private WidgetTag getTagByKeyword(String keyword, Widget widget) {
        Tag tag = getTag(keyword);
        return tag == null ? null : getWidgetTagByTagId(widget, tag.getId());
    }

    private Tag getTag(String keyword) {
        return tagTemplate.findOne(query(where("keyword").is(keyword)));
    }

    private WidgetTag getWidgetTagByTagId(Widget widget, String tagId) {
        for (WidgetTag widgetTag : widget.getTags()) {
            if (widgetTag.getTagId().equals(tagId)) {
                return widgetTag;
            }
        }
        return null;
    }

    /*
     * End WidgetTag Repository Methods
     */

    /*
     * WidgetComment Repository
     */
    @Override
    public WidgetComment getCommentById(String widgetId, String id) {
        return getCommentById(template.get(widgetId), id);
    }

    @Override
    public WidgetComment createWidgetComment(String widgetId, WidgetComment comment) {
        Widget widget = template.get(widgetId);
        widget.getComments().add(comment);
        widget = save(widget);
        return findCommentByProperties(widget, comment);
    }

    @Override
    public WidgetComment updateWidgetComment(String widgetId, WidgetComment comment) {
        Widget widget = template.get(widgetId);
        updateComment(widget, comment);
        return getCommentById(save(widget), comment.getId());
    }

    @Override
    public void deleteWidgetComment(String widgetId, WidgetComment comment) {
        Widget widget = template.get(widgetId);
        removeComment(comment.getId(), widget);
        template.save(widget);
    }

    @Override
    public int deleteAllWidgetComments(String userId) {
        int count = 0;
        List<Widget> widgets = template.find(query(where("comments").elemMatch(where("userId").is(userId))));
        for (Widget widget : widgets) {
            count += updateWidget(userId, widget);
        }
        return count;
    }

    private void removeComment(String commentId, Widget widget) {
        Iterator<WidgetComment> iterator = widget.getComments().iterator();
        while (iterator.hasNext()) {
            WidgetComment comment = iterator.next();
            if (comment.getId().equals(commentId)) {
                iterator.remove();
                return;
            }
        }
    }

    private int updateWidget(String userId, Widget widget) {
        int count = 0;

        Iterator<WidgetComment> iterator = widget.getComments().iterator();
        while (iterator.hasNext()) {
            WidgetComment comment = iterator.next();
            if (comment.getUserId().equals(userId)) {
                iterator.remove();
                count++;
            }
        }
        if (count > 0) {
            template.save(widget);
        }
        return count;
    }

    private WidgetComment getCommentById(Widget widget, String id) {
        if (widget != null) {
            for (WidgetComment comment : widget.getComments()) {
                if (comment.getId().equals(id)) {
                    return comment;
                }
            }
        }
        return null;
    }

    private WidgetComment updateComment(Widget widget, WidgetComment item) {
        for (WidgetComment comment : widget.getComments()) {
            if (comment.getId().equals(item.getId())) {
                comment.setLastModifiedDate(new Date());
                comment.setText(item.getText());
                comment.setUserId(item.getUserId());
                return comment;
            }
        }
        return null;
    }

    /*
     * End WidgetComment Repository
     */
    private Query getWidgetStatusFreeTextQuery(WidgetStatus widgetStatus, String type, String searchTerm) {
        Criteria criteria = addFreeTextClause(searchTerm, new Criteria());
        if (type != null && !type.isEmpty()) {
            criteria.and("type").is(type);
        }
        if (widgetStatus != null) {
            criteria.and("widgetStatus").is(getWidgetStatusString(widgetStatus));
        }
        return query(criteria);
    }

    private Criteria addFreeTextClause(String searchTerm, Criteria criteria) {
        Pattern p = Pattern.compile(".*" + searchTerm + ".*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        criteria.orOperator(where("title").regex(p), (where("description").regex(p)));
        return criteria;
    }

    private Query getQueryByOwner(User owner) {
        return query(where("ownerId").is(owner.getId()));
    }

    private Query getTagQuery(String tagKeyWord) {
        Tag tag = getTag(tagKeyWord);
        return query(where("tags").elemMatch(where("tagId").is(tag.getId())));
    }

    private String getWidgetStatusString(WidgetStatus widgetStatus) {
        return widgetStatus.getWidgetStatus().toUpperCase();
    }

    private Query addSort(Query query) {
        query.sort().on("featured", Order.DESCENDING).on("title", Order.ASCENDING);
        return query;
    }

    private WidgetComment findCommentByProperties(Widget widget, WidgetComment comment) {
        for(WidgetComment c : widget.getComments()) {
            if(c.getUserId().equals(comment.getUserId()) &&
                    c.getText().equals(comment.getText()) &&
                    c.getCreatedDate().equals(comment.getCreatedDate())) {
                return c;
            }
        }
        return null;
    }

    public void setTemplate(MongoWidgetOperations template) {
        this.template = template;
    }

    public void setStatsAggregator(StatisticsAggregator statsAggregator) {
        this.statsAggregator = statsAggregator;
    }

    public void setTagTemplate(MongoTagOperations tagTemplate) {
        this.tagTemplate = tagTemplate;
    }
}
