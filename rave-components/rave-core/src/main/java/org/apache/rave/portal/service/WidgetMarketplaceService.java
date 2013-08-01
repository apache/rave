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

import java.util.List;

import org.apache.rave.model.Widget;
import org.apache.rave.rest.model.SearchResult;

/**
 * Provides capabilities for discovering widgets
 * from an external marketplace (e.g. Edukapp, Widgr, OMR etc.)
 */
public interface WidgetMarketplaceService {
	
    /**
     * Gets a SearchResult for {@link Widget}'s by performing a free text search
     *
     * @param searchTerm free text search term
     * @param offset     start point within the resultset (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @throws Exception if the external marketplace is not configured, not available, or returns an error
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsByFreeTextSearch(String searchTerm, int offset, int pageSize) throws Exception;
    
    /**
     * Gets a SearchResult for {@link Widget}'s by performing a category keyword search
     *
     * @param category  category keyword
     * @param offset     start point within the resultSet (for paging)
     * @param pageSize   maximum number of items to be returned (for paging)
     * @throws Exception if the external marketplace is not configured, not available, or returns an error
     * @return SearchResult
     */
    SearchResult<Widget> getWidgetsByCategory(String category, int offset, int pageSize) throws Exception;
    
    /**
     * Gets the list of categories used for widgets in the marketplace
     * @throws Exception if the external marketplace is not configured, not available, or returns an error
     * @return List
     */
    List<String> getCategories() throws Exception;
    
    /**
     * Adds the specified widget; for W3C widgets this implies POSTing the .wgt to Wookie and then parsing 
     * the resulting metadata; for OpenSocial this implies processing the metadata from Shindig and then adding
     * the widget. 
     * 
     * @param widget the widget to be added from the marketplace
     * @return the widget from the local Rave widget store
     * @throws Exception if there was a problem adding the widget to the rave store
     */
    Widget addWidget(Widget widget) throws Exception;
    
    /**
     * Obtains the widget metadata so it can be validated
     * @param url
     * @param type
     * @return
     * @throws Exception
     */
    Widget resolveWidgetMetadata(String url, String type) throws Exception;
    
    /**
     * Gets the specified Widget
     * @param id the value identifying the widget in the marketplace, e.g. Widget URI or GUID
     * @return a Widget
     * @throws Exception if no match is found
     */
    Widget getWidget(String id) throws Exception;

}
