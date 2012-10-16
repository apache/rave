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
import org.apache.rave.portal.model.conversion.MongoDbConverter;
import org.apache.rave.portal.model.util.WidgetStatistics;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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
    public static final String COLLECTION = "widget";

    @Autowired
    private MongoOperations template;

    @Autowired
    private MongoDbConverter converter;

    @Override
    public List<Widget> getAll() {
        return null;
    }

    @Override
    public List<Widget> getLimitedList(int offset, int pageSize) {
        return hydrateWidgets(template.find(new Query().skip(offset).limit(pageSize), MongoDbWidget.class, COLLECTION));
    }

    @Override
    public int getCountAll() {
        return (int)template.count(new Query(), COLLECTION);
    }

    @Override
    public List<Widget> getByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        Query query = new Query(getFreeTextClause(searchTerm)).skip(offset).limit(pageSize);
        return hydrateWidgets(template.find(query, MongoDbWidget.class, COLLECTION));
    }

    @Override
    public int getCountFreeTextSearch(String searchTerm) {
        return 0;
    }

    @Override
    public List<Widget> getByStatus(WidgetStatus widgetStatus, int offset, int pageSize) {
        Query query = new Query(where("widgetStatus").is(widgetStatus)).skip(offset).limit(pageSize);
        query.sort().on("title", Order.ASCENDING);
        List<MongoDbWidget> widgets = template.find(query, MongoDbWidget.class, COLLECTION);
        return hydrateWidgets(widgets);
    }

    @Override
    public int getCountByStatus(WidgetStatus widgetStatus) {
        return (int)template.count(new Query(where("widgetStatus").is(widgetStatus)), COLLECTION);
    }

    @Override
    public List<Widget> getByStatusAndTypeAndFreeTextSearch(WidgetStatus widgetStatus, String type, String searchTerm, int offset, int pageSize) {
        Query query = getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm).limit(pageSize).skip(offset);
        query.sort().on("title", Order.ASCENDING);
        List<MongoDbWidget> widgets = template.find(query, MongoDbWidget.class, COLLECTION);
        return hydrateWidgets(widgets);
    }

    @Override
    public int getCountByStatusAndTypeAndFreeText(WidgetStatus widgetStatus, String type, String searchTerm) {
        return (int)template.count(getWidgetStatusFreeTextQuery(widgetStatus, type, searchTerm), COLLECTION);
    }

    @Override
    public List<Widget> getByOwner(User owner, int offset, int pageSize) {
        return null;
    }

    @Override
    public int getCountByOwner(User owner, int offset, int pageSize) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Widget getByUrl(String widgetUrl) {
        return hydrateWidget(template.findOne(new Query(where("url").is(widgetUrl)), MongoDbWidget.class, COLLECTION));
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
        return hydrateWidget(template.findById(id, MongoDbWidget.class, COLLECTION));
    }

    @Override
    public Widget save(Widget item) {
        MongoDbWidget converted = converter.convert(item, Widget.class);
        template.save(converted, COLLECTION);
        converter.hydrate(converted, Widget.class);
        return converted;
    }

    @Override
    public void delete(Widget item) {
        template.remove(template.findById(item.getId(), MongoDbWidget.class, COLLECTION));
    }

    private List<Widget> hydrateWidgets(List<MongoDbWidget> widgets) {
        for(MongoDbWidget widget : widgets) {
            converter.hydrate(widget, Widget.class);
        }
        return CollectionUtils.<Widget>toBaseTypedList(widgets);
    }

    private Widget hydrateWidget(MongoDbWidget widget) {
        converter.hydrate(widget, Widget.class);
        return widget;
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
}
