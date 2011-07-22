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

package org.apache.shindig.gadgets.oauth;

import net.oauth.OAuth;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.signature.RSA_SHA1;
import org.apache.commons.io.IOUtils;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.oauth.model.OAuthConsumerStoreDb;
import org.apache.shindig.gadgets.oauth.model.OAuthTokenInfoDb;
import org.apache.shindig.gadgets.oauth.service.ConsumerStoreService;
import org.apache.shindig.gadgets.oauth.service.TokenInfoService;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link OAuthStore} that retrieves the consumer_key, consumer_secret and key_type from the database
 * <p/>
 * Usage scenario: Rave OpenSocial container is the oauth consumer
 * (oauth data are stored elsewhere, this container tries to fetch data from e.g. Google)
 */
public class OAuthStoreDb implements OAuthStore {

    private ConsumerStoreService oAuthStoreService;

    private TokenInfoService tokenInfoService;

    /**
     * Callback to use when no per-key callback URL is found.
     */
    private String defaultCallbackUrl;

    /*
    * Private key for signing the RSH_PRIVATE oauth requests
    */
    private BasicOAuthStoreConsumerKeyAndSecret defaultKey;


    public OAuthStoreDb(String defaultCallbackUrl,
                        String pathToPrivateKey,
                        String privateKeyName,
                        ConsumerStoreService oAuthStoreService,
                        TokenInfoService tokenInfoService) throws IOException {
        this.defaultCallbackUrl = defaultCallbackUrl;
        this.defaultKey = loadDefaultKey(pathToPrivateKey, privateKeyName);
        this.oAuthStoreService = oAuthStoreService;
        this.tokenInfoService = tokenInfoService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumerInfo getConsumerKeyAndSecret(SecurityToken securityToken, String serviceName,
                                                OAuthServiceProvider provider)
            throws GadgetException {
        String gadgetUri = securityToken.getAppUrl();
        OAuthConsumerStoreDb consumerStoreDb = oAuthStoreService.findOAuthConsumerStore(
                gadgetUri, serviceName);
        if (consumerStoreDb == null) {
            return null;
        }

        OAuthConsumer consumer = createOAuthConsumer(provider, consumerStoreDb);
        String callbackUrl = (consumerStoreDb.getCallbackUrl() != null ?
                consumerStoreDb.getCallbackUrl() : defaultCallbackUrl);

        return new ConsumerInfo(consumer, consumerStoreDb.getKeyName(), callbackUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenInfo getTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo,
                                  String serviceName, String tokenName) throws GadgetException {
        OAuthTokenInfoDb info = tokenInfoService.findOAuthTokenInfo(
                securityToken.getViewerId(), securityToken.getAppUrl(),
                OAuthTokenInfoDb.MODULE_ID, tokenName, serviceName);
        if (info == null) {
            return null;
        }

        return new TokenInfo(info.getAccessToken(), info.getTokenSecret(),
                info.getSessionHandle(), info.getTokenExpireMillis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTokenInfo(SecurityToken securityToken, ConsumerInfo consumerInfo,
                             String serviceName, String tokenName, TokenInfo tokenInfo) throws GadgetException {
        OAuthTokenInfoDb tokenInfoDb = new OAuthTokenInfoDb(securityToken,
                serviceName, tokenName, tokenInfo);
        tokenInfoService.saveOAuthTokenInfo(tokenInfoDb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeToken(SecurityToken securityToken, ConsumerInfo consumerInfo,
                            String serviceName, String tokenName) throws GadgetException {
        tokenInfoService.deleteOAuthTokenInfo(
                securityToken.getViewerId(), securityToken.getAppUrl(),
                OAuthTokenInfoDb.MODULE_ID, tokenName, serviceName);
    }

    /**
     * Loads {@link BasicOAuthStoreConsumerKeyAndSecret} needed if there is no specific consumer key and secret
     *
     * @param signingKeyFile location of the signing key file on the classpath or filesystem
     * @param signingKeyName name of the signing key file
     * @return RSA_PRIVATE key and secret read from the classpath or file system
     * @throws IOException if the file cannot be read
     */
    static BasicOAuthStoreConsumerKeyAndSecret loadDefaultKey(
            String signingKeyFile, String signingKeyName) throws IOException {
        InputStream inputStream = new ClassPathResource(signingKeyFile)
                .getInputStream();
        String privateKey = IOUtils.toString(inputStream);
        privateKey = BasicOAuthStore.convertFromOpenSsl(privateKey);
        return new BasicOAuthStoreConsumerKeyAndSecret(null, privateKey,
                BasicOAuthStoreConsumerKeyAndSecret.KeyType.RSA_PRIVATE, signingKeyName, null);

    }

    /**
     * Creates an {@link OAuthConsumer} based on the OAuth signature method
     *
     * @param provider        {@link net.oauth.OAuthServiceProvider}
     * @param consumerStoreDb {@link org.apache.shindig.gadgets.oauth.model.OAuthConsumerStoreDb}
     *                        persistent OAuth consumer keys & secrets
     * @return {@link OAuthConsumer} if the signature method is supported
     */
    private OAuthConsumer createOAuthConsumer(OAuthServiceProvider provider,
                                              OAuthConsumerStoreDb consumerStoreDb) {
        String consumerKey = consumerStoreDb.getConsumerKey();
        String consumerSecret = consumerStoreDb.getConsumerSecret();

        OAuthConsumer consumer;
        switch (consumerStoreDb.getKeyType()) {
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
                        consumerStoreDb.getKeyType());
        }
        return consumer;
    }

}
