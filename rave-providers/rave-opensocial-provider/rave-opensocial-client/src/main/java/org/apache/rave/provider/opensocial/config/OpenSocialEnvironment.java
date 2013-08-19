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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rave.portal.events.PortalPreferenceJavascriptDebugModeEventListener;
import org.apache.rave.portal.events.PortalPreferenceJavascriptDebugModeSaveEvent;
import org.apache.rave.portal.events.RaveEvent;
import org.apache.rave.portal.events.RaveEventManager;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
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
public class OpenSocialEnvironment implements PortalPreferenceJavascriptDebugModeEventListener{

    private static final Log log = LogFactory.getLog(OpenSocialEnvironment.class);

    private static final String CONTAINER_JS_KEY = "containerJs";
    private static final String SCRIPT_RENDER_DEBUG_ON = "1";
    private final static String SCRIPT_TEMPLATE = "<script>requirejs.config({paths: {\"osapi\":\"%1$s://%2$s%3$s/js/%4$s.js?c=1&container=default&debug=%5$s\"}});</script>";

    private String currentDebugMode;

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
    /**
     * The features to request from epensocial engine
     */
    private String containerFeatures;

    @Autowired
    RaveEventManager eventManager;

    @Autowired
    PortalPreferenceService portalPreferenceService;

    @PostConstruct
    public void init() {
        eventManager.addListener(PortalPreferenceJavascriptDebugModeSaveEvent.class, this);
        try{
            this.setCurrentDebugMode(portalPreferenceService.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE).getValue());
        } catch (Exception e){
            log.warn("Caught exception getting preference for JS debug mode. Setting JS to debug mode to 'debug on'.");
            this.setCurrentDebugMode(SCRIPT_RENDER_DEBUG_ON);
        }
        registerScriptBlock();
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

    @Value("${portal.opensocial_engine.container_features}")
    public void setContainerFeatures(String containerFeatures) {
        this.containerFeatures = containerFeatures;
    }

    public String getContainerFeatures() {
        return containerFeatures;
    }

    public String getCurrentDebugMode() {
        return currentDebugMode;
    }

    public void setCurrentDebugMode(String currentDebugMode) {
        this.currentDebugMode = currentDebugMode;
    }

    @Override
    public void handleEvent(RaveEvent event) {
        if(event instanceof PortalPreferenceJavascriptDebugModeSaveEvent){
            this.setCurrentDebugMode(String.valueOf(portalPreferenceService.getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE).getValue()));
            log.debug("found event to change debug mode of JS new value =" + this.getCurrentDebugMode());
            registerScriptBlock();
        } else {
            log.warn("Unhandled event received. " + event.getClass());
        }
    }

    private void registerScriptBlock(){
        scriptManager.registerScriptBlock(CONTAINER_JS_KEY, String.format(SCRIPT_TEMPLATE, engineProtocol, engineRoot, engineGadgetPath, containerFeatures, this.getCurrentDebugMode()), ScriptLocation.BEFORE_RAVE);
    }
}
