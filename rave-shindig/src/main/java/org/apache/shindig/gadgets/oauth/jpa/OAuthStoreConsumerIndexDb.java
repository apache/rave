/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.shindig.gadgets.oauth.jpa;

import org.apache.shindig.social.opensocial.jpa.api.DbObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Db persistent store for OAuth Consumer info based on
 * {@link org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerIndex}
 * <p/>
 * TODO: set key on combination of gadgetUri & service name
 */
@Entity
@Table(name = "oauth_store_consumer_index")
public class OAuthStoreConsumerIndexDb implements DbObject {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "oid")
    private long objectId;

    /**
     * URI where the gadget is hosted, e.g. http://www.example.com/mygadget.xml
     */
    @Column(name = "gadget_uri", length = 512)
    private String gadgetUri;

    /**
     * Name of the oAuth service, matches /Module/ModulePrefs/OAuth/Service/@name
     * in a gadget definition
     */
    @Column(name = "service_name")
    private String serviceName;

    /**
     * {@inheritDoc}
     */
    @Override
    public long getObjectId() {
        return objectId;
    }

    public String getGadgetUri() {
        return gadgetUri;
    }

    public void setGadgetUri(String gadgetUri) {
        this.gadgetUri = gadgetUri;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
