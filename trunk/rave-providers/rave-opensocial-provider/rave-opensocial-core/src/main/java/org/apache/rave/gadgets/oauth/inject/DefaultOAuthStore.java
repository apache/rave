/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.gadgets.oauth.inject;

import net.oauth.OAuth;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;
import org.apache.commons.io.IOUtils;
import org.apache.rave.gadgets.oauth.service.OAuthConsumerStoreService;
import org.apache.rave.gadgets.oauth.service.OAuthTokenInfoService;
import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.oauth.BasicOAuthStore;
import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;
import org.apache.shindig.gadgets.oauth.OAuthStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link OAuthStore} that retrieves the consumer_key, consumer_secret and key_type from the database
 * <p/>
 * Usage scenario: Rave OpenSocial container is the oauth consumer
 * (oauth data are stored elsewhere, this container tries to fetch data from e.g. Google)
 */
public class DefaultOAuthStore implements OAuthStore {

    @Autowired
    private OAuthConsumerStoreService consumerStoreService;

    @Autowired
    private OAuthTokenInfoService tokenInfoService;

    /**
     * Callback to use when no per-key callback URL is found.
     */
    private String defaultCallbackUrl;

    /*
    * Private key for signing the RSH_PRIVATE oauth requests
    */
    private BasicOAuthStoreConsumerKeyAndSecret defaultKey;

    public DefaultOAuthStore(String defaultCallbackUrl,
                             String pathToPrivateKey,
                             String privateKeyName) throws IOException {
        this.defaultCallbackUrl = defaultCallbackUrl;
        this.defaultKey = loadDefaultKey(pathToPrivateKey, privateKeyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumerInfo getConsumerKeyAndSecret(SecurityToken securityToken, String serviceName,
                                                OAuthServiceProvider provider) throws GadgetException {
        String gadgetUri = securityToken.getAppUrl();
        OAuthConsumerStore consumerStore = consumerStoreService.findByUriAndServiceName(gadgetUri, serviceName);
        if (consumerStore == null) {
            return null;
        }
        OAuthConsumer consumer = createOAuthConsumer(provider, consumerStore);
        String callbackUrl = (consumerStore.getCallbackUrl() != null ?
                consumerStore.getCallbackUrl() : defaultCallbackUrl);

        return new ConsumerInfo(consumer, consumerStore.getKeyName(), callbackUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenInfo getTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo, String serviceName,
                                  String tokenName) throws GadgetException {
        OAuthTokenInfo oAuthTokenInfo = tokenInfoService.findOAuthTokenInfo(
                securityToken.getViewerId(), securityToken.getAppUrl(),
                OAuthTokenInfo.MODULE_ID, tokenName, serviceName);
        if (oAuthTokenInfo == null) {
            return null;
        }

        return new TokenInfo(oAuthTokenInfo.getAccessToken(), oAuthTokenInfo.getTokenSecret(),
                oAuthTokenInfo.getSessionHandle(), oAuthTokenInfo.getTokenExpireMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo, String serviceName,
                             String tokenName, TokenInfo tokenInfo) throws GadgetException {
        OAuthTokenInfo oAuthTokenInfo = new OAuthTokenInfoImpl(securityToken.getAppUrl(),
                serviceName, tokenName, tokenInfo.getAccessToken(), tokenInfo.getSessionHandle(),
                tokenInfo.getTokenSecret(), securityToken.getViewerId(), tokenInfo.getTokenExpireMillis());
        tokenInfoService.saveOAuthTokenInfo(oAuthTokenInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToken(SecurityToken securityToken, ConsumerInfo consumerInfo, String serviceName,
                            String tokenName) throws GadgetException {
        tokenInfoService.deleteOAuthTokenInfo(securityToken.getViewerId(), securityToken.getAppUrl(),
                OAuthTokenInfo.MODULE_ID, tokenName, serviceName);
    }

    /**
     * Creates an {@link OAuthConsumer} based on the OAuth signature method
     *
     * @param provider      {@link net.oauth.OAuthServiceProvider}
     * @param consumerStore {@link org.apache.rave.model.OAuthConsumerStore}
     *                      persistent OAuth consumer keys & secrets
     * @return {@link OAuthConsumer} if the signature method is supported
     */
    private OAuthConsumer createOAuthConsumer(OAuthServiceProvider provider,
                                              OAuthConsumerStore consumerStore) {
        String consumerKey = consumerStore.getConsumerKey();
        String consumerSecret = consumerStore.getConsumerSecret();

        OAuthConsumer consumer;
        switch (consumerStore.getKeyType()) {
            case RSA_PRIVATE:
                consumer = new OAuthConsumer(null, consumerKey, null, provider);
                // The oauth.net java code has lots of magic. By setting this property
                // here, code thousands of lines away knows that the consumerSecret
                // value in the consumer should be treated as
                // an RSA private key and not an HMAC key.
                consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.RSA_SHA1);
                consumer.setProperty(RSA_SHA1.PRIVATE_KEY, defaultKey.getConsumerSecret());
                break;
            case HMAC_SYMMETRIC:
                consumer = new OAuthConsumer(null, consumerKey, consumerSecret, provider);
                consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
                break;
            case PLAINTEXT:
                consumer = new OAuthConsumer(null, consumerKey, consumerSecret, provider);
                consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, "PLAINTEXT");
                break;
            default:
                throw new IllegalArgumentException("Cannot handle keytype " +
                        consumerStore.getKeyType());
        }
        return consumer;
    }


    /**
     * Loads {@link BasicOAuthStoreConsumerKeyAndSecret} needed if there is no specific consumer key and secret
     *
     * @param signingKeyFile location of the signing key file on the classpath or filesystem
     * @param signingKeyName name of the signing key file
     * @return RSA_PRIVATE key and secret read from the classpath or file system
     * @throws java.io.IOException if the file cannot be read
     */
    static BasicOAuthStoreConsumerKeyAndSecret loadDefaultKey(
            String signingKeyFile, String signingKeyName) throws IOException {
        InputStream inputStream = new ClassPathResource(signingKeyFile).getInputStream();
        String privateKey = IOUtils.toString(inputStream);
        privateKey = BasicOAuthStore.convertFromOpenSsl(privateKey);
        return new BasicOAuthStoreConsumerKeyAndSecret(null, privateKey,
                BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE, signingKeyName, null);

    }
}
