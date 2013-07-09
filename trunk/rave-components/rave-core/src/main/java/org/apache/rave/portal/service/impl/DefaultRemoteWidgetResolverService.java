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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rave.model.PortalPreference;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.portal.service.RemoteWidgetResolverService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRemoteWidgetResolverService implements RemoteWidgetResolverService {

    private final WidgetService widgetService;
    private final UserService userService;
    private PortalPreferenceService portalPreferenceService;
    private Map<String, WidgetMetadataResolver> widgetMetadataResolverMap;

    @Autowired
    public DefaultRemoteWidgetResolverService(WidgetService widgetService, UserService userService, 
        PortalPreferenceService pps,  List<WidgetMetadataResolver> widgetMetadataResolvers){
        this.widgetService = widgetService;
        this.userService = userService;
        this.portalPreferenceService = pps;
        widgetMetadataResolverMap = new HashMap<String, WidgetMetadataResolver>();
        for (WidgetMetadataResolver widgetMetadataResolver : widgetMetadataResolvers) {
            widgetMetadataResolverMap.put(widgetMetadataResolver.getSupportedContext(), widgetMetadataResolver);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetResolverService#resolveWidgetMetadata(java.lang.String, java.lang.String)
     */
    @Override
    public Widget resolveAndDownloadWidgetMetadata(String url, String type) throws Exception{
        Widget widget = null;
        // check for namespaces in the the type element
        if(type.contains("#")){
            String[] deNamespacedType = type.split("#");
            if(deNamespacedType.length > 0){
                type = deNamespacedType[1];
            }
        }
        if (type.equals("W3C") || type.equals("W3C Widget") || type.equals("W3CWidget")){
            widget = widgetMetadataResolverMap.get("W3C").publishRemote(url);
        }else if (type.equals("OpenSocial") || type.startsWith("OpenSocial")|| type.startsWith("Open Social")){
            widget = widgetMetadataResolverMap.get("OpenSocial").getMetadata(url);
        }else{
            // there may be resources in a marketplace store (or in an OMDL instance) other than W3C & OpenSocial 
            // gadgets so return null if it isn't one of the supported types in rave.
            throw new Exception("Unsupported Widget format");
        }
        return widget;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetResolverService#addWidget(org.apache.rave.model.Widget)
     */
    @Override
    public Widget addWidget(Widget widget) throws Exception{
        PortalPreference status = portalPreferenceService.getPreference(PortalPreferenceKeys.INITIAL_WIDGET_STATUS);
        User user = userService.getAuthenticatedUser();
        widget.setOwnerId(user.getId());
        if (status != null && status.getValue().equals("PUBLISHED")){
            widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        } else {
            widget.setWidgetStatus(WidgetStatus.PREVIEW);
        }
        return widgetService.registerNewWidget(widget);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.RemoteWidgetResolverService#isPublished()
     */
    @Override
    public boolean isPublished() {
        PortalPreference status = portalPreferenceService.getPreference(PortalPreferenceKeys.INITIAL_WIDGET_STATUS);
        if (status != null && status.getValue().equals("PUBLISHED")){
            return true;
        }
        return false;
    }
}
