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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Data structure representing and OAuth consumer key and secret
 * Based on {@link org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret}
 */
@Entity
@Table(name = "oauth_store_consumer_key_secret")
public class OAuthStoreConsumerKeyAndSecretDb implements DbObject {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "oid")
    private long objectId;

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
     * Type of key
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


    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public static enum KeyType {HMAC_SYMMETRIC, RSA_PRIVATE}

    /**
     * {@inheritDoc}
     */
    @Override
    public long getObjectId() {
        return objectId;
    }
}
