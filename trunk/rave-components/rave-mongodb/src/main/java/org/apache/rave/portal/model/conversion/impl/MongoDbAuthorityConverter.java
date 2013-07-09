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

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.Authority;
import org.apache.rave.portal.model.MongoDbAuthority;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.springframework.stereotype.Component;

@Component
public class MongoDbAuthorityConverter implements HydratingModelConverter<Authority, MongoDbAuthority> {
    @Override
    public void hydrate(MongoDbAuthority dehydrated) {
        //NOOP
    }

    @Override
    public Class<Authority> getSourceType() {
        return Authority.class;
    }

    @Override
    public MongoDbAuthority convert(Authority source) {
        MongoDbAuthority converted = source instanceof MongoDbAuthority ? (MongoDbAuthority) source : new MongoDbAuthority();
        converted.setAuthority(source.getAuthority());
        converted.setDefaultForNewUser(source.isDefaultForNewUser());
        return converted;
    }
}
