/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaWidget;
import org.apache.rave.model.Widget;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts from a {@link org.apache.rave.model.Widget} to a {@link org.apache.rave.portal.model.JpaWidget}
 */
@Component
public class JpaWidgetConverter implements ModelConverter<Widget, JpaWidget> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaWidget convert(Widget source) {
        return source instanceof JpaWidget ? (JpaWidget) source : createEntity(source);
    }

    @Override
    public Class<Widget> getSourceType() {
        return Widget.class;
    }

    private JpaWidget createEntity(Widget source) {
        JpaWidget converted = null;
        if (source != null) {
            converted = new JpaWidget();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(Widget source, JpaWidget converted) {
        converted.setEntityId(source.getId() == null ? null : Long.parseLong(source.getId()));
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
        converted.setOwnerId(source.getOwnerId());
        converted.setDisableRendering(source.isDisableRendering());
        converted.setRatings(source.getRatings());
        converted.setTags(source.getTags());
        converted.setCategories(source.getCategories());
        converted.setFeatured(source.isFeatured());
    }
}
