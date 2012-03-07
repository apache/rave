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
package org.apache.rave.model;


import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * POJO representing a static content artifact
 */
public class StaticContent {   
    private String id;
    private URI location;
    private String content;
    // replacementTokens can be used when the static content artifact is a template and needs some
    // environment specific tokens to be replaced
    private Map<String, String> replacementTokens;

    public StaticContent(String id, URI location, Map<String, String> replacementTokens) {
        this.id = id;
        this.location = location;
        this.replacementTokens = replacementTokens == null ? new HashMap<String, String>() : replacementTokens;
        this.content = "";
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public URI getLocation() {
        return location;
    }

    public Map<String, String> getReplacementTokens() {
        return replacementTokens;
    }

    @Override
    public String toString() {
        return "StaticContent{" + "id=" + id + ", location=" + location + "}";
    }
}
