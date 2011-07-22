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

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.model.*;
import org.apache.shindig.social.opensocial.spi.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Dummy implementation of Shindig OpenSocial SPIs.
 * 
 * This class just implements services in a dummy way, which makes Shindig not complain. If a certain service is
 * implemented meaningfully, a new class should be created for it. Read class <code>PersonServiceDb</code> for an
 * example.
 * 
 */
public class JPAOpenSocialService implements MediaItemService, MessageService, AlbumService, ActivityStreamService {
    
    // @Override
    public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds, GroupId groupId,
            CollectionOptions collectionOptions, Set<String> fields, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<ActivityEntry>> getActivityEntries(Set<UserId> userIds, GroupId groupId,
            String appId, Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<ActivityEntry>> getActivityEntries(UserId userId, GroupId groupId, String appId,
            Set<String> fields, CollectionOptions options, Set<String> activityIds, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ActivityEntry> getActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields,
            String activityId, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> deleteActivityEntries(UserId userId, GroupId groupId, String appId, Set<String> activityIds,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ActivityEntry> updateActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields,
            ActivityEntry activity, String activityId, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ActivityEntry> createActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields,
            ActivityEntry activity, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Album> getAlbum(UserId userId, String appId, Set<String> fields, String albumId, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<Album>> getAlbums(UserId userId, String appId, Set<String> fields,
            CollectionOptions options, Set<String> albumIds, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<Album>> getAlbums(Set<UserId> userIds, GroupId groupId, String appId,
            Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> deleteAlbum(UserId userId, String appId, String albumId, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> createAlbum(UserId userId, String appId, Album album, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> updateAlbum(UserId userId, String appId, Album album, String albumId, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<MessageCollection>> getMessageCollections(UserId userId, Set<String> fields,
            CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<MessageCollection> createMessageCollection(UserId userId, MessageCollection msgCollection,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> modifyMessageCollection(UserId userId, MessageCollection msgCollection, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> deleteMessageCollection(UserId userId, String msgCollId, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<Message>> getMessages(UserId userId, String msgCollId, Set<String> fields,
            List<String> msgIds, CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> createMessage(UserId userId, String appId, String msgCollId, Message message,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> deleteMessages(UserId userId, String msgCollId, List<String> ids, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> modifyMessage(UserId userId, String msgCollId, String messageId, Message message,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<MediaItem> getMediaItem(UserId userId, String appId, String albumId, String mediaItemId,
            Set<String> fields, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(UserId userId, String appId, String albumId,
            Set<String> mediaItemIds, Set<String> fields, CollectionOptions options, SecurityToken token)
            throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(UserId userId, String appId, String albumId,
            Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<RestfulCollection<MediaItem>> getMediaItems(Set<UserId> userIds, GroupId groupId, String appId,
            Set<String> fields, CollectionOptions options, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> deleteMediaItem(UserId userId, String appId, String albumId, String mediaItemId,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> createMediaItem(UserId userId, String appId, String albumId, MediaItem mediaItem,
            SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Void> updateMediaItem(UserId userId, String appId, String albumId, String mediaItemId,
            MediaItem mediaItem, SecurityToken token) throws ProtocolException {
        // TODO Auto-generated method stub
        return null;
    }
}
