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
package org.apache.rave.portal.service;

import org.apache.rave.model.Widget;

public interface RemoteWidgetResolverService {

    /**
     * For a given url and widget type wrap the widgetServices implementation of getWidgetMetatdat
     * so that a remote W3C widget can be downloaded and installed into wookie first.
     * @param url - the url of the opensocial or W3C resource on the web
     * @param type - providertype
     * @return - a widget
     * @throws Exception
     */
    public Widget resolveAndDownloadWidgetMetadata(String url, String type) throws Exception;
    
    /**
     * Add a widget to raves DB.
     * @param widget - widget not yet part of rave
     * @return widget - A rave widget with its internal id now set
     * @throws Exception
     */
    public Widget addWidget(Widget widget) throws Exception;
    
    /**
     * Is the config property for widget status set to PUBLISHED
     * @return true if it is set to PUBLISHED
     */
    public boolean isPublished();

}
