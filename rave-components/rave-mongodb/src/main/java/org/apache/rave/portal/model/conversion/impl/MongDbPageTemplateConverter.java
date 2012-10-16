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

import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.MongoDbPageTemplateWidget;
import org.apache.rave.portal.model.PageTemplate;
import org.apache.rave.portal.model.PageTemplateRegion;
import org.apache.rave.portal.model.PageTemplateWidget;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongDbPageTemplateConverter implements HydratingModelConverter<PageTemplate, PageTemplateImpl> {
    @Autowired
    private WidgetRepository widgetRepository;

    @Override
    public void hydrate(PageTemplateImpl dehydrated) {
        for(PageTemplateRegion region : dehydrated.getPageTemplateRegions()) {
            region.setPageTemplate(dehydrated);
            for(PageTemplateWidget widget : region.getPageTemplateWidgets()) {
                ((MongoDbPageTemplateWidget)widget).setWidgetRepository(widgetRepository);
                widget.setPageTemplateRegion(region);
            }
        }
    }

    @Override
    public Class<PageTemplate> getSourceType() {
        return PageTemplate.class;
    }

    @Override
    public PageTemplateImpl convert(PageTemplate source) {
        throw new NotImplementedException("Page Templates are currently not saved by the Portal");
    }
}
