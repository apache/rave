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
import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbWidgetCommentRepository implements WidgetCommentRepository {

    @Autowired
    private MongoWidgetOperations template;

    @Override
    public int deleteAll(Long userId) {
        int count=0;
        List<Widget> widgets = template.find(query(where("comments").elemMatch(where("userId").is(userId))));
        for(Widget widget : widgets) {
            count += updateWidget(userId, widget);
        }
        return count;
    }

    @Override
    public Class<? extends WidgetComment> getType() {
        return WidgetComment.class;
    }

    @Override
    public WidgetComment get(long id) {
        return getCommentById(getWidgetByCommentId(id), id);
    }

    @Override
    public WidgetComment save(WidgetComment item) {
        Widget widget = template.get(item.getWidgetId());
        if(item.getId() == null) {
            item.setId(generateId());
            widget.getComments().add(item);
        } else {
            updateComment(widget, item);
        }
        Widget saved = template.save(widget);
        return getCommentById(saved, item.getId());
    }

    @Override
    public void delete(WidgetComment item) {
        Widget widget = template.get(item.getWidgetId());
        removeComment(item.getId(), widget);
        template.save(widget);
    }

    private void updateComment(Widget widget, WidgetComment item) {
        for(WidgetComment comment : widget.getComments()) {
            if(comment.getId().equals(item.getId())) {
                comment.setLastModifiedDate(new Date());
                comment.setText(item.getText());
                comment.setUser(item.getUser());
                return;
            }
        }
    }

    private Widget getWidgetByCommentId(long id) {
        return template.findOne(query(where("comments").elemMatch(where("_id").is(id))));
    }

    private WidgetComment getCommentById(Widget widget, long id) {
        if(widget != null){
            for(WidgetComment comment : widget.getComments()) {
                if(comment.getId().equals(id)) {
                    return comment;
                }
            }
        }
        return null;
    }

    private int updateWidget(Long userId, Widget widget) {
        int count = 0;

        Iterator<WidgetComment> iterator = widget.getComments().iterator();
        while(iterator.hasNext()) {
            WidgetComment comment = iterator.next();
            if(comment.getUser().getId().equals(userId)) {
                iterator.remove();
                count++;
            }
        }
        if(count > 0) {
            template.save(widget);
        }
        return count;
    }

    private void removeComment(Long commentId, Widget widget) {
        Iterator<WidgetComment> iterator = widget.getComments().iterator();
        while(iterator.hasNext()) {
            WidgetComment comment = iterator.next();
            if(comment.getId().equals(commentId)) {
                iterator.remove();
                return;
            }
        }
    }

    public void setTemplate(MongoWidgetOperations template) {
        this.template = template;
    }

}
