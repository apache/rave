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
package org.apache.rave.service;

/**
 * Interface for dealing with remote Static Content (such as HTML or JavaScript fragments) so that
 * Rave can render them directly on pages and improve performance by internally caching the content
 */
public interface StaticContentFetcherService {
    /**
     * Returns the content from the cache for the supplied key
     *
     * @param key the cache key of the content artifact to lookup
     * @return String representing the static content
     */
    String getContent(String key);

    /**
     * Refreshes all of the cached StaticContent artifacts currently in the cache
     */
    void refreshAll();

    /**
     * Refresh a single StaticContent artifact in the cache based on the supplied key
     * @param key the cache key of the content artifact to refresh
     */
    void refresh(String key);

    /**
     * Registers a StaticContentFetcher consumer with this StaticContentFetcherService.
     * StaticContentFetcherConsumers will have their notify method executed when the content
     * has been updated.
     *
     * @param consumer
     */
    void registerConsumer(StaticContentFetcherConsumer consumer);

    /**
     * Un-registers a StaticContentFetcherConsumer from this StaticContentFetcherService.
     * StaticContentFetcherConsumers un-registered will no longer have their notify method executed when
     * the content has been updated
     *
     * @param consumer
     */
    void unregisterConsumer(StaticContentFetcherConsumer consumer);
}
