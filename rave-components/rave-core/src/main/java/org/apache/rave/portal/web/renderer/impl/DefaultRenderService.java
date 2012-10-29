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
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.portal.web.renderer.*;
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

    private final Map<String, RegionWidgetRenderer> supportedWidgets;

    private final WidgetRepository widgetRepository;

    @Autowired
    public DefaultRenderService(List<RegionWidgetRenderer> widgetRenderers, WidgetRepository widgetRepository) {
        this.supportedWidgets = new HashMap<String, RegionWidgetRenderer>();
        mapRenderersByType(this.supportedWidgets, widgetRenderers);
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Collection<String> getSupportedWidgetTypes() {
        return supportedWidgets.keySet();
    }

    /**
     * Renders the given rw iff there is a {@link org.apache.rave.portal.web.renderer.RegionWidgetRenderer } for the
     * rw type
     *
     * @param rw RegionWidget to renderer
     * @param context
     * @return the String representation of the rendered RegionWidget
     * @throws {@link org.apache.rave.exception.NotSupportedException}
     */
    @Override
    public String render(RegionWidget rw, RenderContext context) {
        Widget widget = widgetRepository.get(rw.getWidgetId());
        RegionWidgetRenderer renderer = supportedWidgets.get(widget.getType());
        if(renderer == null) {
            throw new NotSupportedException(widget.getType() + " is not supported");
        }
        return renderer.render(rw, context);
    }

    private static <T extends Renderer> void mapRenderersByType(Map<String, T> map, List<T> renderers) {
        for(T renderer : renderers) {
            map.put(renderer.getSupportedContext(), renderer);
        }
    }
}
