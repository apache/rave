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

package org.apache.rave.rest.model;

/**
 * REST model for PageTemplate objects
 */
public class PageTemplate implements RestEntity {

    private String id;
    private String name;
    private String description;
    private String pageType;
    private String pageLayoutCode;
    private boolean defaultTemplate;

    public PageTemplate() {}

    public PageTemplate(org.apache.rave.model.PageTemplate source) {
        this.id = source.getId();
        this.name = source.getName();
        this.description = source.getDescription();
        this.pageType = source.getPageType();
        this.pageLayoutCode = source.getPageLayout().getCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageLayoutCode() {
        return pageLayoutCode;
    }

    public void setPageLayoutCode(String pageLayoutCode) {
        this.pageLayoutCode = pageLayoutCode;
    }

    public boolean isDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }
}
