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
 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var rave = rave || {};
rave.opensocial = rave.opensocial || (function() {
    var WIDGET_TYPE = "OpenSocial";
    var OFFSET = 10;
    var MIN_HEIGHT = 250;
    var VIEW_NAMES = { 
        CANVAS : "canvas",
        DEFAULT : "default",
        HOME : "home"       
    }; 

    var container;
    
    /**
     * Initialization
     */
    function initOpenSocial() {
        initContainer();
        registerRpcHooks();
    }

    function initContainer() {
        //Create the common container instance.
        var containerConfig = {};
        containerConfig[osapi.container.ServiceConfig.API_PATH] = "/rpc";
        containerConfig[osapi.container.ContainerConfig.RENDER_DEBUG] = "1";
        container = new osapi.container.Container(containerConfig);
    }

    /**
     * Gets the container singleton or initializes if not instantiated
     */
    function getContainer() {
        if (!container) {
            initContainer();
        }
        return container;
    }

    /** Resets the current singleton reference while allowing anything with a handle on the
     *  current singleton to use that instance.
     */
    function resetContainer() {
        container = null;
    }

    /**
     * Registers the RPC hooks with the container
     */
    function registerRpcHooks() {
        container.rpcRegister('resize_iframe', resizeIframe);
        container.rpcRegister('set_title', setTitle);
        container.rpcRegister('requestNavigateTo', requestNavigateTo);
        container.rpcRegister('set_pref', setPref);
        //container.rpcRegister('pubsub', null);
    }

    /**
     * Validates a gadget's metadata and renders it on the page
     *
     * @param gadget the gadget object to be rendered by the container
     */
    function validateAndRenderGadget(gadget) {
        var validationResult = validateMetadata(gadget.metadata);
        if (validationResult.valid) {
            //Put our gadget metadata into the form that the common container is expecting
            var commonContainerMetadataWrapper = {};
            commonContainerMetadataWrapper[gadget.widgetUrl] = gadget.metadata;

            //Put our gadget security token data into the form that the common container is expecting
            var commonContainerTokenData = {};
            commonContainerTokenData[osapi.container.TokenResponse.TOKEN] = gadget.securityToken;
            commonContainerTokenData[osapi.container.MetadataResponse.RESPONSE_TIME_MS] = new Date().getTime();
            var commonContainerTokenWrapper = {};
            commonContainerTokenWrapper[gadget.widgetUrl] = commonContainerTokenData;

            //Setup the preloadConfig data with all our preload data
            var preloadConfig = {};
            preloadConfig[osapi.container.ContainerConfig.PRELOAD_METADATAS] = commonContainerMetadataWrapper;
            preloadConfig[osapi.container.ContainerConfig.PRELOAD_TOKENS] = commonContainerTokenWrapper;
            preloadConfig[osapi.container.ContainerConfig.PRELOAD_REF_TIME] = null;

            //Preload our data into the common container
            container.preloadCaches(preloadConfig);
            renderNewGadget(gadget);
        } else {
            rave.errorWidget(gadget.regionWidgetId, "Unable to render OpenSocial Gadget: <br /><br />" + validationResult.error);
        }
    }

    /**
     * Renders a new gadget
     * @param gadget
     */
    function renderNewGadget(gadget) {
        var widgetBodyElement = document.getElementById(["widget-", gadget.regionWidgetId, "-body"].join(""));
        gadget.site = container.newGadgetSite(widgetBodyElement);
        gadget.maximize = function() { 
            // always display the gadget in canvas view even if it currently collapsed
            renderGadgetView(rave.opensocial.VIEW_NAMES.CANVAS, this); 
        };
        gadget.minimize = function() { 
            renderGadgetViewIfNotCollapsed(rave.opensocial.VIEW_NAMES.HOME, this);             
        };
        gadget.collapse = function() { 
            // hide the iframe of the gadget via css
            $(getGadgetIframeByWidgetId(this.regionWidgetId)).hide();            
        };
        gadget.restore = function() {
             renderGadgetView(rave.opensocial.VIEW_NAMES.HOME, rave.getRegionWidgetById(this.regionWidgetId));
        };
        gadget.savePreferences = function(userPrefs) {
            this.userPrefs = userPrefs;
            rave.api.rest.saveWidgetPreferences({regionWidgetId: this.regionWidgetId, userPrefs: userPrefs});
            // re-render the gadget in the same view if the gadget is not collapsed
            renderGadgetViewIfNotCollapsed(rave.opensocial.getCurrentView(this.regionWidgetId), this);             
        };
        
        // if the gadget has prefences to edit, enable the edit prefs menu item
        if (gadget.metadata.hasPrefsToEdit) { 
            rave.layout.enableEditPrefsMenuItem(gadget.regionWidgetId);            
        }
        
        // if the gadget is not collapsed, render it
        renderGadgetViewIfNotCollapsed(rave.opensocial.VIEW_NAMES.HOME, gadget);        
    }
    
    /**
     * Utility function to render a gadget in the supplied view if the gadget's 
     * collapsed attribute is false
     * @param view the OpenSocial view to render
     * @param gadget the OpenSocial gadget to render
     */ 
    function renderGadgetViewIfNotCollapsed(view, gadget) {
        if (!gadget.collapsed) {
            renderGadgetView(view, gadget);
        }
    }

    /**
     * Renders a gadget in the given view;
     * @param view the view to render
     * @param gadget the Rave widget object
     */
    function renderGadgetView(view, gadget) {
        var renderParams = {};
        var size = getSizeFromElement(gadget.regionWidgetId, view);
        renderParams[osapi.container.RenderParam.VIEW] = view;
        // 
        // If size.width is passed to the WIDTH param this causes odd behaviour
        // in Firefox and Opera. By using "100%" instead the gadget fills the 
        // wrapper width even after maximizing/minimizing or changes in the region 
        // width caused by other widgets being moved or added
        //
        // renderParams[osapi.container.RenderParam.WIDTH] = size.width;
        //
        renderParams[osapi.container.RenderParam.WIDTH] = "100%";
        renderParams[osapi.container.RenderParam.HEIGHT] = size.height;
        renderParams[osapi.container.RenderParam.USER_PREFS] = getCompleteUserPrefSet(gadget.userPrefs, gadget.metadata.userPrefs);
        container.navigateGadget(gadget.site, gadget.widgetUrl, {}, renderParams);
    }

    function getSizeFromElement(id, view) {
        var elem = document.getElementById("widget-" + id + "-wrapper");
        return {width: elem.clientWidth - OFFSET, height: view == rave.opensocial.VIEW_NAMES.CANVAS ? elem.clientHeight : MIN_HEIGHT};
    }

    /**
     * Returns the Common Container activeGadgetHolder object for the given widgetId
     * @param widgetId the widgetId
     */ 
    function getActiveGadgetHolderByWidgetId(widgetId) {
        return rave.getRegionWidgetById(widgetId).site.getActiveGadgetHolder();     
    }

    /**
     * Returns the iframe element of the gadget for the given widgetId
     */
    function getGadgetIframeByWidgetId(widgetId) {
        return getActiveGadgetHolderByWidgetId(widgetId).getIframeElement();     
    }        

    /**
     * validates the metadata for the current gadget
     * @param metadata the metadata object to validate
     */
    function validateMetadata(metadata) {
        if(typeof metadata.error != "undefined") {
            return {valid:false, error:metadata.error.message};
        }

        return {valid:true};
    }

    /**
     * Combines the default user pref list from the metadata with those set by the user
     * @param setPrefs preferences already set by the user
     * @param metadataPrefs list of all available metadata objects
     */
    function getCompleteUserPrefSet(setPrefs, metadataPrefs) {
        var combined = {};
        for (var key in metadataPrefs) {
            var metaPref = metadataPrefs[key];
            var userPref = setPrefs[metaPref.name];
            combined[metaPref.name] = typeof userPref == "undefined" ? metaPref.defaultValue : userPref;
        }
        return combined;
    }
    
    /**
     * Gets the current view name of a gadget
     * @param regionWidgetId of the gadget
     */
    function getCurrentView(regionWidgetId) {
        // the active gadget holder will be null if the gadget is collapsed
        // as it won't be rendered on the page
        var activeGadgetHolder = getActiveGadgetHolderByWidgetId(regionWidgetId);
        return (activeGadgetHolder == null) ? null : activeGadgetHolder.getView();
    }

    /*
     RPC Callback handlers
     */
    /**
     * Resizes the iFrame when gadgets.window.adjustHeight is called
     *
     * @param args the RPC event args
     */
    function resizeIframe(args) {
        var max = 0x7FFFFFFF;
        var height = args.a > max ? max : args.a;
        args.gs.setHeight(height);
    }

    /**
     * Sets the chrome title when gadgets.window.setTitle is caled
     *
     * @param args RPC event args
     */
    function setTitle(args) {

        //TODO RAVE-229: This implementation relies on parsing of the gadgetHolder's element id
        //to retrieve the module ID
        //A patch should be submitted to Shindig's common container code to properly
        //set the iFrame ID to the module id
        var bodyId = args.gs.getActiveGadgetHolder().getElement().id;
        var titleId = "widget-" + rave.getObjectIdFromDomId(bodyId) + "-title";
        var element = document.getElementById(titleId);
        if (element) {
            var a = isArray(args.a) ? args.a[0] : args.a;
            element.innerHTML = gadgets.util.escapeString(a);
        }

    }
     
    /**
     * Saves a userPref for the widget
     * 
     * @param args RPC event args
     * @param editToken this is an old deprecated parameter but still needs to be in the signature for proper binding
     * @param prefName the userpref name
     * @param prefValue the userpref value
     */ 
    function setPref(args, editToken, prefName, prefValue) {        
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveGadgetHolder().getElement().id);                               
        var regionWidget = rave.getRegionWidgetById(widgetId);
        // update the memory prefs object
        regionWidget.userPrefs[prefName] = prefValue;
        // persist it to database
        rave.api.rest.saveWidgetPreference({regionWidgetId: widgetId, userPref: {prefName: prefName, prefValue: prefValue}});        
    }
      
    /**
     * Re-renders the gadget in the requested view
     *
     * @param args RPC event args
     * @param viewName the view name to render
     */
    function requestNavigateTo(args, viewName) {       
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveGadgetHolder().getElement().id);
        var fnArgs = {};
        fnArgs.data = {}
        fnArgs.data.id = widgetId;
        
        switch(viewName) {
            case VIEW_NAMES.CANVAS:  
                rave.maximizeWidget(fnArgs);
                break;
            case VIEW_NAMES.HOME:            
                rave.minimizeWidget(fnArgs);
                break;
        }       
    }

    /**
     * Utility functions
     */
    function isArray(o) {
        return Object.prototype.toString.call(o) == "[object Array]";
    }
    
    /**
     * Exposed public API calls
     */
    return {
        TYPE : WIDGET_TYPE,
        
        VIEW_NAMES : VIEW_NAMES,
        /**
         * Initializes the Rave OpenSocial machinery
         */
        init : initOpenSocial,
        /**
         * Gets a reference to the container singleton
         */
        container: getContainer,
        /**
         * Instantiates and renders the given gadget
         * @param a gadget to render
         */
        initWidget: validateAndRenderGadget,

        /**
         * Resets the current OpenSocial container
         */
        reset: resetContainer,
        /**
         * Gets the current view name of the given gadget
         * @param regionWidgetId of the gadget
         */
        getCurrentView: getCurrentView
    };

})();

//Register the widget provider with Rave
rave.registerProvider(rave.opensocial);