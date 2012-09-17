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

package org.apache.rave.provider.w3c.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.wookie.connector.framework.WidgetInstance;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WookieWidgetService implements WidgetProviderService {
  private static Logger logger = LoggerFactory.getLogger(WookieWidgetService.class);

    private final String wookieServerUrl; // = "http://localhost:8080/wookie";
    private final String wookieApiKey; // = "TEST";
    private final String adminUsername;
    private final String adminPassword;
    private static WookieConnectorService connectorService;

    public WookieWidgetService(String wookieServerUrl, String wookieApiKey, String adminUsername, String adminPassword){
        this.wookieServerUrl = wookieServerUrl;
        this.wookieApiKey = wookieApiKey;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetProviderService#getWidget(org.apache.rave.portal.model.User)
     */
    @Override
    public Widget getWidget(User viewer, String sharedDataKey, Widget widget) {
        if (widget.getType().equals("W3C")) {
          return getWidgetForViewer(widget, sharedDataKey, viewer);
        } else {
          return null;
        }
    }
    
    /**
     * Get all widgets available from the configured Wookie server
     * @return an array of available widgets
     * @throws WookieConnectorException
     */
    @SuppressWarnings("deprecation")
    public Widget[] getWidgets() throws WookieConnectorException{
        connectorService = getWookieConnectorService(wookieServerUrl, wookieApiKey, null);    
        Collection<org.apache.wookie.connector.framework.Widget> widgets = connectorService.getAvailableWidgets().values();
        ArrayList<Widget> raveWidgets = new ArrayList<Widget>();
        for (org.apache.wookie.connector.framework.Widget wookieWidget: widgets){
            Widget widget = new W3CWidget();
            widget.setUrl(wookieWidget.getIdentifier());
            widget.setDescription(wookieWidget.getDescription());
            widget.setTitle(wookieWidget.getTitle());
            widget.setThumbnailUrl(wookieWidget.getIcon().toString());
            //widget.setAuthor(wookieWidget.getAuthor());
            raveWidgets.add(widget);
        }
        return raveWidgets.toArray(new Widget[raveWidgets.size()]);
    }
    
    public Widget getWidget(String url) throws WookieConnectorException{
        for (Widget widget: getWidgets()){
            if (widget.getUrl().equalsIgnoreCase(url)){
                return widget;
            }
        }
        return null;
    }
    
    @SuppressWarnings("deprecation")
    public Widget publishWidgetUrlToWookie(String widgetUrl){
        Widget widget = null;
        try {
            if(adminUsername.equals(null) || adminUsername.equals("") || adminPassword.equals(null) || adminPassword.equals("")){
                throw new WookieConnectorException("Either the wookie username or password is not defined in portal.properties", null);
            }
            connectorService = getWookieConnectorService(wookieServerUrl, wookieApiKey, "");
            // TODO - replace dummy line below with the commented out version, when bundled with wookie 0.13.0
            //org.apache.wookie.connector.framework.Widget wookieWidget = connectorService.postWidget(widgetUrl, adminUsername, adminPassword);
            org.apache.wookie.connector.framework.Widget wookieWidget = new org.apache.wookie.connector.framework.Widget(widgetUrl, widgetUrl, widgetUrl, null, widgetUrl, widgetUrl, widgetUrl, widgetUrl, widgetUrl);
            widget = new W3CWidget();
            widget.setUrl(wookieWidget.getIdentifier());
            widget.setDescription(wookieWidget.getDescription());
            widget.setTitle(wookieWidget.getTitle());
            widget.setType("W3C");
            widget.setThumbnailUrl(wookieWidget.getIcon().toString());
        } catch (WookieConnectorException e) {
            logger.error(e.getMessage());
        }
        return widget;
    }

    /**
     * Gets the Widget Instance corresponding to the RegionWidget and the Viewer
     * @param widget the type of Widget to obtain
     * @param sharedDataKey the context for data sharing
     * @param viewer the current viewer
     * @return a Widget
     */
    @SuppressWarnings("deprecation")
    private W3CWidget getWidgetForViewer(Widget widget, String sharedDataKey, User viewer){
       try {
            // TODO: parameters for WookieConnectorService should not be fixed in code.
            connectorService = getWookieConnectorService(wookieServerUrl, wookieApiKey, sharedDataKey);
            org.apache.wookie.connector.framework.User user = new org.apache.wookie.connector.framework.User(String.valueOf(viewer.getUsername()), viewer.getUsername());
            connectorService.setCurrentUser(user);
            logger.debug("Getting widget:"+widget.getUrl()+" from:" +connectorService.getConnection().getURL());
            WidgetInstance instance = connectorService.getOrCreateInstance(widget.getUrl());
            return createWidget(instance);
        } catch (WookieConnectorException e) {
            logger.error("Unable to connect to Wookie server", e);
            // FIXME: provide a real error widget
            return createWidget(new WidgetInstance("error", "error", e.getMessage(), "100", "100"));
        } catch (IOException e) {
            logger.error("Problem communicating with Wookie server", e);
            // FIXME: provide a real error widget
            return createWidget(new WidgetInstance("error", "error", e.getMessage(), "100", "100"));
        }
    }

    /**
     * Create a Rave Widget object for the widget instance.
     * This is a transient object and is not persisted
     * @return
     */
    private W3CWidget createWidget(WidgetInstance instance){
        W3CWidget widget = new W3CWidget();
        widget.setUrl(instance.getUrl());
        widget.setTitle(instance.getTitle());
        widget.setType("W3C");
        widget.setHeight(Integer.parseInt(instance.getHeight()));
        widget.setWidth(Integer.parseInt(instance.getWidth()));
        return widget;
    }

    // Get the wookie service connector
    private WookieConnectorService getWookieConnectorService(String serverURL, String apiKey, String sharedDataKey ) throws WookieConnectorException {
      connectorService = new WookieConnectorService(serverURL, apiKey, sharedDataKey);
      return connectorService;
    }

}
