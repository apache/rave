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

package org.apache.rave.provider.opensocial.config;

import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Environment variables for OpenSocial calls from the portal (to Shindig)
 */
@Component
public class OpenSocialEnvironment {

    private static final String SCRIPT_TEMPLATE = "<script src=\"%1$s://%2$s%3$s/js/container.js?c=1&amp;container=default&amp;debug=1\"></script>";

    private ScriptManager scriptManager;

    /**
     * Protocol for the opensocial engine (http, https)
     */
    private String engineProtocol;
    /**
     * Root location of the opensocial engine (localhost:8080, www.example.com)
     */
    private String engineRoot;
    /**
     * Path after the root for gadgets (gadgets)
     */
    private String engineGadgetPath;

    @PostConstruct
    public void init() {
        scriptManager.registerScriptBlock(String.format(SCRIPT_TEMPLATE, engineProtocol, engineRoot, engineGadgetPath), ScriptLocation.BEFORE_RAVE);
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    @Autowired
    public void setScriptManager(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Value("${portal.opensocial_engine.protocol}")
    public void setEngineProtocol(String engineProtocol) {
        this.engineProtocol = engineProtocol;
    }

    public String getEngineProtocol() {
        return engineProtocol;
    }

    @Value("${portal.opensocial_engine.root}")
    public void setEngineRoot(String engineRoot) {
        this.engineRoot = engineRoot;
    }

    public String getEngineRoot() {
        return engineRoot;
    }

    @Value("${portal.opensocial_engine.gadget_path}")
    public void setEngineGadgetPath(String engineGadgetPath) {
        this.engineGadgetPath = engineGadgetPath;
    }

    public String getEngineGadgetPath() {
        return engineGadgetPath;
    }
}
