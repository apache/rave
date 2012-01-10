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

package org.apache.rave.provider.opensocial.service.impl;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.provider.opensocial.Constants;
import org.apache.rave.provider.opensocial.repository.GadgetMetadataRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenSocialWidgetMetadataResolver implements WidgetMetadataResolver {
    private static Logger logger = LoggerFactory.getLogger(OpenSocialWidgetMetadataResolver.class);
    private GadgetMetadataRepository gadgetMetadataRepository;

    @Autowired
    public OpenSocialWidgetMetadataResolver(GadgetMetadataRepository gadgetMetadataRepository) {
        this.gadgetMetadataRepository = gadgetMetadataRepository;
    }

    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    /**
     * Get the opensocial widget metadata
     *
     * @param url url for the widget
     * @return
     */
    public Widget getMetadata(String url) {
        Widget widget = new Widget();
        JSONObject jsonGadget = null;
        try {
            jsonGadget = (JSONObject) new JSONTokener(gadgetMetadataRepository.getGadgetMetadata(url)).nextValue();
            if (jsonGadget != null) {
                String query = jsonGadget.getString("modulePrefs");
                if (query != null) {
                    JSONObject jsonModulePrefsObject = (JSONObject) new JSONTokener(query).nextValue();
                    if (jsonModulePrefsObject != null) {
                        //Wrapped every element into individual try/catch as if any of the element is coming null getString throws JSONException.
                        try {
                            String title = jsonModulePrefsObject.getString("title");
                            widget.setTitle(title);
                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        try {
                            String titleUrl = jsonModulePrefsObject.getString("titleUrl");
                            widget.setTitleUrl(titleUrl);

                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        try {
                            String description = jsonModulePrefsObject.getString("description");
                            widget.setDescription(description);
                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        try {
                            String author = jsonModulePrefsObject.getString("author");
                            widget.setAuthor(author);
                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        try {
                            String authorEmail = jsonModulePrefsObject.getString("authorEmail");
                            widget.setAuthorEmail(authorEmail);

                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }

                        try {
                            String thumbnailUrl = jsonModulePrefsObject.getString("thumbnail");
                            widget.setThumbnailUrl(thumbnailUrl);
                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        try {
                            String screenShot = jsonModulePrefsObject.getString("screenshot");
                            widget.setScreenshotUrl(screenShot);
                        } catch (JSONException e) {
                            logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                        }
                        widget.setUrl(url);
                        widget.setType(getSupportedContext());
                    }
                }
            }
        } catch (JSONException e) {
            try {
                String query = jsonGadget.getString("error");
                if (query != null) {
                    JSONObject jsonModuleErrorObject = (JSONObject) new JSONTokener(query).nextValue();
                    if (jsonModuleErrorObject != null) {
                        String errorMessage = jsonModuleErrorObject.getString("message");
                        String errorCode = jsonModuleErrorObject.getString("code");
                        throw new IllegalArgumentException("HTTP error: " + errorCode + ". Message: " + errorMessage);
                    }
                }
            } catch (JSONException e1) {
                logger.info("Error while reading: " + e.getLocalizedMessage(), e);
                throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call",
                        e1);
            }
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        }
        return widget;
    }
}
