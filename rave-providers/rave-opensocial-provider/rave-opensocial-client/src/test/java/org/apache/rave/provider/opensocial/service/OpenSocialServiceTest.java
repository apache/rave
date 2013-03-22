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

package org.apache.rave.provider.opensocial.service;

import org.apache.rave.exception.ResourceNotFoundException;
import org.apache.rave.model.Page;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.Widget;
import org.apache.rave.model.WidgetStatus;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.apache.rave.provider.opensocial.service.impl.DefaultOpenSocialService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class OpenSocialServiceTest {
    public static final String VALID_TOKEN = "VALID_TOKEN";
    private OpenSocialService openSocialService;
    private GadgetMetadataRepository gadgetMetadataRepository;
    private WidgetService widgetService;
    private PageService pageService;
    private SecurityTokenService tokenService;

    private static final String VALID_PAGE_ID = "VALID";
    private static final String VALID_OWNER_ID = "owner";
    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\"}}}]";

    @Before
    public void setup() {
        gadgetMetadataRepository = createNiceMock(GadgetMetadataRepository.class);
        widgetService = createNiceMock(WidgetService.class);
        pageService = createNiceMock(PageService.class);
        tokenService = createNiceMock(SecurityTokenService.class);
        openSocialService = new DefaultOpenSocialService(gadgetMetadataRepository, widgetService, pageService, tokenService);
    }

    @Test
    public void getGadgetMetadata() {
        expect(gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(gadgetMetadataRepository);

        String result = openSocialService.getGadgetMetadata(VALID_GADGET_URL);
        assertThat(result, is(sameInstance(VALID_METADATA)));
    }

    @Test
    public void getEncryptedSecurityToken_valid() {
        Widget widget = new WidgetImpl("25", VALID_GADGET_URL);
        Page page = new PageImpl(VALID_PAGE_ID, VALID_OWNER_ID);
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        expect(widgetService.getWidgetByUrl(VALID_GADGET_URL)).andReturn(widget);
        expect(pageService.getPage(VALID_PAGE_ID)).andReturn(page);
        expect(tokenService.getEncryptedSecurityToken(isA(RegionWidget.class), eq(widget))).andReturn(VALID_TOKEN);
        replay(widgetService, pageService, tokenService);

        assertThat(openSocialService.getEncryptedSecurityToken(VALID_PAGE_ID, VALID_GADGET_URL), is(equalTo(VALID_TOKEN)));
    }

    @Test(expected = IllegalStateException.class)
    public void getEncryptedSecurityToken_NotPublished() {
        Widget widget = new WidgetImpl("25", VALID_GADGET_URL);
        widget.setWidgetStatus(WidgetStatus.PREVIEW);
        expect(widgetService.getWidgetByUrl(VALID_GADGET_URL)).andReturn(widget);
        replay(widgetService, pageService, tokenService);

        assertThat(openSocialService.getEncryptedSecurityToken(VALID_PAGE_ID, VALID_GADGET_URL), is(equalTo(VALID_TOKEN)));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getEncryptedSecurityToken_NotFound() {
        Widget widget = new WidgetImpl("25", VALID_GADGET_URL);
        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
        expect(widgetService.getWidgetByUrl(VALID_GADGET_URL)).andReturn(null);
        replay(widgetService, pageService, tokenService);

        assertThat(openSocialService.getEncryptedSecurityToken(VALID_PAGE_ID, VALID_GADGET_URL), is(equalTo(VALID_TOKEN)));
    }
}