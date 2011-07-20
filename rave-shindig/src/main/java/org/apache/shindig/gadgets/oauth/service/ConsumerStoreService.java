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

package org.apache.shindig.gadgets.oauth.service;

import org.apache.shindig.gadgets.oauth.model.OAuthConsumerStoreDb;

/**
 * Service to handle OAuth consumer info
 */
public interface ConsumerStoreService {

    /**
     * Fetches {@link OAuthConsumerStoreDb} based on the gadget location and the service provider
     *
     * @param gadgetUri   location of the gadget definition
     * @param serviceName name of the service provider
     * @return {@link OAuthConsumerStoreDb} or {@literal null} if none matches the criteria
     */
    public OAuthConsumerStoreDb findOAuthConsumerStore(String gadgetUri, String serviceName);

    /**
     * Persists {@link OAuthConsumerStoreDb}
     *
     * @param consumerStoreDb {@link OAuthConsumerStoreDb} to store
     */
    public void save(OAuthConsumerStoreDb consumerStoreDb);

    /**
     * Removes the {@link OAuthConsumerStoreDb} from the database
     * @param consumerStoreDb {@link OAuthConsumerStoreDb} to delete
     */
    public void delete(OAuthConsumerStoreDb consumerStoreDb);
}
