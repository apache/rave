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

package org.apache.rave.provider.w3c.service.impl;

import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class WookieWidgetMetadataResolverTest {
    private W3CWidgetMetadataRepository wookieWidgetMetadataRepository;
    private WidgetMetadataResolver widgetMetadataResolver;
    private WookieWidgetService widgetService;
    private static final String TYPE = "W3C";

    // TODO - update these tests to use GUID rather than id once rave is bundled with wookie 0.10.0
    private static final String VALID_IDENTIFIER = "7";
    private static final String VALID_GROUP_IDENTIFIER = "?all=true";
    private static Widget TEST_WIDGET;

    @Before
    public void setup() {
    	widgetService = createNiceMock(WookieWidgetService.class);
        wookieWidgetMetadataRepository = createNiceMock(W3CWidgetMetadataRepository.class);
        widgetMetadataResolver = new WookieWidgetMetadataResolver(widgetService);
        
        TEST_WIDGET = new WidgetImpl();
        TEST_WIDGET.setTitle("freeder");
        TEST_WIDGET.setUrl("http://wookie.apache.org/widgets/freeder");
        TEST_WIDGET.setDescription("An RSS reader widget optimised for small screens or desktop widgets.");
        TEST_WIDGET.setThumbnailUrl("http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/freeder/images/icon.png");
        TEST_WIDGET.setType(TYPE);
    }

    @Test
    public void getSupportedContext_test() {
        assertNotNull(widgetMetadataResolver.getSupportedContext());
    }

    @Test
    public void getMetadata() throws IOException, WookieConnectorException {
        expect(wookieWidgetMetadataRepository.getWidgetMetadata(VALID_IDENTIFIER)).andReturn(TEST_WIDGET);
        replay(wookieWidgetMetadataRepository);
        
        expect(widgetService.getWidget(VALID_IDENTIFIER)).andReturn(TEST_WIDGET);
        replay(widgetService);
        
        Widget w = widgetMetadataResolver.getMetadata(VALID_IDENTIFIER);
        assertNotNull(w);
        assertEquals("freeder", w.getTitle());
        assertEquals("http://wookie.apache.org/widgets/freeder", w.getUrl());
        assertEquals("An RSS reader widget optimised for small screens or desktop widgets.", w.getDescription());
        assertEquals("http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/freeder/images/icon.png", w.getThumbnailUrl());
        assertEquals(TYPE, w.getType());
    }

    @Test
    public void getMetadata_noWidgetFound() throws WookieConnectorException {
        expect(wookieWidgetMetadataRepository.getWidgetMetadata(VALID_IDENTIFIER)).andReturn(null);
        replay(wookieWidgetMetadataRepository);
        
        expect(widgetService.getWidget(VALID_IDENTIFIER)).andReturn(null);
        replay(widgetService);
        Widget w = widgetMetadataResolver.getMetadata(VALID_IDENTIFIER);
        assertNull(w);
    }

    @Test
    public void getAllWidgets() throws IOException, WookieConnectorException {
        Widget[] results = new Widget[]{TEST_WIDGET};
        expect(wookieWidgetMetadataRepository.getWidgetMetadata()).andReturn(results);
        replay(wookieWidgetMetadataRepository);
        
        expect(widgetService.getWidgets()).andReturn(results);
        replay(widgetService);
        
        Widget[] widgets = widgetMetadataResolver.getMetadataGroup(VALID_GROUP_IDENTIFIER);
        assertEquals(1, widgets.length);
        assertEquals("freeder", widgets[0].getTitle());
        assertEquals("http://wookie.apache.org/widgets/freeder", widgets[0].getUrl());
        assertEquals("An RSS reader widget optimised for small screens or desktop widgets.", widgets[0].getDescription());
        assertEquals("http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/freeder/images/icon.png", widgets[0].getThumbnailUrl());
        assertEquals(TYPE, widgets[0].getType());
    }
}
