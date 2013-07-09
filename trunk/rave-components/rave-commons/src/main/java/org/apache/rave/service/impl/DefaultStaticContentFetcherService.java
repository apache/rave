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

package org.apache.rave.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rave.model.StaticContent;
import org.apache.rave.service.StaticContentFetcherConsumer;
import org.apache.rave.service.StaticContentFetcherService;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class DefaultStaticContentFetcherService implements StaticContentFetcherService {
    private static final Log log = LogFactory.getLog(DefaultStaticContentFetcherService.class);
    private Map<String, StaticContent> contentMap;
    private RestTemplate restTemplate;
    private final Set<StaticContentFetcherConsumer> staticContentFetcherConsumers;

    public DefaultStaticContentFetcherService(RestTemplate restTemplate, List<StaticContent> contentObjects) {
        this.restTemplate = restTemplate;
        contentMap = new HashMap<String, StaticContent>();
        for (StaticContent contentObject : contentObjects) {
            contentMap.put(contentObject.getId(), contentObject);
        }
        staticContentFetcherConsumers = new HashSet<StaticContentFetcherConsumer>();
    }

    @Override
    public String getContent(String key) {
        log.debug("getContent(" + key + ")");
        StaticContent content = contentMap.get(key);
        if (content == null) {
            throw new IllegalArgumentException("Invalid content key: " + key);
        }
        return content.getContent();
    }

    @Override
    public void refreshAll() {
        log.debug("refreshAll()");
        for (StaticContent curEntry : contentMap.values()) {
            refreshFromLocation(curEntry);
        }
    }

    @Override
    public void refresh(String key) {
        log.debug("refresh(" + key + ")");
        //if the key exists in the content map, refresh it
        StaticContent item = contentMap.get(key);
        if (item != null) {
            refreshFromLocation(item);
        }
    }

    @Override
    public void registerConsumer(StaticContentFetcherConsumer consumer) {
        synchronized (staticContentFetcherConsumers) {
            log.debug("adding " + consumer.getClass().getName() + " to staticContentFetcherConsumers");
            staticContentFetcherConsumers.add(consumer);
        }
    }

    @Override
    public void unregisterConsumer(StaticContentFetcherConsumer consumer) {
        synchronized (staticContentFetcherConsumers) {
            log.debug("removing " + consumer.getClass().getName() + " from staticContentFetcherConsumers");
            staticContentFetcherConsumers.remove(consumer);
        }
    }

    private void refreshFromLocation(StaticContent staticContent) {
        log.debug("refreshFromLocation() - for " + staticContent);
        try {
            //We need an intermediate temp content string here so we don't even accidentally hand out a reference to
            //a not-fully-token-replaced piece of content.  Very unlikely, but could happen.
            String tempContent = restTemplate.getForObject(staticContent.getLocation(), String.class);
            for (Map.Entry<String, String> replacementTokenEntry : staticContent.getReplacementTokens().entrySet()) {
                tempContent = tempContent.replaceAll(replacementTokenEntry.getKey(), replacementTokenEntry.getValue());
            }
            staticContent.setContent(tempContent);
            // notify any registered consumers that the content has been updated
            synchronized (staticContentFetcherConsumers) {
                for (StaticContentFetcherConsumer consumer : staticContentFetcherConsumers) {
                    log.debug("notifiying consumer " + consumer.getClass().getName() + " for content update: " + staticContent.getId());
                    try {
                        consumer.notify(staticContent.getId());
                    } catch (Exception e) {
                        log.warn("exception during consumer notification", e);
                    }
                }
            }
        } catch (RestClientException e) {
            //RestClientException handles server errors 5xx, client errors 4xx, and IO errors
            log.error("Unable to process {" + staticContent.getLocation() + "}", e);
        }
    }
}
