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

package org.apache.rave.portal.model.conversion;

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.CategoryRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

/**
 */
@Component
public class MongoDbWidgetConverter implements HydratingModelConverter<Widget, MongoDbWidget> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Class<Widget> getSourceType() {
        return Widget.class;
    }

    @Override
    public void hydrate(MongoDbWidget dehydrated) {
        dehydrated.setCategoryRepository(categoryRepository);
        dehydrated.setUserRepository(userRepository);
        if(dehydrated.getComments() != null) {
            hydrateComments(dehydrated);
        }

        if(dehydrated.getTags() != null) {
            hydrateTags(dehydrated);
        }
    }

    private void hydrateTags(MongoDbWidget dehydrated) {
        for (WidgetTag tag : dehydrated.getTags()) {
            if (tag instanceof MongoDbWidgetTag) {
                ((MongoDbWidgetTag) tag).setUserRepository(userRepository);
            }
        }
    }

    private void hydrateComments(MongoDbWidget dehydrated) {
        for (WidgetComment comment : dehydrated.getComments()) {
            if (comment instanceof MongoDbWidgetComment) {
                ((MongoDbWidgetComment) comment).setUserRepository(userRepository);
            }
        }
    }

    @Override
    public MongoDbWidget convert(Widget source) {
        MongoDbWidget widget = source instanceof MongoDbWidget ? (MongoDbWidget) source : new MongoDbWidget();
        updateProperties(source, widget);
        widget.setOwnerId(source.getOwner() != null ? source.getOwner().getId() : null);
        widget.setOwner(null);
        widget.setUserRepository(null);

        if (source.getCategories() != null) {
            convertCategories(source, widget);
        }
        if (source.getComments() != null) {
            convertComments(source, widget);
        }
        if(source.getTags() != null) {
            convertTags(source, widget);
        }
        if(source.getRatings() != null) {
            convertRatings(source, widget);
        }
        return widget;
    }

    private void convertRatings(Widget source, MongoDbWidget widget) {
        List<WidgetRating> ratings = source.getRatings();
        for(WidgetRating rating : ratings) {
            rating.setWidgetId(widget.getId());
            if(rating.getId() == null) {
                rating.setId(generateId());
            }
        }
        widget.setRatings(ratings);
    }

    private void convertTags(Widget source, MongoDbWidget widget) {
        List<WidgetTag> convertedTags = Lists.newArrayList();
        for (WidgetTag tag : source.getTags()) {
            convertedTags.add(convert(tag, widget));
        }
        widget.setTags(convertedTags);
    }

    private void convertComments(Widget source, MongoDbWidget widget) {
        List<WidgetComment> convertedComments = Lists.newArrayList();
        for (WidgetComment comment : source.getComments()) {
            convertedComments.add(convert(comment, widget));
        }
        widget.setComments(convertedComments);
    }

    private void convertCategories(Widget source, MongoDbWidget converted) {
        List<Long> categoryIds = Lists.<Long>newArrayList();
        for (Category category : source.getCategories()) {
            categoryIds.add(category.getId());
        }
        converted.setCategoryIds(categoryIds);
        converted.setCategories(null);
        converted.setCategoryRepository(null);
    }

    private MongoDbWidgetTag convert(WidgetTag tag, Widget widget) {
        MongoDbWidgetTag converted = tag instanceof MongoDbWidgetTag ? ((MongoDbWidgetTag) tag) : new MongoDbWidgetTag();

        converted.setUserId(tag.getUser().getId());
        converted.setUser(null);
        converted.setUserRepository(null);
        converted.setTag(tag.getTag());
        converted.setCreatedDate(tag.getCreatedDate());
        converted.setWidgetId(widget.getId());
        return converted;
    }

    private MongoDbWidgetComment convert(WidgetComment comment, Widget widget) {
        MongoDbWidgetComment converted = comment instanceof MongoDbWidgetComment ? ((MongoDbWidgetComment) comment) : new MongoDbWidgetComment();
        converted.setUserId(comment.getUser().getId());
        converted.setUser(null);
        converted.setId(comment.getId() == null ? generateId() : comment.getId());
        converted.setUserRepository(null);

        converted.setCreatedDate(comment.getCreatedDate());
        converted.setLastModifiedDate(comment.getLastModifiedDate());
        converted.setText(comment.getText());
        converted.setWidgetId(widget.getId());
        return converted;
    }

    private void updateProperties(Widget source, MongoDbWidget converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setUrl(source.getUrl());
        converted.setType(source.getType());
        converted.setTitle(source.getTitle());
        converted.setTitleUrl(source.getTitleUrl());
        converted.setUrl(source.getUrl());
        converted.setThumbnailUrl(source.getThumbnailUrl());
        converted.setScreenshotUrl(source.getScreenshotUrl());
        converted.setAuthor(source.getAuthor());
        converted.setAuthorEmail(source.getAuthorEmail());
        converted.setDescription(source.getDescription());
        converted.setWidgetStatus(source.getWidgetStatus());
        converted.setComments(source.getComments());
        converted.setDisableRendering(source.isDisableRendering());
        converted.setFeatured(source.isFeatured());
    }
}
