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

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.portal.web.renderer.*;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.RegionWidget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    public DefaultRenderService(List<RegionWidgetWrapperRenderer> widgetRenderers, WidgetRepository widgetRepository) {
        this.supportedWidgets = new HashMap<String, RegionWidgetWrapperRenderer>();
        mapRenderersByType(this.supportedWidgets, widgetRenderers);
    }

    @Override
    public Collection<String> getSupportedWidgetTypes() {
        return supportedWidgets.keySet();
    }

    /**
     * Renders the given rw iff there is a {@link org.apache.rave.portal.web.renderer.RegionWidgetWrapperRenderer } for the
     * rw type
     *
     * @param rw RegionWidget to renderer
     * @param context
     * @return the String representation of the rendered RegionWidget
     * @throws {@link org.apache.rave.exception.NotSupportedException}
     */
    @Override
    public String render(RegionWidgetWrapper rw, RenderContext context) {
        RegionWidgetWrapperRenderer renderer = supportedWidgets.get(rw.getWidget().getType());
        if(renderer == null) {
            throw new NotSupportedException(rw.getWidget().getType() + " is not supported");
        }
        return renderer.render(rw, context);
    }

    @Override
    public RegionWidget prepareForRender(RegionWidget source) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Page prepareForRender(Page source) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static <T extends Renderer> void mapRenderersByType(Map<String, T> map, List<T> renderers) {
        for(T renderer : renderers) {
            map.put(renderer.getSupportedContext(), renderer);
        }
    }
}
