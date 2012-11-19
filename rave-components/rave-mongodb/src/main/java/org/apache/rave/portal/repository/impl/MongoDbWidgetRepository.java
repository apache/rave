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
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.StatisticsAggregator;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        return statsAggregator.getWidgetStatistics(widget_id, user_id);
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        return statsAggregator.getAllWidgetStatistics(userId);
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long userId) {
        Query q = query(where("ratings").elemMatch(where("userId").is(userId)));
        List<Widget> widgets = template.find(q);
        Map<Long, WidgetRating> ratings = Maps.newHashMap();
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
    public int unassignWidgetOwner(long userId) {
        Query query = query(where("ownerId").is(userId));
        return template.update(query, update("ownerId", null));
    }

    @Override
    public Class<? extends Widget> getType() {
        return MongoDbWidget.class;
    }

    @Override
    public Widget get(long id) {
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
        return query(where("tags").elemMatch(where("tag.keyword").is(tagKeyWord)));
    }

    private String getWidgetStatusString(WidgetStatus widgetStatus) {
        return widgetStatus.getWidgetStatus().toUpperCase();
    }

    private Query addSort(Query query) {
        query.sort().on("featured", Order.DESCENDING).on("title", Order.ASCENDING);
        return query;
    }

}
