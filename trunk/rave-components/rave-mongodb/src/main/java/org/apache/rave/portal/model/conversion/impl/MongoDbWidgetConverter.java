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

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.WidgetCommentImpl;
import org.apache.rave.portal.model.impl.WidgetRatingImpl;
import org.apache.rave.portal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

/**
 */
@Component
public class MongoDbWidgetConverter implements HydratingModelConverter<Widget, MongoDbWidget> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Class<Widget> getSourceType() {
        return Widget.class;
    }

    @Override
    public void hydrate(MongoDbWidget dehydrated) {
        if(dehydrated == null) {
            return;
        }
        dehydrated.setCategoryRepository(categoryRepository);
    }

    @Override
    public MongoDbWidget convert(Widget source) {
        MongoDbWidget widget = new MongoDbWidget();
        updateProperties(source, widget);

        if (source.getCategories() != null) {
            convertCategories(source, widget);
        }

        if (source.getComments() == null) {
            widget.setComments(Lists.<WidgetComment>newArrayList());
        } else {
            convertComments(source, widget);
        }

        List<WidgetTag> tags = source.getTags() == null ? Lists.<WidgetTag>newArrayList() : source.getTags();
        widget.setTags(tags);

        if (source.getRatings() == null) {
            widget.setRatings(Lists.<WidgetRating>newArrayList());
        } else {
            convertRatings(source, widget);
        }
        return widget;
    }

    private void convertRatings(Widget source, MongoDbWidget widget) {
        List<WidgetRating> ratings = source.getRatings();
        List<WidgetRating> converted = Lists.newArrayList();
        for(WidgetRating rating : ratings) {
            String id = rating.getId() == null ? generateId() : rating.getId();
            converted.add(new WidgetRatingImpl(id, rating.getUserId(), rating.getScore()));
        }
        widget.setRatings(converted);
    }


    private void convertComments(Widget source, MongoDbWidget widget) {
        List<WidgetComment> convertedComments = Lists.newArrayList();
        for (WidgetComment comment : source.getComments()) {
            convertedComments.add(convert(comment, widget));
        }
        widget.setComments(convertedComments);
    }


    private WidgetCommentImpl convert(WidgetComment comment, Widget widget) {
        WidgetCommentImpl converted = comment instanceof WidgetCommentImpl ? ((WidgetCommentImpl) comment) : new WidgetCommentImpl();
        converted.setUserId(comment.getUserId());
        converted.setId(comment.getId() == null ? generateId() : comment.getId());

        converted.setCreatedDate(comment.getCreatedDate());
        converted.setLastModifiedDate(comment.getLastModifiedDate());
        converted.setText(comment.getText());
        return converted;
    }

    private void convertCategories(Widget source, MongoDbWidget converted) {
        List<String> categoryIds = Lists.<String>newArrayList();
        for (Category category : source.getCategories()) {
            categoryIds.add(category.getId());
        }
        converted.setCategoryIds(categoryIds);
        converted.setCategories(null);
        converted.setCategoryRepository(null);
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
        converted.setDisableRendering(source.isDisableRendering());
        converted.setDisableRenderingMessage(source.getDisableRenderingMessage());
        converted.setFeatured(source.isFeatured());
        converted.setOwnerId(source.getOwnerId());
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
}
