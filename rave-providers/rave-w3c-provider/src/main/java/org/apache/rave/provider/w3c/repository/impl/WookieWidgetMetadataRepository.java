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

package org.apache.rave.provider.w3c.repository.impl;

import org.apache.rave.model.Widget;
import org.apache.rave.portal.service.WidgetProviderService;
import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.apache.rave.provider.w3c.service.impl.WookieWidgetService;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * Handles the call to wookies metadata service
 * (wookie currently uses an xml format response)
 */
@Repository
public class WookieWidgetMetadataRepository implements W3CWidgetMetadataRepository {
    private static Logger logger = LoggerFactory.getLogger(WookieWidgetMetadataRepository.class);
    private WookieWidgetService widgetService;

    @Autowired
    public WookieWidgetMetadataRepository(@Qualifier("wookieWidgetService") WidgetProviderService widgetService) {
    	this.widgetService = (WookieWidgetService) widgetService;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository#getWidgetMetadata(java.lang.String)
     */
    @Override
    public Widget getWidgetMetadata(String widgetGuid) {
        try {
        	return this.widgetService.getWidget(widgetGuid);
        } catch (WookieConnectorException e) {
            throw new IllegalArgumentException("Error occurred while processing response from wookie metadata call", e);
        }
    }

	/* (non-Javadoc)
	 * @see org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository#getWidgetMetadata()
	 */
	@Override
	public Widget[] getWidgetMetadata() {
		try {
			return this.widgetService.getWidgets();
		} catch (WookieConnectorException e) {
            throw new IllegalArgumentException("Error occurred while processing response from wookie metadata call", e);
		}
	}

    @Override
    public Widget publishRemote(String url) {
        return this.widgetService.publishWidgetUrlToWookie(url);
    }
    
    

}
