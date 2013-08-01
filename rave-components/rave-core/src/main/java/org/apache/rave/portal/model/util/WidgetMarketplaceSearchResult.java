/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.ExternalWidgetImpl;
import org.apache.rave.rest.model.SearchResult;

/**
 * Wrapper for a set of search results from an external marketplace,
 * mapping values from the marketplace to internal Rave model objects.
 * 
 * We have to do this as the JSON mechanism cannot handle marshalling
 * parameterized classes such as SearchResult<Widget>. It also gives
 * us a chance to map values that have different terms in a marketplace.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WidgetMarketplaceSearchResult {
	
	public WidgetMarketplaceSearchResult(){};
	
	@JsonProperty("number_of_results")
	public int numberOfResults;
	
	@JsonProperty("SearchResults")
	public MarketplaceWidgetResult[] widgets;
	
	public SearchResult<Widget> toSearchResult(){
	    Long widgetIndex = 1L;
		List<Widget> widgets = new ArrayList<Widget>();
		for (MarketplaceWidgetResult widget:getWidgets()){
			widgets.add(widget.toWidget(widgetIndex.toString()));
			widgetIndex++;
		}
		
		SearchResult<Widget> searchResult = new SearchResult<Widget>(widgets,getNumberOfResults());
		
		return searchResult;
	}

	/**
	 * @return the numberOfResults
	 */
	public int getNumberOfResults() {
		return numberOfResults;
	}

	/**
	 * @param numberOfResults the numberOfResults to set
	 */
	public void setNumberOfResults(int numberOfResults) {
		this.numberOfResults = numberOfResults;
	}

	/**
	 * @return the widgets
	 */
	public MarketplaceWidgetResult[] getWidgets() {
		return widgets;
	}

	/**
	 * @param widgets the widgets to set
	 */
	public void setWidgets(MarketplaceWidgetResult[] widgets) {
		this.widgets = widgets;
	}
	

	/**
	 * Wrapper for widget metadata in marketplace search results
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class MarketplaceWidgetResult {
		
		public String name;
		public String description;
		public String type;
		public String uri;
		public String icon;
		public String id;
		public String downloadUrl;
				
		public Widget toWidget(String widgetIndex){
			ExternalWidgetImpl widget = new ExternalWidgetImpl(widgetIndex);
			widget.setTitle(name);
			widget.setDescription(description);
			widget.setType(type);
			widget.setUrl(downloadUrl);
			widget.setThumbnailUrl(icon);
			widget.setExternalId(id);
			return widget;
		}
	}

}
