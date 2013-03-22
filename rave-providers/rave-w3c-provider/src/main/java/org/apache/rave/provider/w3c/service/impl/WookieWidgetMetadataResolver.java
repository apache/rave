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

import org.apache.rave.model.Widget;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.provider.w3c.Constants;
import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.apache.rave.provider.w3c.repository.impl.WookieWidgetMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class WookieWidgetMetadataResolver implements WidgetMetadataResolver {
    private static Logger logger = LoggerFactory.getLogger(WookieWidgetMetadataResolver.class);
    private W3CWidgetMetadataRepository widgetMetadataRepository;

    @Autowired
    public WookieWidgetMetadataResolver(@Qualifier("wookieWidgetService") WidgetProviderService widgetService){
        this.widgetMetadataRepository = new WookieWidgetMetadataRepository(widgetService);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getSupportedContext()
     */
    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getMetadata(java.lang.String)
     */
    public Widget getMetadata(String url) {
        try {
            return widgetMetadataRepository.getWidgetMetadata(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getMetadataGroup(java.lang.String)
     */
    @Override
    public Widget[] getMetadataGroup(String url) {
        try {
            return widgetMetadataRepository.getWidgetMetadata();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget (group) metadata call", e);
        }
    }

    @Override
    public Widget publishRemote(String url) {
        return widgetMetadataRepository.publishRemote(url);
    }

}
