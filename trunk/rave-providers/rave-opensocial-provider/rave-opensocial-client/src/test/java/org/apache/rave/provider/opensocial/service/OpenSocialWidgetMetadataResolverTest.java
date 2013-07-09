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

import org.apache.rave.model.Widget;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.apache.rave.provider.opensocial.service.impl.OpenSocialWidgetMetadataResolver;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

public class OpenSocialWidgetMetadataResolverTest {

    private GadgetMetadataRepository gadgetMetadataRepository;
    private WidgetMetadataResolver widgetMetadataResolver;
    private final String TYPE = "OpenSocial";
    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_METADATA = "{\"modulePrefs\":{\"screenshot\":\"http://www.gstatic.com/ig/modules/dictionary/dictionary_content/ALL_ALL.cache.png\",\"authorEmail\":\"googlemodules+dictionary+201109071@google.com\",\"authorLink\":\"\",\"links\":{},\"author\":\"Google Taiwan\",\"title\":\"Google Translate\",\"authorAffiliation\":\"Google Inc.\",\"titleUrl\":\"http://translate.google.com/\",\"thumbnail\":\"http://www.gstatic.com/ig/modules/dictionary/dictionary_content/ALL_ALL-thm.cache.png\",\"authorLocation\":\"Taiwan\",\"description\":\"Google Translation gadget can translate between common languages in the world\",\"features\":{\"dynamic-height\":{\"name\":\"dynamic-height\",\"params\":{},\"required\":true},\"setprefs\":{\"name\":\"setprefs\",\"params\":{},\"required\":true},\"core\":{\"name\":\"core\",\"params\":{},\"required\":true}}}}";
    private static final String INVALID_METADATA = "{\"hasPrefsToEdit\":false,\"error\":{\"message\":\"Unable to retrieve spec for http://www.gstatic.com/ig/modules/dictionary/123dictionary.xml. HTTP error 404\",\"code\":404}}";

    @Before
    public void setup() {
        gadgetMetadataRepository = createNiceMock(GadgetMetadataRepository.class);
        widgetMetadataResolver = new OpenSocialWidgetMetadataResolver(gadgetMetadataRepository);
    }

    @Test
    public void getSupportedContext_test() {
        assertNotNull(widgetMetadataResolver.getSupportedContext());
    }

    @Test
    public void getMetadata() {
        expect(gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(gadgetMetadataRepository);
        Widget w = widgetMetadataResolver.getMetadata(VALID_GADGET_URL);
        assertNotNull(w);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMetadata_invalid() {
        expect(gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL)).andReturn(INVALID_METADATA);
        replay(gadgetMetadataRepository);
        Widget w = widgetMetadataResolver.getMetadata(VALID_GADGET_URL);
        assertNotNull(w);
    }
}
