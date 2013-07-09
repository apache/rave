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

import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.WidgetRepository;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
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
    private Widget bogusWidget;

    private final String VALID_REGION_WIDGET_ID = "1";
    private final String VALID_USER_ID = "1";
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

        validPerson = new UserImpl(VALID_USER_ID, VALID_USER_NAME);

        validPage = new PageImpl("1", validPerson.getId());
        validRegion = new RegionImpl("1", validPage, 1);
        validPage.setRegions(Arrays.asList(validRegion));

        validWidget = new WidgetImpl("1", VALID_URL);
        validWidget.setType("OpenSocial");
        validWidget.setTitle("Widget Title");

        bogusWidget = new WidgetImpl("-1", VALID_URL);

        validRegionWidget = new RegionWidgetImpl(VALID_REGION_WIDGET_ID, validWidget.getId(), validRegion);
        validRegion.setRegionWidgets(Arrays.asList(validRegionWidget));
    }

    @Test
    public void getSecurityToken_validWidget() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        expect(userService.getUserById(VALID_USER_ID)).andReturn(validPerson);
        replay(userService);

        SecurityToken securityToken = securityTokenService.getSecurityToken(validRegionWidget, validWidget);
        validateSecurityToken(securityToken);
    }

    @Test
    public void getSecurityToken_validWidget_ownerIsNotViewer() throws SecurityTokenException {
        String expectedOwnerId = "99999";
        String expected = "Expected";
        validPage.setOwnerId(expectedOwnerId);

        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        expect(userService.getUserById(expectedOwnerId)).andReturn(new UserImpl(expectedOwnerId, expected));
        replay(userService);

        SecurityToken securityToken = securityTokenService.getSecurityToken(validRegionWidget, validWidget);
        validateSecurityToken(securityToken, expected);
    }

    @Test
    public void getEncryptedSecurityToken_validWidget_validToken() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        expect(userService.getUserById(VALID_USER_ID)).andReturn(validPerson);
        replay(userService);

        String token = securityTokenService.getEncryptedSecurityToken(validRegionWidget, validWidget);
        assertNotNull(token);
    }

    @Test
    public void decryptSecurityToken_validTokenString() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        expect(userService.getUserById(VALID_USER_ID)).andReturn(validPerson);
        replay(userService);

        String encryptedToken = securityTokenService.getEncryptedSecurityToken(validRegionWidget, validWidget);
        assertNotNull(encryptedToken);

        SecurityToken securityToken = securityTokenService.decryptSecurityToken(encryptedToken);
        validateSecurityToken(securityToken);
    }

    @Test
    public void refreshEncryptedSecurityToken_validTokenString() throws SecurityTokenException {
        expect(userService.getAuthenticatedUser()).andReturn(validPerson).anyTimes();
        expect(userService.getUserByUsername(VALID_USER_NAME)).andReturn(validPerson).anyTimes();
        expect(userService.getUserById(VALID_USER_ID)).andReturn(validPerson).anyTimes();
        replay(userService);

        String encryptedToken = securityTokenService.getEncryptedSecurityToken(validRegionWidget, validWidget);
        assertNotNull(encryptedToken);

        encryptedToken = securityTokenService.refreshEncryptedSecurityToken(encryptedToken);

        SecurityToken securityToken = securityTokenService.decryptSecurityToken(encryptedToken);
        validateSecurityToken(securityToken);
    }

    private void validateSecurityToken(SecurityToken securityToken) {
        validateSecurityToken(securityToken, VALID_USER_NAME);
    }

    private void validateSecurityToken(SecurityToken securityToken, String expectedOwnerId) {
        assertNotNull(securityToken);
        assertEquals(Long.parseLong(VALID_REGION_WIDGET_ID), securityToken.getModuleId());
        assertEquals(expectedOwnerId, securityToken.getOwnerId());
        assertEquals(VALID_USER_NAME, securityToken.getViewerId());
        assertEquals(VALID_URL, securityToken.getAppUrl());
    }
}
