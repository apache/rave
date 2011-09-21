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

import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.apache.rave.provider.opensocial.service.impl.DefaultOpenSocialService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class OpenSocialServiceTest {
    private OpenSocialService openSocialService;
    private GadgetMetadataRepository gadgetMetadataRepository;

    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\"}}}]";

    @Before
    public void setup() {
        gadgetMetadataRepository = createNiceMock(GadgetMetadataRepository.class);
        openSocialService = new DefaultOpenSocialService(gadgetMetadataRepository);
    }

    @Test
    public void getGadgetMetadata() {
        expect(gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL)).andReturn(VALID_METADATA);
        replay(gadgetMetadataRepository);

        String result = openSocialService.getGadgetMetadata(VALID_GADGET_URL);
        assertThat(result, is(sameInstance(VALID_METADATA)));
    }
}