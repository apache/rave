/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.OAuthTokenInfo;
import org.apache.rave.portal.repository.OAuthTokenInfoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MongoDbOAuthTokenInfoRepository implements OAuthTokenInfoRepository {
    @Override
    public OAuthTokenInfo findOAuthTokenInfo(String userId, String appUrl, String moduleId, String tokenName, String serviceName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends OAuthTokenInfo> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OAuthTokenInfo get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OAuthTokenInfo save(OAuthTokenInfo item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(OAuthTokenInfo item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
