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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.rave.model.Widget;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.RemoteWidgetResolverService;
import org.apache.rave.portal.service.WidgetMarketplaceService;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.easymock.EasyMock.*;

public class DefaultWidgetMarketplaceServiceTest {

    static RemoteWidgetResolverService widgetResolverService;
    static PortalPreferenceService prefs;
    static WidgetMarketplaceService service;
    static Map<String, WidgetMetadataResolver> widgetMetadataResolverMap;
    static List<WidgetMetadataResolver> metadataResolver;

    @BeforeClass
    public static void setup(){
        prefs = createMock(PortalPreferenceService.class);
        metadataResolver = new ArrayList<WidgetMetadataResolver>();
        service = new DefaultWidgetMarketplaceService(widgetResolverService, prefs);
    }

    @Ignore
    @Test
    public void getSearchResults() throws Exception{
        SearchResult<Widget> results = service.getWidgetsByFreeTextSearch("wookie", 0, 10);
        for (Widget widget: results.getResultSet()){
            System.out.println(widget.getTitle());
        }
        System.out.println(results.getTotalResults() + " total results");
    }

    @Ignore
    @Test
    public void getCategoryResults() throws Exception{
        prefs = createMock(PortalPreferenceService.class);
        service = new DefaultWidgetMarketplaceService(widgetResolverService, prefs);
        SearchResult<Widget> results = service.getWidgetsByCategory("a", 0, 10);
        for (Widget widget: results.getResultSet()){
            System.out.println(widget.getTitle());
        }
        System.out.println(results.getTotalResults() + " total results");
    }

    @Ignore
    @Test
    public void getCategories() throws Exception{
        prefs = createMock(PortalPreferenceService.class);
        service = new DefaultWidgetMarketplaceService(widgetResolverService, prefs);
        List<String> results = service.getCategories();
        for (String category: results){
            System.out.println(category);
        }
    }

}