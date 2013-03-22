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
 * The invitation status of a page - either its the owner, or a shared page
 * which is pending (not yet accepted) refused or accepted by the shared to user  
 */
public enum PageInvitationStatus {
    OWNER("owner"),
    PENDING("pending"),
    REFUSED("refused"),
    ACCEPTED("accepted");

    private String pageStatus;
    private static final Map<String, PageInvitationStatus> lookup = new HashMap<String, PageInvitationStatus>();

    static {
        for (PageInvitationStatus pt : PageInvitationStatus.values()) {
            lookup.put(pt.toString(), pt);
        }
    }

    private PageInvitationStatus(String pageStatus) {
        this.pageStatus = pageStatus;
    }

    public String getPageStatus() {
        return pageStatus;
    }

    @Override
    public String toString() {
        return pageStatus;
    }

    public static PageInvitationStatus get(String pageStatus) {
        return lookup.get(pageStatus);
    }
}
