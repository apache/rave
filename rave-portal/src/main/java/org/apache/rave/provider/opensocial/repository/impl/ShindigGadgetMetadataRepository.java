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

package org.apache.rave.provider.opensocial.repository.impl;

import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

@Repository
public class ShindigGadgetMetadataRepository implements GadgetMetadataRepository {
    private static Logger logger = LoggerFactory.getLogger(ShindigGadgetMetadataRepository.class);

    private RestOperations restOperations;
    private String shindigUrl;

    @Autowired
    public ShindigGadgetMetadataRepository(@Qualifier(value = "jsonStringCompatibleRestTemplate") RestOperations restOperations,
                                           @Value("${portal.opensocial_engine.protocol}") String shindigProtocol,
                                           @Value("${portal.opensocial_engine.root}") String shindigRoot) {
        this.restOperations = restOperations;
        this.shindigUrl = shindigProtocol + "://" + shindigRoot + "/rpc";
    }

    @Override
    public String getGadgetMetadata(String gadgetUrl) {
        //generate the json request to be sent to the shindig RPC service
        JSONArray rpcArray = new JSONArray();
        try {
            JSONObject fetchMetadataRpcOperation = new JSONObject()
                    .put("method", "gadgets.metadata")
                    .put("id", "gadgets.metadata")
                    .put("params", new JSONObject()
                            .put("container", "default")

                            .append("ids", gadgetUrl)

                            .append("fields", "iframeUrl")
                            .append("fields", "modulePrefs.*")
                            .append("fields", "needsTokenRefresh")
                            .append("fields", "userPrefs.*")
                            .append("fields", "views.preferredHeight")
                            .append("fields", "views.preferredWidth")
                            .append("fields", "expireTimeMs")
                            .append("fields", "responseTimeMs")

                            .put("userId", "@viewer")
                            .put("groupId", "@self")
                    );

            rpcArray.put(fetchMetadataRpcOperation);
        } catch (JSONException e) {
            logger.error("Error occurred while generating data for shindig metadata call: " + e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        //convert the json object to a string
        String postData = rpcArray.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("requestContent: " + postData);
        }

        //fire off the request and get the raw JSON back as a string
        String responseString = restOperations.postForObject(shindigUrl, postData, String.class);
        if (logger.isDebugEnabled()) {
            logger.debug("shindig metadata raw response: " + responseString);
        }

        //now trim back the response to just the metadata for the single gadget
        try {
            JSONObject responseObject = new JSONArray(responseString).
                    getJSONObject(0).
                    getJSONObject("result").
                    getJSONObject(gadgetUrl);
            responseString = responseObject.toString();

            if (logger.isDebugEnabled()) {
                logger.debug("shindig metadata trimmed response: " + responseString);
            }
        } catch (JSONException e) {
            logger.error("Error occurred while processing response from shindig metadata call: " + e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        //return the raw JSON
        return responseString;
    }
}