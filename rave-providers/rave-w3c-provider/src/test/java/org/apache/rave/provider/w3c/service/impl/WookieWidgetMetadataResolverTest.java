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

import org.apache.commons.io.FileUtils;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WookieWidgetMetadataResolverTest {
    private W3CWidgetMetadataRepository wookieWidgetMetadataRepository;
    private WidgetMetadataResolver widgetMetadataResolver;
    private static final String TYPE = "W3C";

    // TODO - update these tests to use GUID rather than id once rave is bundled with wookie 0.10.0
    private static final String VALID_IDENTIFIER = "7";
    private static final String VALID_GROUP_IDENTIFIER = "?all=true";
    private static final String NO_WIDGETS_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><widgets></widgets>";
    private static File ALL_WIDGETS_FILE;
    private static File SINGLE_WIDGET_FILE;

    @Before
    public void setup() {
        wookieWidgetMetadataRepository = createNiceMock(W3CWidgetMetadataRepository.class);
        widgetMetadataResolver = new WookieWidgetMetadataResolver(wookieWidgetMetadataRepository);
        ALL_WIDGETS_FILE = new File("src/test/resources/allwidgets.xml");
        SINGLE_WIDGET_FILE = new File("src/test/resources/singlewidget.xml");
    }

    @Test
    public void getSupportedContext_test() {
        assertNotNull(widgetMetadataResolver.getSupportedContext());
    }

    @Test
    public void getMetadata() throws IOException {
        assertTrue(SINGLE_WIDGET_FILE.exists());
        String xmlText = FileUtils.readFileToString(SINGLE_WIDGET_FILE);
        expect(wookieWidgetMetadataRepository.getWidgetMetadata(VALID_IDENTIFIER)).andReturn(xmlText);
        replay(wookieWidgetMetadataRepository);
        Widget w = widgetMetadataResolver.getMetadata(VALID_IDENTIFIER);
        assertNotNull(w);
        assertEquals("freeder", w.getTitle());
        assertEquals("Apache Wookie (Incubating) Team", w.getAuthor());
        assertEquals("http://wookie.apache.org/widgets/freeder", w.getUrl());
        assertEquals("An RSS reader widget optimised for small screens or desktop widgets.", w.getDescription());
        assertEquals("http://localhost:8080/wookie/wservices/wookie.apache.org/widgets/freeder/images/icon.png", w.getThumbnailUrl());
        assertEquals(TYPE, w.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMetadata_noWidgetFound() {
        expect(wookieWidgetMetadataRepository.getWidgetMetadata(VALID_IDENTIFIER)).andReturn(NO_WIDGETS_RESPONSE);
        replay(wookieWidgetMetadataRepository);
        Widget w = widgetMetadataResolver.getMetadata(VALID_IDENTIFIER);
        assertNotNull(w);
    }

    @Test
    public void getAllWidgets() throws IOException {
        assertTrue(ALL_WIDGETS_FILE.exists());
        String xmlText = FileUtils.readFileToString(ALL_WIDGETS_FILE);
        expect(wookieWidgetMetadataRepository.getWidgetMetadata(VALID_GROUP_IDENTIFIER)).andReturn(xmlText);
        replay(wookieWidgetMetadataRepository);
        Widget[] widgets = widgetMetadataResolver.getMetadataGroup(VALID_GROUP_IDENTIFIER);
        assertEquals(14, widgets.length);
        assertNull(widgets[0].getThumbnailUrl());
        assertEquals("http://www.getwookie.org/widgets/wiki", widgets[3].getUrl());
        assertEquals("A silly Weather widget", widgets[4].getDescription());
        assertEquals("Ta-Da!", widgets[9].getTitle());
        assertEquals("http://localhost:8080/wookie/wservices/www.opera.com/widgets/bubbles/icon_64.png", widgets[12].getThumbnailUrl());
    }
}
