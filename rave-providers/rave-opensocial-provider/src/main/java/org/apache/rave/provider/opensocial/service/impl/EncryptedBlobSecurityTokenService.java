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

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.provider.opensocial.exception.SecurityTokenException;
import org.apache.rave.provider.opensocial.service.SecurityTokenService;
import org.apache.shindig.auth.BlobCrypterSecurityToken;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.common.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

@Service
public class EncryptedBlobSecurityTokenService implements SecurityTokenService {
    private static Logger logger = LoggerFactory.getLogger(EncryptedBlobSecurityTokenService.class);

    public static final String EMBEDDED_KEY_PREFIX = "embedded:";
    public static final String CLASSPATH_KEY_PREFIX = "classpath:";

    private UserService userService;
    private String container;
    private String domain;

    private BlobCrypter blobCrypter;

    @Autowired
    public EncryptedBlobSecurityTokenService(UserService userService,
                                             @Value("${portal.opensocial_security.container}") String container,
                                             @Value("${portal.opensocial_security.domain}") String domain,
                                             @Value("${portal.opensocial_security.encryptionkey}") String encryptionKey) {
        this.userService = userService;
        this.container = container;
        this.domain = domain;

        if (encryptionKey.startsWith(EMBEDDED_KEY_PREFIX)) {
            byte[] key = CharsetUtil.getUtf8Bytes(encryptionKey.substring(EMBEDDED_KEY_PREFIX.length()));
            this.blobCrypter = new BasicBlobCrypter(key);
        } else if (encryptionKey.startsWith(CLASSPATH_KEY_PREFIX)) {
            try {
                File file = new ClassPathResource(encryptionKey.substring(CLASSPATH_KEY_PREFIX.length())).getFile();
                this.blobCrypter = new BasicBlobCrypter(file);
            } catch (IOException e) {
                throw new SecurityException("Unable to load encryption key from classpath resource: " + encryptionKey);
            }
        } else {
            try {
                this.blobCrypter = new BasicBlobCrypter(new File(encryptionKey));
            } catch (IOException e) {
                throw new SecurityException("Unable to load encryption key from file: " + encryptionKey);
            }
        }
    }

    @Override
    public SecurityToken getSecurityToken(RegionWidget regionWidget) throws SecurityTokenException {
        return this.getBlobCrypterSecurityToken(regionWidget);
    }

    @Override
    public String getEncryptedSecurityToken(RegionWidget regionWidget) throws SecurityTokenException {
        String encryptedToken = null;

        try {
            BlobCrypterSecurityToken securityToken = this.getBlobCrypterSecurityToken(regionWidget);
            encryptedToken = this.encryptSecurityToken(securityToken);
        } catch (Exception e) {
            throw new SecurityTokenException("Error creating security token from regionWidget", e);
        }

        return encryptedToken;
    }

    @Override
    public SecurityToken decryptSecurityToken(String encryptedSecurityToken) throws SecurityTokenException {
        SecurityToken securityToken;

        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Decrypting security token: " + encryptedSecurityToken);
            }

            //Remove the header container string and :
            encryptedSecurityToken = encryptedSecurityToken.substring((container + ":").length());

            //Decrypt
            //TODO RAVE-158: This hack is in place until we can get a patch applied to shindig to make the target method public
            Method decryptMethod = BlobCrypterSecurityToken.class.getDeclaredMethod("decrypt", BlobCrypter.class,
                    String.class, String.class, String.class, String.class);
            decryptMethod.setAccessible(true);
            securityToken = (SecurityToken) decryptMethod.invoke(null, blobCrypter, container, domain,
                    encryptedSecurityToken, null);
        } catch (Exception e) {
            throw new SecurityTokenException("Error creating security token from encrypted string: " +
                    encryptedSecurityToken, e);
        }

        return securityToken;
    }

    @Override
    public String refreshEncryptedSecurityToken(String encryptedSecurityToken) throws SecurityTokenException {
        //Decrypt the current token
        SecurityToken securityToken = this.decryptSecurityToken(encryptedSecurityToken);

        //Make sure the person is authorized to refresh this token
        String userId = String.valueOf(userService.getAuthenticatedUser().getEntityId());
        if (!securityToken.getViewerId().equalsIgnoreCase(userId)) {
            throw new SecurityTokenException("Illegal attempt by user " + userId +
                    " to refresh security token with a viewerId of " + securityToken.getViewerId());
        }

        //Create a new RegionWidget instance from it so we can use it to generate a new encrypted token
        RegionWidget regionWidget = new RegionWidget(securityToken.getModuleId(),
                new Widget(-1L, securityToken.getAppUrl()),
                new Region(-1L, new Page(-1L, new User(Long.valueOf(securityToken.getOwnerId()))), -1));

        //Create and return the newly encrypted token
        return getEncryptedSecurityToken(regionWidget);
    }

    private BlobCrypterSecurityToken getBlobCrypterSecurityToken(RegionWidget regionWidget)
            throws SecurityTokenException {
        User user = userService.getAuthenticatedUser();

        BlobCrypterSecurityToken securityToken = new BlobCrypterSecurityToken(blobCrypter, container, domain);
        securityToken.setAppUrl(regionWidget.getWidget().getUrl());
        securityToken.setModuleId(regionWidget.getEntityId());
        securityToken.setOwnerId(String.valueOf(regionWidget.getRegion().getPage().getOwner().getEntityId()));
        securityToken.setViewerId(String.valueOf(user.getEntityId()));
        securityToken.setTrustedJson("");

        if (logger.isTraceEnabled()) {
            logger.trace("Token created for regionWidget " + regionWidget.toString() + " and user " + user.toString());
        }

        return securityToken;
    }

    private String encryptSecurityToken(BlobCrypterSecurityToken securityToken) throws SecurityTokenException {
        String encryptedToken = null;

        try {
            encryptedToken = securityToken.encrypt();
            if (logger.isTraceEnabled()) {
                logger.trace("Encrypted token created from security token: " + securityToken.toString() +
                        " -- encrypted token is: " + encryptedToken);
            }
        } catch (Exception e) {
            throw new SecurityTokenException("Error creating security token from person gadget", e);
        }

        return encryptedToken;
    }
}
