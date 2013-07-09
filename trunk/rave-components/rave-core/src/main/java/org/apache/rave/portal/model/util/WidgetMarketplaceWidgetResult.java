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
package org.apache.rave.portal.model.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.ExternalWidgetImpl;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WidgetMarketplaceWidgetResult {

    public WidgetMarketplaceWidgetResult(){};

    @JsonProperty("widgetProfile")
    public MarketplaceWidgetDetailResult widgetProfile;

    public Widget getWidget(){
        MarketplaceWidgetDetailResult result = getWidgetProfile();
        return result.toWidget("1");
    }
    
    /**
     * @return the widgets
     */
    public MarketplaceWidgetDetailResult getWidgetProfile() {
        return widgetProfile;
    }

    public void setWidgetProfile(MarketplaceWidgetDetailResult profile) {
        this.widgetProfile = profile;
    }
    
    
    /**
     * Wrapper for widget metadata in marketplace search results
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class MarketplaceWidgetDetailResult {
        
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
