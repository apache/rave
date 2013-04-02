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

package org.apache.rave.portal.model;

import org.apache.rave.model.OAuthConsumerStore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 * Persistent store for OAuth consumer key & secrets.
 * Equivalent of:
 * <pre>
 * {
 *    "http://localhost:8080/samplecontainer/examples/oauth.xml" : {
 *      "" : {
 *          "consumer_key" : "gadgetConsumer",
 *          "consumer_secret" : "gadgetSecret",
 *          "key_type" : "HMAC_SYMMETRIC"
 *      }
 *  },
 *   "http://localhost:8080/samplecontainer/examples/shindigoauth.xml" : {
 *      "shindig" : {
 *          "consumer_key" : "http://localhost:8080/samplecontainer/examples/shindigoauth.xml",
 *          "consumer_secret" : "secret",
 *          "key_type" : "HMAC_SYMMETRIC"
 *      }
 *  }
 * }
 * </pre>
 */
@Entity
@Table(name = "oauth_consumer_store",
        uniqueConstraints = @UniqueConstraint(columnNames = {"gadget_uri", "service_name"}))
@NamedQueries(value = {
        @NamedQuery(name = JpaOAuthConsumerStore.FIND_BY_URI_AND_SERVICE_NAME,
                query = "SELECT cs FROM JpaOAuthConsumerStore cs WHERE cs.gadgetUri = :gadgetUriParam AND cs.serviceName = :serviceNameParam")
        })
public class JpaOAuthConsumerStore implements BasicEntity, OAuthConsumerStore {

    public static final String FIND_BY_URI_AND_SERVICE_NAME = "OAuthConsumerStore.findByUriAndServiceName";
    public static final String GADGET_URI_PARAM = "gadgetUriParam";
    public static final String SERVICE_NAME_PARAM = "serviceNameParam";
    private static final int HASH_START = 7;
    private static final int HASH_INCREASE = 67;

    /**
     * The internal object ID used for references to this object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "consumerStoreIdGenerator")
    @TableGenerator(name = "consumerStoreIdGenerator", table = "RAVE_SHINDIG_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "oauth_consumer_store", allocationSize = 1, initialValue = 1)
    @Column(name = "entity_id")
    private Long entityId;

    /**
     * URI where the gadget is hosted, e.g. http://www.example.com/mygadget.xml
     */
    @Column(name = "gadget_uri")
    private String gadgetUri;

    /**
     * Name of the oAuth service, matches /Module/ModulePrefs/OAuth/Service/@name
     * in a gadget definition
     */
    @Column(name = "service_name")
    private String serviceName;


    /**
     * Value for oauth_consumer_key
     */
    @Column(name = "consumer_key")
    private String consumerKey;

    /**
     * HMAC secret, or RSA private key, depending on KeyType
     */
    @Column(name = "consumer_secret")
    private String consumerSecret;

    /**
     * Type of key, also known as "OAuth signature method"
     */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "key_type")
    private KeyType keyType;

    /**
     * Name of public key to use with xoauth_public_key parameter.
     * May be {@literal null}.
     */
    @Column(name = "key_name")
    private String keyName;

    /**
     * Callback URL associated with this consumer key
     * May be {@literal null}.
     */
    @Column(name = "callback_url")
    private String callbackUrl;

    /*
    * {@inheritDoc}
    */
    @Override
    public String getId() {
        return getEntityId().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {
        setEntityId(id == null ? null : Long.parseLong(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getEntityId() {
        return entityId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String getGadgetUri() {
        return gadgetUri;
    }

    @Override
    public void setGadgetUri(String gadgetUri) {
        this.gadgetUri = gadgetUri;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getConsumerKey() {
        return consumerKey;
    }

    @Override
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @Override
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @Override
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @Override
    public KeyType getKeyType() {
        return keyType;
    }

    @Override
    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    @Override
    public String getKeyName() {
        return keyName;
    }

    @Override
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @Override
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JpaOAuthConsumerStore other = (JpaOAuthConsumerStore) obj;
        if (this.entityId != other.entityId && (this.entityId == null || !this.entityId.equals(other.entityId))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = HASH_START;
        hash = HASH_INCREASE * hash + (this.entityId != null ? this.entityId.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OAuthConsumerStore{" +
                "entityId=" + entityId +
                ", gadgetUri='" + gadgetUri + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                ", keyType=" + keyType +
                ", keyName='" + keyName + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                '}';
    }
}
