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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 */
@Repository
public class MongoDbWidgetTagRepository implements WidgetTagRepository {
    @Autowired
    private MongoWidgetOperations template;

    @Override
    public WidgetTag getByWidgetIdAndTag(Long widgetId, String keyword) {
        Widget widget = template.get(widgetId);
        return getTagByKeyword(keyword, widget);
    }

    @Override
    public Class<? extends WidgetTag> getType() {
        return WidgetTag.class;
    }

    @Override
    public WidgetTag get(long id) {
        throw new NotSupportedException();
    }

    @Override
    public WidgetTag save(WidgetTag item) {
        Widget widget = template.get(item.getWidgetId());
        updateOrAddTag(widget, item);
        Widget saved = template.save(widget);
        return getTagByKeyword(item.getTag().getKeyword(), saved);
    }

    private void updateOrAddTag(Widget widget, WidgetTag item) {
        //The current programming model expects there to be only one instance of a tag
        //consider an update a NOOP unless it is a new tag.
        WidgetTag tag = getTagByKeyword(item.getTag().getKeyword(), widget);
        if(tag == null) {
            widget.getTags().add(item);
        }
    }

    @Override
    public void delete(WidgetTag item) {
        Widget widget = template.get(item.getWidgetId());
        removeTag(item.getTag().getKeyword(), widget);
    }

    private void removeTag(String keyword, Widget widget) {
        Iterator<WidgetTag> iterator = widget.getTags().iterator();
        while (iterator.hasNext()) {
            WidgetTag comment = iterator.next();
            if (comment.getTag().getKeyword().equals(keyword)) {
                iterator.remove();
                return;
            }
        }
    }

    private WidgetTag getTagByKeyword(String keyword, Widget widget) {
        for(WidgetTag tag : widget.getTags()) {
            if(tag.getTag().getKeyword().equals(keyword)) {
                return tag;
            }
        }
        return null;
    }

    public void setTemplate(MongoWidgetOperations template) {
        this.template = template;
    }
}
