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
import org.apache.rave.model.*;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.portal.web.renderer.RegionWidgetWrapperRenderer;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.w3c.service.impl.W3CWidget;
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
public class W3cWidgetWrapperRenderer implements RegionWidgetWrapperRenderer {
    private static Logger logger = LoggerFactory.getLogger(W3cWidgetWrapperRenderer.class);
    
    private static final String REGISTER_WIDGET_KEY = "wookieRegisterWidget";

    private final WidgetProviderService widgetService;
    private final UserService userService;
    private ScriptManager scriptManager;

    @Autowired
    public W3cWidgetWrapperRenderer(@Qualifier("wookieWidgetService") WidgetProviderService widgetService,
                                    UserService userService, ScriptManager scriptManager) {
        this.widgetService = widgetService;
        this.userService = userService;
        this.scriptManager = scriptManager;
    }

    /**
     * The script block template
     */
    private static final String SCRIPT_BLOCK =
        "<script>require(['rave'], function(rave){rave.registerWidget('%1$s', {type: '%2$s'," +
        " regionWidgetId: %3$s," +
        " widgetUrl: '%4$s', " +
        " height: '%5$s', " +
        " width: '%6$s', " +
        " collapsed: %7$s, " +
        " widgetId: %8$s, " +
        " locked: %9$s, " +
        " hideChrome: %10$s, " +
        " subPage: {id: %11$s, name: '%12$s', isDefault: %13$s}" +
        "})});</script>";
    private static final String MARKUP = "<!-- RegionWidget %1$s placeholder -->";


    @Override
    public String getSupportedContext() {
        return WIDGET_TYPE;
    }

    /**
     * Renders a {@link org.apache.rave.model.RegionWidget} as HTML markup
     *
     * @param wrapper RegionWidgetImpl to render
     * @param context
     * @return valid HTML markup
     */
    @Override
    public String render(RegionWidgetWrapper wrapper, RenderContext context) {
        Widget widget = wrapper.getWidget();
        RegionWidget item = wrapper.getRegionWidget();
        if(!WIDGET_TYPE.equals(widget.getType())) {
            throw new NotSupportedException("Invalid widget type passed to renderer: " + widget.getType());
        }

        String widgetScript = getWidgetScript(item, widget);
        // the key is based off the RegionWidget.id to ensure uniqueness
        String key = REGISTER_WIDGET_KEY + "-" + item.getId();
        scriptManager.registerScriptBlock(key, widgetScript, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, context);
        logger.debug("Gadget Script Data: " + widgetScript);

        return String.format(MARKUP, item.getId());
    }

    @Override
    public RegionWidgetWrapper prepareForRender(RegionWidgetWrapper item) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a widget script block
     *
     * @param item the RegionWidget to create a script block for
     * @param widget
     * @return the script block
     */
    private String getWidgetScript(RegionWidget item, Widget widget) {
        User user = userService.getAuthenticatedUser();

        //
        // For the shared data key we use the RegionWidget entity ID.
        //
        String sharedDataKey = String.valueOf(item.getId());

        //
        // Get the Rave Widget for this regionWidget instance
        //
        W3CWidget contextualizedWidget = (W3CWidget) widgetService.getWidget(user, sharedDataKey, widget);

        //
        // TODO make this do something useful; currently these preferences aren't
        // actually available in the Widget Instance as prefs are managed separately in Wookie
        //
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

        //
        // Use width and height attributes if available, otherwise set to "100%"
        //
        String width = "100%";
        String height = "100%";
        if (contextualizedWidget.getWidth() > 0)
           width = String.valueOf(contextualizedWidget.getWidth()) + "px";
        if (contextualizedWidget.getHeight() > 0)
        	height = String.valueOf(contextualizedWidget.getHeight()) + "px";

        // get attributes about the sub page this regionWidget is on.  This is needed to assist the client in
        // determining which gadgets are on visible tabs/sub pages initially to make widget rendering more efficient
        String pageId = null;
        String pageName = "";
        boolean isDefault = false;
        Page page =  item.getRegion().getPage();
        if (PageType.SUB_PAGE.equals(page.getPageType())) {
            pageId = page.getId();
            pageName = page.getName();
            // check to see if this regionWidget is on the first sub page, which will be the default
            // subpage rendered if the user doesn't specify which subpage via the URL hash
            isDefault = isDefaultSubPage(page);
        }

        //
        // Construct and return script block
        //
        return String.format(SCRIPT_BLOCK,
                item.getRegion().getId(),
                WIDGET_TYPE,
                item.getId(),
                contextualizedWidget.getUrl(),
                height,
                width,
                item.isCollapsed(),
                item.getWidgetId(),
                item.isLocked(),
                item.isHideChrome(),
                pageId,
                pageName,
                isDefault);
    }

    private boolean isDefaultSubPage(Page subPage) {
        return subPage.getParentPage().getSubPages().get(0).getId().equals(subPage.getId());
    }
}
