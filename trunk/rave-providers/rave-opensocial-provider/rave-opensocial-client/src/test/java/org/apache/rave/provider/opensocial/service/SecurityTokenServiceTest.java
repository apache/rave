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

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.provider.opensocial.service.impl.EncryptedBlobSecurityTokenService;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.auth.SecurityTokenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class SecurityTokenServiceTest {
    private UserService userService;
    private SecurityTokenService securityTokenService;
    private String encryptionKey;

    private User validPerson;
    private Page validPage;
    private Region validRegion;
    private RegionWidget validRegionWidget;
    private Widget validWidget;

    private final Long VALID_REGION_WIDGET_ID = 1L;
    private final Long VALID_USER_ID = 1L;
    private final String VALID_USER_NAME = "jdoe";
    private final String VALID_URL = "http://example.com/test.xml";

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        return Arrays.asList(new Object[][]{
                //Run the tests once using an embedded key
                {EncryptedBlobSecurityTokenService.EMBEDDED_KEY_PREFIX + "insecure-security-token-test-key"},

                //And again with a classpath reference to an external key file
                {EncryptedBlobSecurityTokenService.CLASSPATH_KEY_PREFIX + "security_token_encryption_key.txt"},

                //And again with a direct filesystem reference to an external key file
                {new ClassPathResource("security_token_encryption_key.txt").getFile().getAbsolutePath()}
        });
    }

    public SecurityTokenServiceTest(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @Before
    public void setup() throws MalformedURLException {
        userService = createMock(UserService.class);
        securityTokenService = new EncryptedBlobSecurityTokenService(userService, "default", "default",
                encryptionKey);

        validPerson = new User(VALID_USER_ID, VALID_USER_NAME);

        validPage = new Page(1L, validPerson);
        validRegion = new Region(1L, validPage, 1);
        validPage.setRegions(Arrays.asList(validRegion));

        validWidget = new Widget(1L, VALID_URL);
        validWidget.setType("OpenSocial");
        validWidget.setTitle("Widget Title");

        validRegionWidget = new RegionWidget(VALID_REGION_WIDGET_ID, validWidget, validRegion);
        validRegion.setRegionWidgets(Arrays.asList(validRegionWidget));
    }

    @Test
    public void getSecurityToken_validWidget() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        replay(userService);

        SecurityToken securityToken = securityTokenService.getSecurityToken(validRegionWidget);
        validateSecurityToken(securityToken);
    }

    @Test
    public void getSecurityToken_validWidget_ownerIsNotViewer() throws SecurityTokenException {
        Long expectedOwnerId = 99999L;
        validPage.setOwner(new User(expectedOwnerId));

        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        replay(userService);

        SecurityToken securityToken = securityTokenService.getSecurityToken(validRegionWidget);
        validateSecurityToken(securityToken, expectedOwnerId);
    }

    @Test
    public void getEncryptedSecurityToken_validWidget_validToken() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        replay(userService);

        String token = securityTokenService.getEncryptedSecurityToken(validRegionWidget);
        assertNotNull(token);
    }

    @Test
    public void decryptSecurityToken_validTokenString() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        replay(userService);

        String encryptedToken = securityTokenService.getEncryptedSecurityToken(validRegionWidget);
        assertNotNull(encryptedToken);

        SecurityToken securityToken = securityTokenService.decryptSecurityToken(encryptedToken);
        validateSecurityToken(securityToken);
    }

    @Test
    public void refreshEncryptedSecurityToken_validTokenString() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        replay(userService);

        String encryptedToken = securityTokenService.getEncryptedSecurityToken(validRegionWidget);
        assertNotNull(encryptedToken);

        encryptedToken = securityTokenService.refreshEncryptedSecurityToken(encryptedToken);

        SecurityToken securityToken = securityTokenService.decryptSecurityToken(encryptedToken);
        validateSecurityToken(securityToken);
    }

    private void validateSecurityToken(SecurityToken securityToken) {
        validateSecurityToken(securityToken, VALID_USER_ID);
    }

    private void validateSecurityToken(SecurityToken securityToken, Long expectedOwnerId) {
        assertNotNull(securityToken);
        assertEquals(VALID_REGION_WIDGET_ID.longValue(), securityToken.getModuleId());
        assertEquals(expectedOwnerId, Long.valueOf(securityToken.getOwnerId()));
        assertEquals(VALID_USER_ID, Long.valueOf(securityToken.getViewerId()));
        assertEquals(VALID_URL, securityToken.getAppUrl());
    }
}
