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

package org.apache.rave.portal.web.api.rest;

import org.apache.commons.lang3.Validate;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.portal.service.RegionWidgetService;
import org.apache.rave.portal.web.model.RegionWidgetPreferenceListWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Handler for all services exposed under the /api/regionWidgets path.
 */
@Controller
@RequestMapping("/api/rest/regionWidgets")
public class RegionWidgetApi extends AbstractRestApi {
    private static Logger logger = LoggerFactory.getLogger(RegionWidgetApi.class);
    

    private RegionWidgetService regionWidgetService;

    @Autowired
    public RegionWidgetApi(RegionWidgetService regionWidgetService) {
        this.regionWidgetService = regionWidgetService;
    }

    @ResponseBody
    @RequestMapping(value = "/{regionWidgetId}/preferences", method = RequestMethod.PUT)
    public RegionWidgetPreferenceListWrapper replaceAllRegionWidgetPreferences(@PathVariable String regionWidgetId,
                                                                               @RequestBody RegionWidgetPreferenceListWrapper
                                                                                       regionWidgetPreferenceListWrapper) {
        if (logger.isDebugEnabled()) {
            logger.debug("PUT received to replace all preferences for regionWidget: " + regionWidgetId + " with data: " +
                    regionWidgetPreferenceListWrapper);
        }

        List<RegionWidgetPreference> regionWidgetPreferences = regionWidgetService.saveRegionWidgetPreferences(
                regionWidgetId, regionWidgetPreferenceListWrapper.getPreferences());
        return new RegionWidgetPreferenceListWrapper(regionWidgetPreferences);
    }

    @ResponseBody
    @RequestMapping(value = "/{regionWidgetId}/preferences/{regionWidgetPreferenceName}", method = RequestMethod.PUT)
    public RegionWidgetPreference createOrReplaceRegionWidgetPreference(@PathVariable String regionWidgetId,
                                                                        @PathVariable String regionWidgetPreferenceName,
                                                                        @RequestBody RegionWidgetPreference regionWidgetPreference) {
        if (logger.isDebugEnabled()) {
            logger.debug("PUT received to create or replace preference: " + regionWidgetPreferenceName + " for regionWidget: " +
                    regionWidgetId + " with data: " + regionWidgetPreference);
        }

        Validate.isTrue(regionWidgetPreferenceName.equalsIgnoreCase(regionWidgetPreference.getName()),
                "The preference name in the URL does not match the preference name in the RegionWidgetPreference object.");

        return regionWidgetService.saveRegionWidgetPreference(regionWidgetId, regionWidgetPreference);
    }
        
    /**
     * REST call to update the collapsed value of a RegionWidget
     * 
     * @param regionWidgetId the ID of the RegionWidget to update
     * @param collapsed boolean representing the new collapse state
     * @return the updated RegionWidget object
     */
    @ResponseBody
    @RequestMapping(value = "/{regionWidgetId}/collapsed", method = RequestMethod.PUT)
    public RegionWidget updateRegionWidgetCollapsedStatus(@PathVariable String regionWidgetId,
                                                          @RequestBody Boolean collapsed) {
        if (logger.isDebugEnabled()) {
            logger.debug("POST received to update regionWidget " +
                    regionWidgetId + ": " + collapsed);
        }
        
        return regionWidgetService.saveRegionWidgetCollapsedState(regionWidgetId, collapsed);              
    }
}