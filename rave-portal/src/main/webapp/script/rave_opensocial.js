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
     * Creates and renders the list of gadgets
     *
     * @param gadgets list of gadget objects that are to be rendered by the container
     */
    function createGadgetInstances(gadgets) {

        //break out of the function if there are no OpenSocial gadgets
        if(!gadgets || gadgets.length == 0) return;


        //Create a list of gadget URLs from the gadget objects
        var gadgetUrls = [];
        for(var i = 0; i < gadgets.length; i++) {
            gadgetUrls.push(gadgets[i].widgetUrl);
        }

        /**
         * Tell the common container to pre-load the metadata for all the widgets we're going to ask it to render.  If we
         * don't do this then when we call navigateGadget for each regionWidget the common container will fetch the metadata
         * for each one at a time.  We also pass a callback function which will take the metadata retrieved from the preload
         * so we can get all the default values for userPrefs and pass them along with our navigateGadget call.
         *
         * TODO: Prime the common container metadata cache with data we pull from our own persistent store so that we dont have
         * to have common container fetch metadata on every page render.  See osapi.container.Container.prototype.preloadFromConfig_
         * function which gets called from the osapi.container.Container constructor to get an idea of how this might be done.
         *
         * TODO: Use real userPrefs that we pull from our persistent store instead of the default values pulled from common
         * containers metadata call.
         *
         * TODO: Get real moduleId's based on the regionWidget.id into the iframe URL.  Right now common container uses an
         * internal counter for the mid parameter value with no external way to set it.
         */
        container.preloadGadgets(gadgetUrls, function(response) {
            for (var i = 0; i < gadgets.length; i++) {
                var gadget = gadgets[i];
                var gadgetMetadata = response[gadget.widgetUrl];

                for (var userPref in gadgetMetadata.userPrefs) {
                    userPref = gadgetMetadata.userPrefs[userPref];
                    gadget.userPrefs[userPref.name] = userPref.defaultValue;
                }

                var renderParams = {};
                renderParams[osapi.container.RenderParam.VIEW] = "home";
                renderParams[osapi.container.RenderParam.WIDTH] = 250;
                renderParams[osapi.container.RenderParam.HEIGHT] = 250;
                renderParams[osapi.container.RenderParam.USER_PREFS] = gadget.userPrefs;
                var widgetBodyElement = document.getElementById(["widget-", gadget.regionWidgetId, "-body"].join(""));
                var gadgetSite = container.newGadgetSite(widgetBodyElement);
                container.navigateGadget(gadgetSite, gadget.widgetUrl, {}, renderParams);
            }
        });
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
            element.innerHTML = gadgets.util.escapeString(args.a);
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
         * Instantiates and renders the given gadgets list
         * @param a list of gadgets to render
         */
        initWidgets: createGadgetInstances,

        /**
         * Resets the current OpenSocial container
         */
        reset: resetContainer
    };

})();

//Register teh widget provider with Rave
rave.registerProvider(rave.opensocial);