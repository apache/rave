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
import org.apache.rave.portal.model.MongoDbPageTemplate;
import org.apache.rave.model.PageTemplate;
import org.apache.rave.model.PageTemplateRegion;
import org.apache.rave.model.PageTemplateWidget;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.PageTemplateRegionImpl;
import org.apache.rave.portal.model.impl.PageTemplateWidgetImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbPageTemplateConverter implements HydratingModelConverter<PageTemplate, MongoDbPageTemplate> {

    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Override
    public void hydrate(MongoDbPageTemplate dehydrated) {
        if (dehydrated == null) {
            return;
        }
        dehydrated.setPageLayoutRepository(pageLayoutRepository);
        for (PageTemplateRegion region : dehydrated.getPageTemplateRegions()) {
            region.setPageTemplate(dehydrated);
            for (PageTemplateWidget widget : region.getPageTemplateWidgets()) {
                widget.setPageTemplateRegion(region);
            }
        }
        if (dehydrated.getSubPageTemplates() != null) {
            for (PageTemplate sub : dehydrated.getSubPageTemplates()) {
                sub.setParentPageTemplate(dehydrated);
                hydrate((MongoDbPageTemplate) sub);
            }
        }
    }

    @Override
    public Class<PageTemplate> getSourceType() {
        return PageTemplate.class;
    }

    @Override
    public MongoDbPageTemplate convert(PageTemplate source) {
        MongoDbPageTemplate converted = source instanceof MongoDbPageTemplate ? ((MongoDbPageTemplate) source) : new MongoDbPageTemplate();
        updateProperties(source, converted);

        if (source.getSubPageTemplates() != null) {
            List<PageTemplate> subPages = Lists.newArrayList();
            for (PageTemplate sub : source.getSubPageTemplates()) {
                subPages.add(convert(sub));
            }
            converted.setSubPageTemplates(subPages);
        }

        if (source.getPageTemplateRegions() != null) {
            List<PageTemplateRegion> convertedRegions = Lists.newArrayList();
            for (PageTemplateRegion region : source.getPageTemplateRegions()) {
                convertedRegions.add(convert(region));
            }
            converted.setPageTemplateRegions(convertedRegions);
        }
        return converted;
    }

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    public PageLayoutRepository getPageLayoutRepository() {
        return pageLayoutRepository;
    }

    private PageTemplateRegion convert(PageTemplateRegion region) {
        PageTemplateRegionImpl converted = region instanceof PageTemplateRegionImpl ? ((PageTemplateRegionImpl) region) : new PageTemplateRegionImpl();
        updateProperties(region, converted);

        if (region.getPageTemplateWidgets() != null) {
            List<PageTemplateWidget> convertedWidgets = Lists.newArrayList();
            for (PageTemplateWidget widget : region.getPageTemplateWidgets()) {
                convertedWidgets.add(convert(widget));
            }
            converted.setPageTemplateWidgets(convertedWidgets);
        }
        return converted;
    }

    private PageTemplateWidget convert(PageTemplateWidget widget) {
        PageTemplateWidgetImpl converted = widget instanceof PageTemplateWidgetImpl ? ((PageTemplateWidgetImpl) widget) : new PageTemplateWidgetImpl();
        updateProperties(widget, converted);
        return converted;
    }


    private void updateProperties(PageTemplateWidget source, PageTemplateWidgetImpl converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setHideChrome(source.isHideChrome());
        converted.setPageTemplateRegion(null);
        converted.setRenderSeq(source.getRenderSeq());
        converted.setWidgetId(source.getWidgetId());
        converted.setLocked(source.isLocked());
    }

    private void updateProperties(PageTemplateRegion source, PageTemplateRegionImpl converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setPageTemplate(null);
        converted.setLocked(source.isLocked());
    }

    private void updateProperties(PageTemplate source, MongoDbPageTemplate converted) {
        converted.setId(source.getId() == null ? generateId() : source.getId());
        converted.setName(source.getName());
        converted.setDescription(source.getDescription());

        //Enforce consistent casing for page types
        converted.setPageType(source.getPageType() == null ? null : source.getPageType().toUpperCase());
        converted.setParentPageTemplate(null);
        converted.setPageLayoutCode(source.getPageLayout().getCode());
        converted.setPageLayout(null);
        converted.setRenderSequence(source.getRenderSequence());
        converted.setDefaultTemplate(source.isDefaultTemplate());
    }


}
