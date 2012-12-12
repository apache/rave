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


import com.google.common.collect.Lists;
import org.apache.commons.collections.ListUtils;
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.repository.MongoWidgetOperations;
import org.apache.rave.portal.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbTagRepository implements TagRepository {

    @Autowired
    private MongoWidgetOperations widgetTemplate;

    @Override
    public List<Tag> getAll() {
        List<Widget> widgets = widgetTemplate.find(new Query());
        List<Tag> tags = Lists.newArrayList();
        for (Widget widget : widgets) {
            addUniqueTags(tags, widget);
        }
        return tags;
    }

    @Override
    public int getCountAll() {
        return getAll().size();
    }

    @Override
    public Tag getByKeyword(String keyword) {
        return new TagImpl(keyword);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> getAvailableTagsByWidgetId(Long widgetId) {
        List<Tag> all = getAll();
        List<Tag> widgetTags = getTagsFromWidget(widgetTemplate.get(widgetId).getTags());
        return ListUtils.subtract(all, widgetTags);
    }

    @Override
    public Class<? extends Tag> getType() {
        return Tag.class;
    }

    @Override
    public Tag get(long id) {
        throw new NotSupportedException("Cannot access tags by Id");
    }

    @Override
    public Tag save(Tag item) {
        throw new NotSupportedException("Cannot save tags directly");
    }

    @Override
    public void delete(Tag item) {
        throw new NotSupportedException("Cannot delete tags directly");
    }

    private List<Tag> getTagsFromWidget(List<WidgetTag> widgetTags) {
        List<Tag> tags = Lists.newArrayList();
        if (widgetTags != null) {
            for (WidgetTag widgetTag : widgetTags) {
                tags.add(widgetTag.getTag());
            }
        }
        return tags;
    }


    private void addUniqueTags(List<Tag> tags, Widget widget) {
        //returns if there are no tags for this widget to prevent null pointer exception
        if (widget.getTags() == null) return;

        for (WidgetTag widgetTag : widget.getTags()) {
            Tag tag = widgetTag.getTag();
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }
    }

    public void setWidgetTemplate(MongoWidgetOperations widgetTemplate) {
        this.widgetTemplate = widgetTemplate;
    }
}
