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
package org.apache.rave.portal.repository;

import org.apache.rave.model.ApplicationData;
import org.apache.rave.repository.Repository;

import java.util.List;

public interface ApplicationDataRepository extends Repository<ApplicationData> {

    /**
     * Gets the application data for the given users and application
     *
     * @param userIds The users
     * @param appId   The application
     * @return The application data for the specified users, or an empty list if none is found
     */
    List<ApplicationData> getApplicationData(List<String> userIds, String appId);

    /**
     * Gets the application data for the given user and application
     *
     * @param personId The user
     * @param appId    The application
     * @return The application data, or null if not found
     */
    ApplicationData getApplicationData(String personId, String appId);
}
