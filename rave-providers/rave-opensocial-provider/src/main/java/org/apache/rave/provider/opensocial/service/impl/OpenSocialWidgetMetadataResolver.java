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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenSocialWidgetMetadataResolver implements WidgetMetadataResolver {
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
     * @param url   url for the widget
     * @return
     */
    public Widget getMetadata(String url) {
        Widget widget = new Widget();
        JSONObject jsonGadget = null;
        try {
            jsonGadget = (JSONObject) new JSONTokener(gadgetMetadataRepository.getGadgetMetadata(url)).nextValue();
            if ( jsonGadget != null ) {
                String query = jsonGadget.getString("modulePrefs");
                JSONObject jsonModulePrefsObject = (JSONObject) new JSONTokener(query).nextValue();
                if ( jsonModulePrefsObject != null ) {
                    String title = jsonModulePrefsObject.getString("title");
                    String titleUrl = jsonModulePrefsObject.getString("titleUrl");
                    String description =  jsonModulePrefsObject.getString("description");
                    String author = jsonModulePrefsObject.getString("author");
                    String authorEmail = jsonModulePrefsObject.getString("authorEmail");
                    String thumbnailUrl = jsonModulePrefsObject.getString("thumbnail");
                    String screenShot = jsonModulePrefsObject.getString("screenshot");

                    widget.setTitle(title);
                    widget.setTitleUrl(titleUrl);
                    widget.setDescription(description);
                    widget.setAuthor(author);
                    widget.setAuthorEmail(authorEmail);
                    widget.setThumbnailUrl(thumbnailUrl);
                    widget.setScreenshotUrl(screenShot);
                    widget.setUrl(url);
                    widget.setType(getSupportedContext());
                }
            }
        } catch (JSONException e) {
            try {
                String query = jsonGadget.getString("error");
                if (query != null ) {
                    JSONObject jsonModuleErrorObject = (JSONObject) new JSONTokener(query).nextValue();
                    if ( jsonModuleErrorObject != null ) {
                        String errorMessage = jsonModuleErrorObject.getString("message");
                        String errorCode = jsonModuleErrorObject.getString("code");
                        throw new IllegalArgumentException("HTTP error: " + errorCode + ". Message: " + errorMessage);
                    }
                }
            } catch ( JSONException e1) {
                throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e1);
            }
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        }
        return widget;
    }
}
