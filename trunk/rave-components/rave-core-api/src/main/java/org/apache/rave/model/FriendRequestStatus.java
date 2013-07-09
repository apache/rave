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
package org.apache.rave.model;

import java.util.HashMap;
import java.util.Map;
/**
 * The friend request status of a user - which is pending (not yet accepted) or accepted by the friend
 */
public enum FriendRequestStatus {
    SENT("sent"),
    RECEIVED("received"),
    ACCEPTED("accepted");

    private String requestStatus;
    private static final Map<String, FriendRequestStatus> lookup = new HashMap<String, FriendRequestStatus>();

    static {
        for (FriendRequestStatus pt : FriendRequestStatus.values()) {
            lookup.put(pt.toString(), pt);
        }
    }

    private FriendRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    @Override
    public String toString() {
        return requestStatus;
    }

    public static FriendRequestStatus get(String requestStatus) {
        return lookup.get(requestStatus);
    }
}
