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

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.shindig.common.crypto.BlobCrypter;
import org.apache.shindig.gadgets.oauth.OAuthFetcherConfig;
import org.apache.shindig.gadgets.oauth.OAuthModule;
import org.apache.shindig.gadgets.oauth.OAuthRequest;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.sample.oauth.SampleOAuthDataStore;

/**
 * Replacement for the {@link org.apache.shindig.gadgets.oauth.OAuthModule}.
 * We can't extend {@link org.apache.shindig.gadgets.oauth.OAuthModule} and call
 * super.configure, because Guice does not permit bindig the same thing twice.
 * Therefore some duplicate code, but at least no changes in
 * {@link org.apache.shindig.gadgets.oauth.OAuthModule}
 * <p/>
 * OAuthStore is bound to DefaultOAuthStore using Spring
 */
public class OAuthGuiceModule extends AbstractModule {
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bind(BlobCrypter.class).annotatedWith(Names.named(OAuthFetcherConfig.OAUTH_STATE_CRYPTER))
                .toProvider(OAuthModule.OAuthCrypterProvider.class);
        bind(OAuthRequest.class).toProvider(OAuthModule.OAuthRequestProvider.class);

        //Required for SampleOAuthDataStore
        bind(String.class).annotatedWith(Names.named("shindig.canonical.json.db"))
            .toInstance("sampledata/canonicaldb.json");
        bind(OAuthDataStore.class).to(SampleOAuthDataStore.class);
    }
}
