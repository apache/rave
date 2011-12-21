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
import org.apache.shindig.social.opensocial.model.MediaItem;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.MediaItemService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class DefaultMediaItemService implements MediaItemService {
    @Override
    public Future<MediaItem> getMediaItem(UserId userId, String appId, String albumId, String mediaItemId, Set<String> fields, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(UserId userId, String appId, String albumId, Set<String> mediaItemIds, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(UserId userId, String appId, String albumId, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Void> deleteMediaItem(UserId userId, String appId, String albumId, String mediaItemId, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Void> createMediaItem(UserId userId, String appId, String albumId, MediaItem mediaItem, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }

    @Override
    public Future<Void> updateMediaItem(UserId userId, String appId, String albumId, String mediaItemId, MediaItem mediaItem, SecurityToken token) throws ProtocolException {

        throw new ProtocolException(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not Implemented");
    }
}
