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
package org.apache.shindig.social.opensocial.jpa.spi;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.apache.rave.os.JPAOpenSocialService;
import org.apache.shindig.social.opensocial.jpa.openjpa.OpenJPAEntityManagerProvider;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.spi.*;
import org.apache.shindig.social.sample.oauth.SampleOAuthDataStore;

import javax.persistence.EntityManager;

/**
 *
 */
public class JPASocialModule extends AbstractModule {

  private EntityManager entityManager;

  /**
   *
   */
  public JPASocialModule() {
      this(null);
  }

  /**
   *
   */
  public JPASocialModule(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * {@inheritDoc}
   *
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    if (entityManager == null) {
      bind(EntityManager.class).toProvider(OpenJPAEntityManagerProvider.class)
          .in(Scopes.SINGLETON);
    } else {
      bind(EntityManager.class).toInstance(this.entityManager);
    }
    bind(PersonService.class).to(PersonServiceDb.class);
    bind(ActivityService.class).to(ActivityServiceDb.class);
    bind(ActivityStreamService.class).to(JPAOpenSocialService.class);
    bind(AlbumService.class).to(JPAOpenSocialService.class);
    bind(MediaItemService.class).to(JPAOpenSocialService.class);
    bind(AppDataService.class).to(AppDataServiceDb.class);
    bind(MessageService.class).to(JPAOpenSocialService.class);
    bind(OAuthDataStore.class).to(SampleOAuthDataStore.class);
    
    bind(String.class).annotatedWith(Names.named("shindig.canonical.json.db"))
    .toInstance("sampledata/canonicaldb.json");
  }
}
