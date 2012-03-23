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
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.RegionWidgetPreference;
import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
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
public class OpenSocialWidgetRenderer implements RegionWidgetRenderer {
    private static Logger logger = LoggerFactory.getLogger(OpenSocialWidgetRenderer.class);

    private OpenSocialService openSocialService;
    private SecurityTokenService securityTokenService;
    private ScriptManager scriptManager;

    @Autowired
    public OpenSocialWidgetRenderer(OpenSocialService openSocialService,
                                    SecurityTokenService securityTokenService,
                                    ScriptManager scriptManager) {
        this.openSocialService = openSocialService;
        this.securityTokenService = securityTokenService;
        this.scriptManager = scriptManager;
    }

    //Note the widgets.push() call.  This defines the widget objects, which are
    //added to the widgets[] array in home.jsp.
    private static final String SCRIPT_BLOCK =
            "<script>rave.registerWidget(widgetsByRegionIdMap, %1$s, {type: '%2$s'," +
            " regionWidgetId: %3$s," +
            " widgetUrl: '%4$s', " +
            " securityToken: '%5$s', " +
            " metadata: %6$s," +
            " userPrefs: %7$s," +
            " collapsed: %8$s, " +
            " widgetId: %9$s," +
            " locked: %10$s});</script>";
    private static final String MARKUP = "<!-- RegionWidget %1$s placeholder -->";

    @Override
    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    /**
     * Renders a {@link org.apache.rave.portal.model.RegionWidget} as HTML markup
     *
     *
     * @param item RegionWidget to render
     * @param context the context under which to render the gadget.
     * @return valid HTML markup
     */
    @Override
    public String render(RegionWidget item, RenderContext context) {
        String type = item.getWidget().getType();
        if (!Constants.WIDGET_TYPE.equals(type)) {
            throw new NotSupportedException("Invalid widget type passed to renderer: " + type);
        }

        String widgetScript = getWidgetScript(item);
        scriptManager.registerScriptBlock(widgetScript, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, context);
        logger.debug("Gadget Script Data: " + widgetScript);

        return String.format(MARKUP, item.getEntityId());
    }

    private String getWidgetScript(RegionWidget item) {
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

        return String.format(SCRIPT_BLOCK,
                item.getRegion().getEntityId(),
                Constants.WIDGET_TYPE,
                item.getEntityId(),
                item.getWidget().getUrl(),
                securityTokenService.getEncryptedSecurityToken(item),
                openSocialService.getGadgetMetadata(item.getWidget().getUrl()),
                userPrefs.toString(),
                item.isCollapsed(),
                item.getWidget().getEntityId(),
                item.isLocked());
    }
}