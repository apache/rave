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

package org.apache.rave.provider.opensocial.service.impl;

import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.model.Page;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultOpenSocialService implements OpenSocialService {
    protected final GadgetMetadataRepository gadgetMetadataRepository;
    protected final WidgetService widgetService;
    protected final PageService pageService;
    protected final SecurityTokenService tokenService;

    @Autowired
    public DefaultOpenSocialService(GadgetMetadataRepository gadgetMetadataRepository, WidgetService widgetService, PageService pageService, SecurityTokenService tokenService) {
        this.gadgetMetadataRepository = gadgetMetadataRepository;
        this.widgetService = widgetService;
        this.pageService = pageService;
        this.tokenService = tokenService;
    }

    @Override
    public String getGadgetMetadata(String gadgetUrl) {
        //TODO RAVE-243 -- Add caching of gadget metadata in this service layer
        return gadgetMetadataRepository.getGadgetMetadata(gadgetUrl);
    }

    @Override
    public String getEncryptedSecurityToken(String pageId, String url) {
        Widget widget = widgetService.getWidgetByUrl(url);
        Page page = pageService.getPage(pageId);
        validate(widget);
        // Use a dummy RegionWidget to generate the security token
        RegionWidget regionWidget = new RegionWidgetImpl(String.valueOf(System.currentTimeMillis()),"-1",
                new RegionImpl("-1", page, -1));
        return tokenService.getEncryptedSecurityToken(regionWidget, widget);
    }

    @Override
    public String getEncryptedSecurityToken(org.apache.rave.rest.model.RegionWidget item) {
        return tokenService.getEncryptedSecurityToken(item.getId(), item.getWidgetUrl(), item.getOwnerId());
    }

    protected void validate(Widget widget) {
        if(widget == null) {
            throw new ResourceNotFoundException("The requested gadget does not exist in the gadget store.");
        } else if(widget.getWidgetStatus().equals(WidgetStatus.PREVIEW)) {
            throw new IllegalStateException("The requested gadget exists in the gadget store but is not published.");
        }
    }
}