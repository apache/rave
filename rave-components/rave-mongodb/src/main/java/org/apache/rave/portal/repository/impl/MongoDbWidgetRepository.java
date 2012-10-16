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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.MongoModelOperations;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 */
@Repository
public class MongoDbWidgetRepository implements WidgetRepository {

    @Autowired
    private MongoModelOperations.MongoWidgetOperations template;

    @Override
    public List<Widget> getAll() {
        return template.find(new Query());
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return template.find(new Query().skip(offset).limit(pageSize));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query());
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        Query query = new Query(getFreeTextClause(searchTerm)).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return (int)template.count(new Query(getFreeTextClause(searchTerm)));
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        Query query = new Query(where("widgetStatus").is(widgetStatus)).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return (int)template.count(new Query(where("widgetStatus").is(widgetStatus)));
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        Query query = getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm).limit(pageSize).skip(offset);
        query.sort().on("title", Order.ASCENDING);
        return template.find(query);
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return (int)template.count(getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm));
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        Query query = getQueryByOwner(owner).skip(offset).limit(pageSize);
        return template.find(query);
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return (int)template.count(getQueryByOwner(owner));
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return template.findOne(new Query(where("url").is(widgetUrl)));
    }

    @Override
    public WidgetStatistics getWidgetStatistics(long widget_id, long user_id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Long, WidgetStatistics> getAllWidgetStatistics(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<Long, WidgetRating> getUsersWidgetRatings(long userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Widget> getWidgetsByTag(String tagKeyWord, int offset, int pageSize) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCountByTag(String tagKeyword) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int unassignWidgetOwner(long userId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
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
        return new Query(where("widgetStatus").is(widgetStatus)
                .andOperator(where("type").is(type))
                .andOperator(getFreeTextClause(searchTerm))
        );
    }

    private Criteria getFreeTextClause(String searchTerm) {
        String regex = "/" + searchTerm + "/";
        return where("title").is(regex).orOperator(where("title").is(regex));
    }

    private Query getQueryByOwner(User owner) {
        return new Query(where("ownerId").is(owner.getId()));
    }
}
