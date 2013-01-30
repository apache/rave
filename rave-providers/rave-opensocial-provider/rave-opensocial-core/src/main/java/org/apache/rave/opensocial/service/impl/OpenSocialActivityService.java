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

package org.apache.rave.opensocial.service.impl;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.spi.ActivityService;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class OpenSocialActivityService implements ActivityService {

    @Override
    public Future<RestfulCollection<Activity>> getActivities(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {

      throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<RestfulCollection<Activity>> getActivities(UserId userId, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, Set<String> activityIds, SecurityToken token) throws ProtocolException {

      throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Activity> getActivity(UserId userId, GroupId groupId, String appId, Set<String> fields, String activityId, SecurityToken token) throws ProtocolException {

      throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Void> deleteActivities(UserId userId, GroupId groupId, String appId, Set<String> activityIds, SecurityToken token) throws ProtocolException {

      throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Void> createActivity(UserId userId, GroupId groupId, String appId, Set<String> fields, Activity activity, SecurityToken token) throws ProtocolException {

       throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }
}
