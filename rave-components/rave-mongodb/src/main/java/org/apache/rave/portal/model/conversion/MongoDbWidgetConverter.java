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
import org.apache.rave.portal.repository.TagRepository;
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
    
    @Autowired
    private TagRepository tagRepository;

    @Override
    public void hydrate(MongoDbWidget dehydrated) {
        dehydrated.setCategoryRepository(categoryRepository);
        dehydrated.setUserRepository(userRepository);
        
        for(WidgetComment comment: dehydrated.getComments()) {
            if(comment instanceof MongoDbWidgetComment) {
                ((MongoDbWidgetComment)comment).setUserRepository(userRepository);
            }
        }        
        
        for(WidgetTag tag: dehydrated.getTags()) {
            if(tag instanceof MongoDbWidgetTag) {
                ((MongoDbWidgetTag)tag).setUserRepository(userRepository);
                ((MongoDbWidgetTag)tag).setTagRepository(tagRepository);
            }
        }
    }

    @Override
    public MongoDbWidget convert(Widget source) {
        MongoDbWidget widget = source instanceof MongoDbWidget ? (MongoDbWidget)source : new MongoDbWidget();
        updateProperties(source, widget);
        widget.setOwnerId(source.getOwner().getId());
        widget.setOwner(null);
        widget.setUserRepository(null);

        List<Long> categoryIds = Lists.<Long>newArrayList();
        for(Category category : source.getCategories()) {
            widget.getCategoryIds().add(category.getId());
        }
        widget.setCategoryIds(categoryIds);
        widget.setCategories(null);
        widget.setCategoryRepository(null);

        List<WidgetComment> convertedComments = Lists.newArrayList();
        for(WidgetComment comment : source.getComments()) {
            convertedComments.add(convert(comment, widget));
        }
        widget.setComments(convertedComments);

        List<WidgetTag> convertedTags = Lists.newArrayList();
        for(WidgetTag tag : source.getTags()) {
            convertedTags.add(convert(tag, widget));
        }
        widget.setTags(convertedTags);

        return widget;
    }

    private MongoDbWidgetTag convert(WidgetTag tag, Widget widget) {
        MongoDbWidgetTag converted = tag instanceof MongoDbWidgetTag ? ((MongoDbWidgetTag)tag) : new MongoDbWidgetTag();

        converted.setUserId(tag.getUser().getId());
        converted.setUser(null);
        converted.setTagKeyword(tag.getTag().getKeyword());
        converted.setTag(null);
        converted.setUserRepository(null);
        converted.setTagRepository(null);

        converted.setCreatedDate(tag.getCreatedDate());
        converted.setWidgetId(widget.getId());
        return converted;
    }

    private MongoDbWidgetComment convert(WidgetComment comment, Widget widget) {
        MongoDbWidgetComment converted = comment instanceof MongoDbWidgetComment ? ((MongoDbWidgetComment)comment) : new MongoDbWidgetComment();
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

    @Override
    public Class<Widget> getSourceType() {
        return Widget.class;
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
        converted.setRatings(source.getRatings());
        converted.setFeatured(source.isFeatured());
    }
}
