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

package org.apache.rave.provider.opensocial.web.renderer;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.model.*;
import org.apache.rave.portal.web.renderer.RegionWidgetWrapperRenderer;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates the appropriate markup to represent an OpenSocial widget
 * <p/>
 * //TODO RAVE-29: Create infrastructure for rendering inline gadgets via Caja
 */
@Component
public class OpenSocialWidgetWrapperRenderer implements RegionWidgetWrapperRenderer {
    private static Logger logger = LoggerFactory.getLogger(OpenSocialWidgetWrapperRenderer.class);

    public static final String REGISTER_WIDGET_KEY = "openSocialRegisterWidget";

    private OpenSocialService openSocialService;
    private SecurityTokenService securityTokenService;
    private ScriptManager scriptManager;

    @Autowired
    public OpenSocialWidgetWrapperRenderer(OpenSocialService openSocialService,
                                           SecurityTokenService securityTokenService,
                                           ScriptManager scriptManager) {
        this.openSocialService = openSocialService;
        this.securityTokenService = securityTokenService;
        this.scriptManager = scriptManager;
    }

    //Note the widgets.push() call.  This defines the widget objects, which are
    //added to the widgets[] array in home.jsp.
    private static final String SCRIPT_BLOCK =
            "<script>require(['rave'], function(rave){rave.registerWidget('%1$s', {type: '%2$s'," +
            " regionWidgetId: '%3$s'," +
            " widgetUrl: '%4$s', " +
            " securityToken: '%5$s', " +
            " metadata: %6$s," +
            " userPrefs: %7$s," +
            " collapsed: %8$s, " +
            " widgetId: '%9$s'," +
            " locked: %10$s," +
            " hideChrome: %11$s," +
            " subPage: {id: %12$s, name: '%13$s', isDefault: %14$s}" +
            "})});</script>";
    private static final String MARKUP = "<!-- RegionWidget '%1$s' placeholder -->";

    @Override
    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    /**
     * Renders a {@link org.apache.rave.model.RegionWidget} as HTML markup
     *
     *
     * @param item RegionWidget to render
     * @param context the context under which to render the gadget.
     * @return valid HTML markup
     */
    @Override
    public String render(RegionWidgetWrapper item, RenderContext context) {
        Widget widget = item.getWidget();
        String type = widget.getType();
        if (!Constants.WIDGET_TYPE.equals(type)) {
            throw new NotSupportedException("Invalid widget type passed to renderer: " + type);
        }

        RegionWidget regionWidget = item.getRegionWidget();
        String widgetScript = getWidgetScript(regionWidget, widget);
        // the key is based off the RegionWidget.id to ensure uniqueness
        String key = REGISTER_WIDGET_KEY  + (regionWidget.getId() == null ? "" :  "-" + regionWidget.getId());
        scriptManager.registerScriptBlock(key, widgetScript, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, context);
        logger.debug("Gadget Script Data from OpenSocialWidgetWrapperRenderer: " + widgetScript);

        return String.format(MARKUP, regionWidget.getId());
    }

    @Override
    public RegionWidgetWrapper prepareForRender(RegionWidgetWrapper item) {
        throw new UnsupportedOperationException();
    }

    private String getWidgetScript(RegionWidget item, Widget widget) {
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

        // get attributes about the sub page this regionWidget is on.  This is needed to assist the client in
        // determining which gadgets are on visible tabs/sub pages initially to make widget rendering more efficient
        String pageId = null;
        String pageName = "";
        boolean isDefault = false;
        Page page =  item.getRegion().getPage();
        if (page.getParentPage() != null) {
            pageId = "'" + page.getId() + "'";
            pageName = page.getName();
            // check to see if this regionWidget is on the first sub page, which will be the default
            // subpage rendered if the user doesn't specify which subpage via the URL hash
            isDefault = isDefaultSubPage(page);
        }

        return String.format(SCRIPT_BLOCK,
                item.getRegion().getId(),
                Constants.WIDGET_TYPE,
                item.getId(),
                widget.getUrl(),
                securityTokenService.getEncryptedSecurityToken(item, widget),
                openSocialService.getGadgetMetadata(widget.getUrl()),
                userPrefs.toString(),
                item.isCollapsed(),
                widget.getId(),
                item.isLocked(),
                item.isHideChrome(),
                pageId,
                pageName,
                isDefault
                );
    }

    private boolean isDefaultSubPage(Page subPage) {
        return subPage.getParentPage().getSubPages().get(0).getId().equals(subPage.getId());
    }
}
