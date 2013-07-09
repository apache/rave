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

package org.apache.rave.model;


import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public interface OAuthConsumerStore {
    /**
     * enum of KeyType's
     */
    public static enum KeyType {
        HMAC_SYMMETRIC, RSA_PRIVATE, PLAINTEXT
    }

    String getId();
    void setId(String id);

    String getGadgetUri();
    void setGadgetUri(String gadgetUri);

    String getServiceName();
    void setServiceName(String serviceName);

    String getConsumerKey();
    void setConsumerKey(String consumerKey);

    String getConsumerSecret();
    void setConsumerSecret(String consumerSecret);

    OAuthConsumerStore.KeyType getKeyType();
    void setKeyType(OAuthConsumerStore.KeyType keyType);

    String getKeyName();
    void setKeyName(String keyName);

    String getCallbackUrl();
    void setCallbackUrl(String callbackUrl);
}
