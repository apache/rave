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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.ApplicationData;

import java.util.Map;

public class ApplicationDataImpl implements ApplicationData {
    private String id;
    private String userId;
    private String appUrl;
    private Map<String, String> data;

    public ApplicationDataImpl() {}

    public ApplicationDataImpl(String id) {
        this.id = id;
    }

    public ApplicationDataImpl(String id, String userId, String appUrl, Map<String, String> data) {
        this.id = id;
        this.userId = userId;
        this.appUrl = appUrl;
        this.data = data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getAppUrl() {
        return appUrl;
    }

    @Override
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

    @Override
    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
