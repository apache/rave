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

package org.apache.rave.rest.impl;


import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.rest.PortalPreferenceResource;
import org.apache.rave.rest.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPortalPreferenceResource implements PortalPreferenceResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PortalPreferenceService preferenceService;

    @Override
    public SearchResult<PortalPreference> getPreferences() {
        return preferenceService.getAll();
    }

    @Override
    public PortalPreference updatePreferences(PortalPreference preference) {
        preferenceService.savePreference(preference);

        return null;
    }

    @Override
    public PortalPreference getPreference(String id) {
        PortalPreference fromDb = preferenceService.getPreference(id);

        if(fromDb == null) {
            throw new ResourceNotFoundException(id);
        }

        return fromDb;
    }

    public void setPreferenceService(PortalPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }
}
