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
        //container.rpcRegister('set_pref', null);
        //container.rpcRegister('pubsub', null);
    }

    /**
     * Renders a gadget on the page
     *
     * @param gadget the gadget object to be rendered by the container
     */
    function createGadgetInstance(gadget) {

        var gadgetMetadata = gadget.metadata;
        var validationResult = validateMetadata(gadgetMetadata);
        if (validationResult.valid) {
            //TODO: Submit a patch to Shindig common container to expose the backing service or add a method to push cached items  into the container config
            container.service_.addGadgetMetadatas(gadgetMetadata[0].result, null);

            var renderParams = {};
            renderParams[osapi.container.RenderParam.VIEW] = "home";
            renderParams[osapi.container.RenderParam.WIDTH] = 250;
            renderParams[osapi.container.RenderParam.HEIGHT] = 250;
            renderParams[osapi.container.RenderParam.USER_PREFS] = getCompleteUserPrefSet(gadget.userPrefs, gadgetMetadata[0].result[gadget.widgetUrl].userPrefs);
            var widgetBodyElement = document.getElementById(["widget-", gadget.regionWidgetId, "-body"].join(""));
            var gadgetSite = container.newGadgetSite(widgetBodyElement);
            container.navigateGadget(gadgetSite, gadget.widgetUrl, {}, renderParams);

        } else {
            rave.errorWidget(gadget.regionWidgetId, "Unable to render OpenSocial Gadget: <br /><br />" + validationResult.error);
        }

    }

    /**
     * validates the metadata for the current gadget
     * @param metadatas the list of metadata objects to validate
     */
    function validateMetadata(metadatas) {
        for(var i = 0; i < metadatas.length; i++) {
            var result = metadatas[i].result;
            for(var url in result) {
                if(typeof result[url].error != "undefined") {
                    return {valid:false, error:result[url].error.message};
                }
            }
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
        var element = document.getElementById(args.f);
        if (element) {
            element.style.height = height + 'px';
        }
    }

    /**
     * Sets the chrome title when gadgets.window.setTitle is caled
     *
     * @param args RPC event args
     */
    function setTitle(args) {

        //TODO: This implementation relies on parsing of the gadgetHolder's element id
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
     * Re-renders the gadget in the requested view with the parameters
     *
     * @param view target view
     * @param opt_params
     */
    function requestNavigateTo(view, opt_params) {
        //TODO: Implement this function
        throw "Not Implemented!!!!!";
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
        initWidget: createGadgetInstance,

        /**
         * Resets the current OpenSocial container
         */
        reset: resetContainer
    };

})();

//Register the widget provider with Rave
rave.registerProvider(rave.opensocial);