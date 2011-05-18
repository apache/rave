/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.rave.os;

import org.apache.shindig.social.opensocial.jpa.spi.ActivityServiceDb;
import org.apache.shindig.social.opensocial.jpa.spi.AppDataServiceDb;
import org.apache.shindig.social.opensocial.jpa.spi.PersonServiceDb;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.ActivityStreamService;
import org.apache.shindig.social.opensocial.spi.AlbumService;
import org.apache.shindig.social.opensocial.spi.AppDataService;
import org.apache.shindig.social.opensocial.spi.MediaItemService;
import org.apache.shindig.social.opensocial.spi.MessageService;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.sample.oauth.SampleOAuthDataStore;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Provides bindings for implementations of social API classes using JPA.
 */
public class JPAOpenSocialModule extends AbstractModule {
  
    @Override
    protected void configure() {
        // FIXME
        // This line is copied from Shindig SampleModule.java.
        // If this line is not included here, tomcat gives error when it's started.
        // It seems that other modules depend on this the binding.
        bind(String.class).annotatedWith(Names.named("shindig.canonical.json.db"))
            .toInstance("sampledata/canonicaldb.json");

        bind(PersonService.class).to(PersonServiceDb.class);
        bind(ActivityService.class).to(ActivityServiceDb.class);
        bind(ActivityStreamService.class).to(JPAOpenSocialService.class);
        bind(AlbumService.class).to(JPAOpenSocialService.class);
        bind(MediaItemService.class).to(JPAOpenSocialService.class);
        bind(AppDataService.class).to(AppDataServiceDb.class);
        bind(MessageService.class).to(JPAOpenSocialService.class);
        bind(OAuthDataStore.class).to(SampleOAuthDataStore.class);
    }
}
