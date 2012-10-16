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

import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.repository.PortalPreferenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoDbPortalPreferenceRepository implements PortalPreferenceRepository {
    @Override
    public List<PortalPreference> getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PortalPreference getByKey(String key) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Class<? extends PortalPreference> getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PortalPreference get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PortalPreference save(PortalPreference item) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(PortalPreference item) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
