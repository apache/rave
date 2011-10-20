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

package org.apache.rave.provider.w3c.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.apache.rave.provider.w3c.Constants.WIDGET_TYPE;

/**
 * Creates the appropriate markup to represent a W3C widget
 */
@Component
public class W3cWidgetRenderer implements RegionWidgetRenderer {
    private static Logger logger = LoggerFactory.getLogger(W3cWidgetRenderer.class);

    private final WidgetProviderService widgetService;
    private final UserService userService;
    private ScriptManager scriptManager;

    @Autowired
    public W3cWidgetRenderer(@Qualifier("wookieWidgetService") WidgetProviderService widgetService,
                             UserService userService, ScriptManager scriptManager) {
        this.widgetService = widgetService;
        this.userService = userService;
        this.scriptManager = scriptManager;
    }
    
    private static final String SCRIPT_BLOCK =
        "<script>rave.registerWidget(widgetsByRegionIdMap, %1$s, {type: '%2$s'," +
        " regionWidgetId: %3$s," +
        " widgetUrl: '%4$s', " +
        " collapsed: %5$s});</script>";
    private static final String MARKUP = "<!-- RegionWidget %1$s placeholder -->";


    @Override
    public String getSupportedContext() {
        return WIDGET_TYPE;
    }

    /**
     * Renders a {@link org.apache.rave.portal.model.RegionWidget} as HTML markup
     *
     * @param item RegionWidget to render
     * @param context
     * @return valid HTML markup
     */
    @Override
    public String render(RegionWidget item, RenderContext context) {
        Widget widget = item.getWidget();
        if(!WIDGET_TYPE.equals(widget.getType())) {
            throw new NotSupportedException("Invalid widget type passed to renderer: " + widget.getType());
        }
        
        String widgetScript = getWidgetScript(item);
        scriptManager.registerScriptBlock(widgetScript, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, context);
        logger.debug("Gadget Script Data: " + widgetScript);

        return String.format(MARKUP, item.getEntityId());
    }
    
    private String getWidgetScript(RegionWidget item) {
        User user = userService.getAuthenticatedUser();
        Widget contextualizedWidget = widgetService.getWidget(user, null, item.getWidget());
        
        JSONObject userPrefs = new JSONObject();
        if (item.getPreferences() != null) {
            for (RegionWidgetPreference regionWidgetPreference : item.getPreferences()) {
                try {
                    userPrefs.put(regionWidgetPreference.getName(), regionWidgetPreference.getValue());
                } catch (JSONException e) {
                    logger.error("Exception caught adding preference to JSONObject: " + regionWidgetPreference, e);
                }
            }
        }

        return String.format(SCRIPT_BLOCK, item.getRegion().getEntityId(), WIDGET_TYPE, item.getEntityId(),
                contextualizedWidget.getUrl(), item.isCollapsed());
    }
}
