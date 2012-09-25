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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.model.WidgetStatus;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.model.util.WidgetMarketplaceSearchResult;
import org.apache.rave.portal.model.util.WidgetMarketplaceWidgetResult;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetMarketplaceService;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * Default implementation of a widget marketplace service. 
 * 
 * Uses REST and JSON for searching and obtaining category listings
 *
 */
@Service
public class DefaultWidgetMarketplaceService implements WidgetMarketplaceService {

    private final WidgetService widgetService;
    private final UserService userService;
    private PortalPreferenceService portalPreferenceService;
    private Map<String, WidgetMetadataResolver> widgetMetadataResolverMap;
    private static final String DEFAULT_URL = null;
    private static final Logger logger = LoggerFactory.getLogger(DefaultWidgetMarketplaceService.class);
    private static final String SEARCH = "search?q=${SEARCHTERM}&start=${OFFSET}&rows=${LIMIT}";
    private static final String CATEGORY = "tag/${CATEGORY}/widgets?start=${OFFSET}&rows=${LIMIT}";	
    private static final String DETAIL = "widget/${ID}";
    private static final String CATEGORY_LIST = "tag/all";
    
    @Autowired
    public DefaultWidgetMarketplaceService(WidgetService widgetService, UserService userService, 
        PortalPreferenceService pps,  List<WidgetMetadataResolver> widgetMetadataResolvers){
        this.widgetService = widgetService;
        this.userService = userService;
        this.portalPreferenceService = pps;
        widgetMetadataResolverMap = new HashMap<String, WidgetMetadataResolver>();
        for (WidgetMetadataResolver widgetMetadataResolver : widgetMetadataResolvers) {
            widgetMetadataResolverMap.put(widgetMetadataResolver.getSupportedContext(), widgetMetadataResolver);
        }
	}

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#getWidgetsByFreeTextSearch(java.lang.String, int, int)
     */
    @Override
    public SearchResult<Widget> getWidgetsByFreeTextSearch(String searchTerm,
            int offset, int pageSize) throws Exception{
        if (getStoreUrl() == null) throw new Exception("External marketplace URL not configured");
        // commented out because encoding the searchTerm here ends up with
        // it being encoded twice 
        // String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
        String encodedSearchTerm = searchTerm;
        String url = getStoreUrl() + SEARCH;
        url = url.replace("${SEARCHTERM}", encodedSearchTerm);
        url = url.replace("${OFFSET}", String.valueOf(offset));
        url = url.replace("${LIMIT}", String.valueOf(pageSize));
        SearchResult<Widget> result = executeQuery(url);
        result.setOffset(offset);
        result.setPageSize(pageSize);
        return result;
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#getWidgetsByTag(java.lang.String, int, int)
     */
    @Override
    public SearchResult<Widget> getWidgetsByCategory(String category, int offset,
            int pageSize) throws Exception{
        if (getStoreUrl() == null) throw new Exception("External marketplace URL not configured");
        String url = getStoreUrl() + CATEGORY;
        url = url.replace("${CATEGORY}", category);
        url = url.replace("${OFFSET}", String.valueOf(offset));
        url = url.replace("${LIMIT}", String.valueOf(pageSize));
        SearchResult<Widget> result = executeQuery(url);
        result.setOffset(offset);
        result.setPageSize(pageSize);
        return result;
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#getCategories()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getCategories() throws Exception{
        if (getStoreUrl() == null) throw new Exception("External marketplace URL not configured");
        String url = getStoreUrl() + CATEGORY_LIST;
        ArrayList<String> categories = new ArrayList<String>();
        // Extract category names from the result
        @SuppressWarnings("rawtypes")
        List result = getRestJsonTemplate().getForObject(url, List.class);
        for (Object category:result){
            categories.add((String)((Map<String, String>)category).get("tagtext"));
        }
        return categories;
    }

    /**
     * Get the store URL
     * @return the URL for the store; either as set in portal preferences or the default value
     */
    private String getStoreUrl(){
        PortalPreference storePref = portalPreferenceService.getPreference(PortalPreferenceKeys.EXTERNAL_MARKETPLACE_URL);
        if (storePref != null){
            return storePref.getValue();
        } else {
            return DEFAULT_URL;
        }
    }

    /**
     * Execute the query and return a SearchResult
     * @param query the query to perform
     * @return the search result
     */
    private SearchResult<Widget> executeQuery(String query) throws Exception{
        // Use a wrapper for the result, and then convert to SearchResult
        WidgetMarketplaceSearchResult result;
        try {
            result = getRestJsonTemplate().getForObject(query, WidgetMarketplaceSearchResult.class);
        } catch (RestClientException e) {
            throw e;
        }
        return result.toSearchResult();
    }

    /**
     * Create a JSON REST Template for requests
     * @return a RestTemplate configured to process JSON data
     */
    private RestTemplate getRestJsonTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> mc = restTemplate.getMessageConverters();
        // Add JSON message handler
        MappingJacksonHttpMessageConverter json = new MappingJacksonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(new MediaType("application","json", Charset.forName("UTF-8")));
        // Add default media type in case marketplace uses incorrect MIME type, otherwise
        // Spring refuses to process it, even if its valid JSON
        supportedMediaTypes.add(new MediaType("application","octet-stream", Charset.forName("UTF-8")));
        json.setSupportedMediaTypes(supportedMediaTypes);
        mc.add(json);
        restTemplate.setMessageConverters(mc);
        return restTemplate;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#getWidgetMetadata(java.lang.String, java.lang.String)
     */
    public Widget getWidgetMetadata(String url, String type) throws Exception{
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
        } else if (type.equals("OpenSocial") || type.startsWith("OpenSocial")|| type.startsWith("Open Social")){
            widget = widgetMetadataResolverMap.get("OpenSocial").getMetadata(url);
        }else{
            // there may be resources in a marketplace store other than W3C & OpenSocial 
            // gadgets so return null if it isn't one of the supported types in rave.
            throw new Exception("Unsupported Widget format");
        }
        return widget;
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#addWidget(org.apache.rave.portal.model.Widget)
     */
    @Override
    public Widget addWidget(Widget widget) throws Exception{
        PortalPreference status = portalPreferenceService.getPreference(PortalPreferenceKeys.INITIAL_WIDGET_STATUS);
        User user = userService.getAuthenticatedUser();
        widget.setOwner(user);
        if (status != null && status.getValue().equals("PUBLISHED")){
            widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        } else {
            widget.setWidgetStatus(WidgetStatus.PREVIEW);
        }
        widgetService.registerNewWidget(widget);
        return widget;
    }

    /* (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMarketplaceService#getWidget(java.lang.String)
     */
    @Override
    public Widget getWidget(String id) throws Exception {
        if (getStoreUrl() == null) throw new Exception("External marketplace URL not configured");
        String url = getStoreUrl() + DETAIL;
        url = url.replace("${ID}", id);
        WidgetMarketplaceWidgetResult widgetResult = getRestJsonTemplate().getForObject(url, WidgetMarketplaceWidgetResult.class);
        Widget widget = widgetResult.getWidget();
        return widget;
    }

}
