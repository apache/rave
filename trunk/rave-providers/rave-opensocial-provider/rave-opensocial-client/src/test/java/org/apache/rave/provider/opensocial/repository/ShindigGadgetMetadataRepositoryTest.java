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

package org.apache.rave.provider.opensocial.repository;

import org.apache.rave.provider.opensocial.repository.impl.ShindigGadgetMetadataRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestOperations;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShindigGadgetMetadataRepositoryTest {
    private ShindigGadgetMetadataRepository gadgetMetadataRepository;
    private RestOperations restOperations;

    @Autowired
    @Value("${portal.opensocial_engine.protocol}")
    String shindigProtocol;

    @Autowired
    @Value("${portal.opensocial_engine.root}")
    String shindigRoot;

    private String shindigUrl;

    private static final String VALID_GADGET_URL = "http://www.example.com/gadget.xml";
    private static final String VALID_SHINDIG_METADATA_RPC_REQUEST = "[{\"id\":\"gadgets.metadata\"," +
            "\"method\":\"gadgets.metadata\",\"params\":{\"groupId\":\"@self\"," +
            "\"ids\":[\"http://www.example.com/gadget.xml\"],\"container\":\"default\",\"userId\":\"@viewer\"," +
            "\"fields\":[\"iframeUrls\",\"modulePrefs.*\",\"needsTokenRefresh\",\"userPrefs.*\",\"views.preferredHeight\"," +
            "\"views.preferredWidth\",\"expireTimeMs\",\"responseTimeMs\"]}}]";
    private static final String VALID_METADATA = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\",\"userPrefs\":{\"age\":{\"dataType\":\"STRING\",\"default\":\"0\",\"displayName\":\"Age\"}}   }}}]";
    private static final String EXPECTED_METADATA_RESPONSE = "{\"userPrefs\":{\"age\":{\"dataType\":\"STRING\",\"default\":\"0\",\"displayName\":\"Age\"}},\"hasPrefsToEdit\":true,\"data-snipped\":\"here-for-brevity\"}";
   
    private static final String VALID_METADATA_NO_USERPREFS = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\",\"userPrefs\":{}}}}]";
    private static final String EXPECTED_METADATA_NO_USERPREFS_RESPONSE = "{\"userPrefs\":{},\"hasPrefsToEdit\":false,\"data-snipped\":\"here-for-brevity\"}";
   
    private static final String VALID_METADATA_ONLY_HIDDEN_USERPREFS = "[{\"id\":\"gadgets.metadata\",\"result\"" +
            ":{\"http://www.example.com/gadget.xml\":{\"data-snipped\":\"here-for-brevity\",\"userPrefs\":{\"age\":{\"dataType\":\"HIDDEN\",\"default\":\"0\",\"displayName\":\"Age\"}}   }}}]";
    private static final String EXPECTED_METADATA_ONLY_HIDDEN_USERPREFS_RESPONSE = "{\"userPrefs\":{\"age\":{\"dataType\":\"HIDDEN\",\"default\":\"0\",\"displayName\":\"Age\"}},\"hasPrefsToEdit\":false,\"data-snipped\":\"here-for-brevity\"}";
    
    
    @Before
    public void setup() {
        restOperations = createNiceMock(RestOperations.class);
        gadgetMetadataRepository = new ShindigGadgetMetadataRepository(restOperations, shindigProtocol, shindigRoot);
        shindigUrl = shindigProtocol + "://" + shindigRoot + "/rpc";
    }

    @Test
    public void getGadgetMetadata() {
        expect(restOperations.postForObject(shindigUrl, VALID_SHINDIG_METADATA_RPC_REQUEST, String.class)).andReturn(VALID_METADATA);
        replay(restOperations);

        String result = gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL);
        assertThat(result, is(EXPECTED_METADATA_RESPONSE));
        
        verify(restOperations);
    }
    
    @Test
    public void getGadgetMetadata_noUserPrefs() {
        expect(restOperations.postForObject(shindigUrl, VALID_SHINDIG_METADATA_RPC_REQUEST, String.class)).andReturn(VALID_METADATA_NO_USERPREFS);
        replay(restOperations);

        String result = gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL);
        assertThat(result, is(EXPECTED_METADATA_NO_USERPREFS_RESPONSE));
        
        verify(restOperations);
    }       
    
    @Test
    public void getGadgetMetadata_onlyHiddenUserPrefs() {
        expect(restOperations.postForObject(shindigUrl, VALID_SHINDIG_METADATA_RPC_REQUEST, String.class)).andReturn(VALID_METADATA_ONLY_HIDDEN_USERPREFS);
        replay(restOperations);

        String result = gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL);
        assertThat(result, is(EXPECTED_METADATA_ONLY_HIDDEN_USERPREFS_RESPONSE));
        
        verify(restOperations);
    }      
    
    @Test(expected=IllegalArgumentException.class)
    public void getGadgetMetadata_exceptionParsingResponse() {
        expect(restOperations.postForObject(shindigUrl, VALID_SHINDIG_METADATA_RPC_REQUEST, String.class)).andReturn("BAD RESPONSE STRING");
        replay(restOperations);

        gadgetMetadataRepository.getGadgetMetadata(VALID_GADGET_URL);       
        
        verify(restOperations);
    }   
}
