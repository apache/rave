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

import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

/**
 * Handles the call to wookies metadata service
 * (wookie currently uses an xml format response)
 */
@Repository
public class WookieWidgetMetadataRepository implements W3CWidgetMetadataRepository {
    private static Logger logger = LoggerFactory.getLogger(WookieWidgetMetadataRepository.class);
    private RestOperations restOperations;
    private String wookieUrl;

    @Autowired
    public WookieWidgetMetadataRepository(@Qualifier(value = "xmlStringCompatibleRestTemplate") RestOperations restOperations,
                                          @Value("${provider.wookie.wookieServerUrl}") String wookieRoot) {
        this.restOperations = restOperations;
        this.wookieUrl = wookieRoot + "/widgets/";
        logger.debug("Wookie render Url: {}", wookieUrl);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository#getWidgetMetadata(java.lang.String)
     */
    @Override
    public String getWidgetMetadata(String widgetGuid) {
        String responseString = null;
        try {
            responseString = restOperations.getForObject(wookieUrl + widgetGuid, String.class);
        } catch (RestClientException e) {
            throw new IllegalArgumentException("Error occurred while processing response from wookie metadata call", e);
        }
        // return the raw xml
        return responseString;
    }

}
