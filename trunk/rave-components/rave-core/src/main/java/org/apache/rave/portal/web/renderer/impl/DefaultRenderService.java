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

package org.apache.rave.portal.web.renderer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.apache.rave.portal.web.renderer.RegionWidgetWrapperRenderer;
import org.apache.rave.portal.web.renderer.RenderService;
import org.apache.rave.portal.web.renderer.Renderer;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.Region;
import org.apache.rave.rest.model.RegionWidget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link RenderService}
 * <p/>
 * Retrieves a list of all {@link org.apache.rave.portal.web.renderer.Renderer} beans for supported operations
 * and delegates rendering operations to them
 */
@Service
public class DefaultRenderService implements RenderService {

    public static final String WIDGET_CONTEXT_KEY = "widget";

    private final Map<String, RegionWidgetWrapperRenderer> supportedWidgets;
    private final Map<String, RegionWidgetRenderer> supportedApiWidgets;
    private final WidgetRepository repository;

    @Autowired
    public DefaultRenderService(List<RegionWidgetWrapperRenderer> widgetRenderers,
                                List<RegionWidgetRenderer> apiWidgetRenderers,
                                WidgetRepository widgetRepository) {

        this.repository = widgetRepository;
        this.supportedWidgets = Maps.newHashMap();
        this.supportedApiWidgets = Maps.newHashMap();
        mapRenderersByType(this.supportedWidgets, widgetRenderers);
        mapRenderersByType(this.supportedApiWidgets, apiWidgetRenderers);
    }

    @Override
    public Collection<String> getSupportedWidgetTypes() {
        //TODO: change to API when refactor of rendering is complete
        return supportedWidgets.keySet();
    }

    /**
     * Renders the given rw iff there is a {@link org.apache.rave.portal.web.renderer.RegionWidgetWrapperRenderer } for the
     * rw type
     *
     * @param rw      RegionWidget to renderer
     * @param context
     * @return the String representation of the rendered RegionWidget
     * @throws {@link org.apache.rave.exception.NotSupportedException}
     */
    @Override
    public String render(RegionWidgetWrapper rw, RenderContext context) {
        RegionWidgetWrapperRenderer renderer = supportedWidgets.get(rw.getWidget().getType());
        if (renderer == null) {
            throw new NotSupportedException(rw.getWidget().getType() + " is not supported");
        }
        return renderer.render(rw, context);
    }

    @Override
    public RegionWidget prepareForRender(RegionWidget source) {
        if (source.getType() == null || source.getWidgetUrl() == null) {
            updateRegionWidget(source);
        }
        RegionWidgetRenderer renderer = supportedApiWidgets.get(source.getType());
        if (renderer == null) {
            throw new NotSupportedException(source.getType() + " is not supported");
        }
        return renderer.prepareForRender(source);
    }

    @Override
    public Page prepareForRender(Page source) {
        for (Region region : source.getRegions()) {
            List<RegionWidget> widgetList = Lists.newArrayListWithCapacity(region.getRegionWidgets().size());
            for (RegionWidget widget : region.getRegionWidgets()) {
                widgetList.add(prepareForRender(widget));
            }
            region.setRegionWidgets(widgetList);
        }
        if(source.getSubPages() != null) {
            for (Page sub : source.getSubPages()) {
                prepareForRender(sub);
            }
        }
        return source;
    }

    private static <T extends Renderer> void mapRenderersByType(Map<String, T> map, List<T> renderers) {
        if (renderers != null) {
            for (T renderer : renderers) {
                map.put(renderer.getSupportedContext(), renderer);
            }
        }
    }

    private void updateRegionWidget(RegionWidget source) {
        Widget widget = repository.get(source.getWidgetId());
        if (widget == null) {
            throw new IllegalArgumentException("Could not retrieve widget for RegionWidget " + source.getId());
        }
        source.setType(widget.getType());
        source.setWidgetUrl(widget.getUrl());
    }
}
