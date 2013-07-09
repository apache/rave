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

package org.apache.rave.provider.opensocial.web.renderer;

import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.service.OpenSocialService;
import org.apache.rave.provider.opensocial.web.model.OpenSocialRegionWidget;
import org.apache.rave.rest.model.RegionWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenSocialRegionWidgetRenderer implements RegionWidgetRenderer {
    private static Logger logger = LoggerFactory.getLogger(OpenSocialRegionWidgetRenderer.class);

    private final OpenSocialService openSocialService;

    @Autowired
    public OpenSocialRegionWidgetRenderer(OpenSocialService openSocialService) {
        this.openSocialService = openSocialService;
    }

    @Override
    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    @Override
    public String render(RegionWidget item, RenderContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegionWidget prepareForRender(RegionWidget item) {
        logger.debug("Building OpenSocialRegionWidget for " + item.getId());
        return new OpenSocialRegionWidget(
                item,
                openSocialService.getEncryptedSecurityToken(item),
                openSocialService.getGadgetMetadata(item.getWidgetUrl())
        );
    }
}
