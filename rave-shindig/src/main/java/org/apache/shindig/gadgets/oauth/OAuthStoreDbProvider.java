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

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.spi.Message;
import org.apache.rave.os.DatabasePopulateContextListener;
import org.apache.shindig.gadgets.oauth.service.ConsumerStoreService;
import org.apache.shindig.gadgets.oauth.service.TokenInfoService;
import org.apache.shindig.gadgets.oauth.service.impl.ConsumerStoreServiceDbImpl;
import org.apache.shindig.gadgets.oauth.service.impl.TokenInfoServiceDbImpl;
import org.apache.shindig.social.opensocial.jpa.openjpa.OpenJPADbModule;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;

/**
 * {@link com.google.inject.Singleton} {@link Provider} for {@link OAuthStoreDb}
 */
@Singleton
public class OAuthStoreDbProvider implements Provider<OAuthStore> {
    private static final String OAUTH_CALLBACK_URL = "shindig.signing.global-callback-url";
    private static final String OAUTH_SIGNING_KEY_FILE = "shindig.signing.key-file";
    private static final String OAUTH_SIGNING_KEY_NAME = "shindig.signing.key-name";

    private final OAuthStoreDb store;
    private ConsumerStoreService consumerStoreService;
    private TokenInfoService tokenInfoService;

    @Inject
    public OAuthStoreDbProvider(@Named(OAUTH_CALLBACK_URL) String defaultCallbackUrl,
                                @Named(OAUTH_SIGNING_KEY_FILE) String pathToPrivateKey,
                                @Named(OAUTH_SIGNING_KEY_NAME) String privateKeyName) {
        super();
        try {
            Injector dbInjector = Guice.createInjector(new OpenJPADbModule());
            this.store = new OAuthStoreDb(defaultCallbackUrl, pathToPrivateKey, privateKeyName,
                    getConsumerStoreService(dbInjector), getTokenInfoService(dbInjector));
        } catch (IOException e) {
            throw new CreationException(Arrays.asList(
                    new Message("Could not initialize OAuthStore " + e.getMessage())
            ));
        }
    }

    /**
     * @return {@link ConsumerStoreService} that can retrieve OAuth consumer keys & secretss
     * @param dbInjector {@link Injector} for persistence
     */
    protected ConsumerStoreService getConsumerStoreService(Injector dbInjector) {
        if (consumerStoreService == null) {
            consumerStoreService = dbInjector.getInstance(ConsumerStoreServiceDbImpl.class);
        }
        return consumerStoreService;
    }

    /**
     * @return {@link TokenInfoService} that can retrieve TokenInfo from the db
     * @param dbInjector {@link Injector} for persistence
     */
    protected TokenInfoService getTokenInfoService(Injector dbInjector) {
        if (tokenInfoService == null) {
            tokenInfoService = dbInjector.getInstance(TokenInfoServiceDbImpl.class);
        }
        return tokenInfoService;
    }

    /**
     * @return {@link EntityManager} for this provider
     */
    protected static EntityManager getEntityManager() {
        return DatabasePopulateContextListener.getEntityManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthStore get() {
        return store;
    }
}
