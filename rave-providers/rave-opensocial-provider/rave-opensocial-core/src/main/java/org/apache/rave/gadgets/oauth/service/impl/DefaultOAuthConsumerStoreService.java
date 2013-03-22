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

package org.apache.rave.gadgets.oauth.service.impl;

import org.apache.rave.model.OAuthConsumerStore;
import org.apache.rave.portal.repository.OAuthConsumerStoreRepository;
import org.apache.rave.gadgets.oauth.service.OAuthConsumerStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link OAuthConsumerStoreService}
 */
@Service
public class DefaultOAuthConsumerStoreService implements OAuthConsumerStoreService {

    private OAuthConsumerStoreRepository repository;

    @Autowired
    public DefaultOAuthConsumerStoreService(OAuthConsumerStoreRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthConsumerStore findByUriAndServiceName(String gadgetUri, String serviceName) {
        return repository.findByUriAndServiceName(gadgetUri, serviceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthConsumerStore save(OAuthConsumerStore oAuthConsumerStore) {
        return repository.save(oAuthConsumerStore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(OAuthConsumerStore oAuthConsumerStore) {
        repository.delete(oAuthConsumerStore);
    }
}
