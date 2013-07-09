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

package org.apache.rave.provider.opensocial.service;

import org.apache.rave.rest.model.RegionWidget;

public interface OpenSocialService {
    /**
     * Fetches gadget metadata for the specified gadget URL.
     *
     * @param gadgetUrl The gadget to fetch metadata for.
     * @return The raw JSON response from the Shindig metadata RPC call.
     */
    String getGadgetMetadata(String gadgetUrl);

    /**
     * Gets a security token for a new instance (RegionWidget) of the given url
     * @param widget the gadget URL to create a new instance for
     * @return a valid, encrypted securityToken
     */
    String getEncryptedSecurityToken(String pageId, String widget);

    /**
     * Gets a security token for the given RegionWidget
     * @param item the region widget to get a security token for
     * @return a valid, encrypted securityToken
     */
    String getEncryptedSecurityToken(RegionWidget item);
}