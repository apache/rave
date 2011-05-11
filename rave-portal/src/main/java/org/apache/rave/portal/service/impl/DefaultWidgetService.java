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
package org.apache.rave.portal.service.impl;

import java.io.IOException;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetService;
//import org.apache.wookie.connector.framework.WidgetInstance;
//import org.apache.wookie.connector.framework.WookieConnectorException;
//import org.apache.wookie.connector.framework.WookieConnectorService;
import org.springframework.stereotype.Service;

@Service
public class DefaultWidgetService implements WidgetService {
	
	// TODO uncomment when Wookie Connector is available in Maven
    
    private static final String WOOKIE_SERVER_URL = "http://localhost:8080/wookie";
    private static final String WOOKIE_API_KEY = "TEST"; 
    //private static WookieConnectorService  connectorService;
    
    public DefaultWidgetService(){   
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetService#getWidget(org.apache.rave.portal.model.User)
     */
    @Override
    public Widget getWidget(User viewer, String context, Widget widget) {
        //if (widget.getType().equals("W3C")) return getWidgetForViewer(widget, context, viewer);  
        return widget;
    }
    

    /*
    private Widget getWidgetForViewer(Widget widget, String context, User viewer){
        try {
            connectorService = getWookieConnectorService(WOOKIE_SERVER_URL, WOOKIE_API_KEY, context);
            org.apache.wookie.connector.framework.User user = new org.apache.wookie.connector.framework.User(String.valueOf(viewer.getUserId()), viewer.getUsername());
            connectorService.setCurrentUser(user);
            
            System.out.println("Getting widget:"+widget.getUrl()+" from:" +connectorService.getConnection().getURL());
            WidgetInstance instance = connectorService.getOrCreateInstance(widget.getUrl());
            return createWidget(instance);
        } catch (WookieConnectorException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a Rave Widget object for the widget instance.
     * This is a transient object and is not persisted
     * @return
     *
    private Widget createWidget(WidgetInstance instance){
        Widget widget = new Widget();
        widget.setUrl(instance.getUrl());
        widget.setTitle(instance.getTitle());
        widget.setType("W3C");
        return widget;
    }
    
    
    // Get the wookie service connector
    private WookieConnectorService getWookieConnectorService(String serverURL, String apiKey, String sharedDataKey ) throws WookieConnectorException {
      if (connectorService == null) {
        connectorService = new WookieConnectorService(serverURL, apiKey, sharedDataKey);
      }
      return connectorService;
    }
    
    */
    

}
