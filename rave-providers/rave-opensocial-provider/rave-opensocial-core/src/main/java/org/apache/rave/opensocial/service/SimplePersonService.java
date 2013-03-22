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
package org.apache.rave.opensocial.service;

import org.apache.rave.model.Person;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;

import java.util.List;
import java.util.Set;

public interface SimplePersonService {
    /**
     * Returns a list of people that correspond to the passed in person ids.
     *
     * @param userIds           A set of users
     * @param groupId           The group
     * @param collectionOptions How to filter, sort and paginate the collection being fetched
     * @param token             The gadget token
     * @return a list of people
     */
    List<Person> getPeople(Set<UserId> userIds, GroupId groupId, CollectionOptions collectionOptions, SecurityToken token);
}
