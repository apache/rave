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

package org.apache.rave.provider.opensocial.web.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rave.rest.model.RegionWidget;

import javax.xml.bind.annotation.*;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
public class OpenSocialRegionWidget extends RegionWidget {
    @XmlElement(name = "securityToken")
    protected String securityToken;
    @XmlElement(name = "metadata")
    protected Map<String, Object> metadata;

    private static ObjectMapper mapper = new ObjectMapper();

    public OpenSocialRegionWidget() {
    }

    public OpenSocialRegionWidget(RegionWidget base, String securityToken, String metadata) {
        this(base);
        this.securityToken = securityToken;
        setMetadata(metadata);
    }

    public OpenSocialRegionWidget(RegionWidget base) {
        this.id = base.getId();
        this.type = base.getType();
        this.widgetId = base.getWidgetId();
        this.widgetUrl = base.getWidgetUrl();
        this.regionId = base.getRegionId();
        this.collapsed = base.isCollapsed();
        this.locked = base.isLocked();
        this.hideChrome = base.isHideChrome();
        this.ownerId = base.getOwnerId();
        this.userPrefs = base.getUserPrefs();
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public  Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        Map<String, Object> metadataObject = null;
        try {
            metadataObject = mapper.readValue(metadata, Map.class);
        } catch (Exception e) {
            //TODO: Do we need to handle this exception somehow?
        }
        this.metadata = metadataObject;
    }
}
