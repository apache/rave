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
rave.opensocial = rave.opensocial || (function () {
    var WIDGET_TYPE = "OpenSocial";
    var OFFSET = 10;
    var VIEW_NAMES = {
        CANVAS:"canvas",
        DEFAULT:"default",
        HOME:"home",
        PROFILE:"profile",
        PREFERENCES:"preferences"
    };
    var VIEW_TARGETS = {
        TAB: 'tab',
        DIALOG: 'dialog',
        MODALDIALOG: 'modal_dialog',
        SIDEBAR: 'sidebar'
    };

    var container, defaultView;

    /**
     * Initialization
     */
    function initOpenSocial(view) {
        defaultView = typeof view == "string" ? view :VIEW_NAMES.HOME;
        initContainer();
        registerRpcHooks();
        implementViews();
        gadgets.pubsub2router.init({
            hub:rave.getManagedHub()
        })
    }

    function initContainer() {
        //Create the common container instance.
        var containerConfig = {};
        containerConfig[osapi.container.ServiceConfig.API_PATH] = "/rpc";
        containerConfig[osapi.container.ContainerConfig.RENDER_DEBUG] = rave.getJavaScriptDebugMode();
        container = new osapi.container.Container(containerConfig);
    }

    /**
     * Gets the container singleton or initializes if not instantiated
     */
    function getContainer() {
        if (typeof container == "undefined" || container == null) {
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
        container.rpcRegister('set_title', setTitle);
        container.rpcRegister('requestNavigateTo', requestNavigateTo);
        container.rpcRegister('set_pref', setPref);
        container.rpcRegister('hideWidget', hideWidget);
        container.rpcRegister('showWidget', showWidget);
    }

    function implementViews() {

        container.views.createElementForGadget = function (metadata, rel, opt_view, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
                if (opt_viewTarget) {
                    var prefs = (metadata && metadata.views && metadata.views[opt_view])
                    return rave.createPopup(opt_viewTarget, prefs);
                }
            };


        container.views.createElementForEmbeddedExperience = function (rel, opt_gadgetInfo, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
        	var widgetUrl = opt_gadgetInfo.url;
        	
        	getSecurityToken({
            		"url": widgetUrl,
            		"pageid": rave.layout.getCurrentPageId(),
            		"successCallback": function(data) {
            			if (!data.error) {
            				var gadget = {
            			  	  "widgetUrl": widgetUrl,
            			  	  "securityToken": data.securityToken,
            			  	  "metadata": opt_gadgetInfo
            				}
            		
            				preloadMetadata(gadget);
                        
            				if (opt_viewTarget) {
            					opt_callback(rave.createPopup(opt_viewTarget));
            				}
            			} else {
            				console.log(data.error.message);
            			}
            		}
            	})
            };

        container.views.createElementForUrl = function (rel, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
                if (opt_viewTarget) {
                    return rave.createPopup(opt_viewTarget);
                }
            };

        container.views.destroyElement = function (site) {
                var element = site.el_;
                container.closeGadget(site);
                rave.destroyPopup(element);
            };

    }
    
    function getSecurityToken(args) {
        $.ajax({
            type: 'GET',
            url: rave.getContext() + "api/rest/" + "st?url=" + args.url + "&pageid=" + args.pageid,
            dataType: "json",
            success: function (data) {
                if (typeof args.successCallback == 'function') {
                    args.successCallback(data);
                }
        	}
        });
    }

    /**
     * Validates a gadget's metadata and renders it on the page
     *
     * @param gadget the gadget object to be rendered by the container
     */
    function validateAndRenderGadget(gadget) {
        var validationResult = validateMetadata(gadget.metadata);
        if (validationResult.valid) {
            preloadMetadata(gadget);
            renderNewGadget(gadget);
        } else {
            rave.errorWidget(gadget.regionWidgetId, rave.getClientMessage("opensocial.render_error") + "<br /><br />" + validationResult.error);
        }
    }
    
    function preloadMetadata(gadget) {
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
    }

    /**
     * Renders a new gadget
     * @param gadget
     */
    function renderNewGadget(gadget) {
        var widgetBodyElement = document.getElementById(["widget-", gadget.regionWidgetId, "-body"].join(""));
        gadget.site = container.newGadgetSite(widgetBodyElement);
        gadget.maximize = function (view_params, view) {
            var viewName = (typeof(view) === "undefined" || view === null) ? rave.opensocial.VIEW_NAMES.CANVAS : view;
            // always display the gadget in canvas view even if it currently collapsed
            renderGadgetView(viewName, this, view_params);
        };
        gadget.minimize = function (view_params, view) {
            var viewName = (typeof(view) === "undefined" || view === null) ? defaultView : view;
            renderGadgetViewIfNotCollapsed(viewName, this, view_params);
        };
        gadget.collapse = function () {
            // hide the iframe of the gadget via css
            $(getGadgetIframeByWidgetId(this.regionWidgetId)).hide();
        };
        gadget.restore = function () {
            renderGadgetView(defaultView, rave.getRegionWidgetById(this.regionWidgetId));
        };
        gadget.hide = function(){
            $(getGadgetIframeByWidgetId(this.regionWidgetId)).closest('.widget-wrapper').hide();
        };
        gadget.show = function() {
            $(getGadgetIframeByWidgetId(this.regionWidgetId)).closest('.widget-wrapper').show();
        };
        gadget.savePreferences = function (userPrefs) {
            this.userPrefs = userPrefs;
            if(rave.isPageEditor()){
                rave.api.rest.saveWidgetPreferences({regionWidgetId:this.regionWidgetId, userPrefs:userPrefs});
            }
            // re-render the gadget in the same view if the gadget is not collapsed
            renderGadgetViewIfNotCollapsed(rave.opensocial.getCurrentView(this.regionWidgetId), this);
        };
        gadget.editCustomPrefs = function () {
            // display the gadget in custom edit prefs view
            renderGadgetView(rave.opensocial.VIEW_NAMES.PREFERENCES, this);
        };
        gadget.renderView = function(viewName) {
            // render the gadget in the view supplied.  This can be used to render a gadget in any custom-defined view
            renderGadgetView(viewName, this);
        };

        // if the gadget has prefences to edit, or has the Preferences view
        // enable the edit prefs menu item
        if (gadget.metadata.hasPrefsToEdit || gadget.metadata.views.preferences) {
            if (gadget.metadata.views.preferences != undefined) {
                rave.layout.enableEditPrefsMenuItem(gadget.regionWidgetId, true);
            }
            else {
                rave.layout.enableEditPrefsMenuItem(gadget.regionWidgetId, false);
            }
        }

        // if the gadget is on a top level page, or an active sub page tab, render it
        if (!rave.layout.isWidgetOnHiddenTab(gadget)) {
            // if the gadget is not collapsed, render it
            renderGadgetViewIfNotCollapsed(defaultView, gadget);
        }
    }

    /**
     * Utility function to render a gadget in the supplied view if the gadget's
     * collapsed attribute is false
     * @param view the OpenSocial view to render
     * @param gadget the OpenSocial gadget to render
     */
    function renderGadgetViewIfNotCollapsed(view, gadget, view_params) {
        if (!gadget.collapsed) {
            renderGadgetView(view, gadget, view_params);
        }
    }

    /**
     * Renders a gadget in the given view;
     * @param view the view to render
     * @param gadget the Rave widget object
     */
    function renderGadgetView(view, gadget, view_params) {
        var renderParams = {};
        var size = calculateSize(view, gadget);

        renderParams[osapi.container.RenderParam.VIEW] = view;
        renderParams[osapi.container.RenderParam.WIDTH] = size.width;
        renderParams[osapi.container.RenderParam.HEIGHT] = size.height;
        renderParams[osapi.container.RenderParam.USER_PREFS] = getCompleteUserPrefSet(gadget.userPrefs, gadget.metadata.userPrefs);
        container.navigateGadget(gadget.site, gadget.widgetUrl, view_params, renderParams);
    }

    function calculateSize(view, gadget) {
        var id = gadget.regionWidgetId;
        var elem = document.getElementById("widget-" + id + "-wrapper");

        // determine the height of the gadget's iframe
        var height = rave.getDefaultWidgetHeight();
        if (view == rave.opensocial.VIEW_NAMES.CANVAS) {
            height = elem.clientHeight;
        } else if (gadget.metadata.modulePrefs && gadget.metadata.modulePrefs.height) {
            height = gadget.metadata.modulePrefs.height;
        }
        return {width:"100%", height:height};
    }

    /**
     * Returns the Common Container activeSiteHolder object for the given widgetId
     * @param widgetId the widgetId
     */
    function getActiveSiteHolderByWidgetId(widgetId) {
        return rave.getRegionWidgetById(widgetId).site.getActiveSiteHolder();
    }

    /**
     * Returns the iframe element of the gadget for the given widgetId
     */
    function getGadgetIframeByWidgetId(widgetId) {
        return getActiveSiteHolderByWidgetId(widgetId).getIframeElement();
    }

    /**
     * validates the metadata for the current gadget
     * @param metadata the metadata object to validate
     */
    function validateMetadata(metadata) {
        if (typeof metadata.error != "undefined") {
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
        var activeGadgetHolder = getActiveSiteHolderByWidgetId(regionWidgetId);
        return (activeGadgetHolder == null) ? null : activeGadgetHolder.getView();
    }

    /*
     RPC Callback handlers
     */

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
        var bodyId = args.gs.getActiveSiteHolder().getElement().id;
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
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveSiteHolder().getElement().id);
        var regionWidget = rave.getRegionWidgetById(widgetId);
        // update the memory prefs object
        regionWidget.userPrefs[prefName] = prefValue;
        // persist it to database
        if(rave.isPageEditor()){
            rave.api.rest.saveWidgetPreference({regionWidgetId:widgetId, userPref:{prefName:prefName, prefValue:prefValue}});
        }
    }


    function hideWidget(args){
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveSiteHolder().getElement().id);
        var fnArgs = {};
        fnArgs.data = {}
        fnArgs.data.id = widgetId;

        rave.hideWidget(fnArgs);
    }

    function showWidget(args){
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveSiteHolder().getElement().id);
        var fnArgs = {};
        fnArgs.data = {}
        fnArgs.data.id = widgetId;

        rave.showWidget(fnArgs);
    }

    /**
     * Re-renders the gadget in the requested view
     *
     * @param args RPC event args
     * @param viewName the view name to render
     */
    function requestNavigateTo(args, viewName, opt_params, opt_ownerId) {
        var widgetId = rave.getObjectIdFromDomId(args.gs.getActiveSiteHolder().getElement().id);
        var fnArgs = {};
        fnArgs.data = {}
        fnArgs.data.id = widgetId;
        fnArgs.data.view = viewName;

        var viewType = viewName.indexOf(".") == -1 ? viewName : viewName.substring(0, viewName.indexOf("."));

        switch (viewType) {
            case VIEW_NAMES.CANVAS:
                rave.maximizeWidget(fnArgs, opt_params);
                break;
            case VIEW_NAMES.HOME:
                rave.minimizeWidget(fnArgs, opt_params);
                break;
            case VIEW_NAMES.PREFERENCES:
                rave.editCustomPrefs(fnArgs);
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
        TYPE:WIDGET_TYPE,

        VIEW_NAMES:VIEW_NAMES,

        VIEW_TARGETS: VIEW_TARGETS,
        /**
         * Initializes the Rave OpenSocial machinery
         */
        init:initOpenSocial,
        /**
         * Gets a reference to the container singleton
         */
        container:getContainer,
        /**
         * Instantiates and renders the given gadget
         * @param a gadget to render
         */
        initWidget:validateAndRenderGadget,

        /**
         * Resets the current OpenSocial container
         */
        reset:resetContainer,
        /**
         * Gets the current view name of the given gadget
         * @param regionWidgetId of the gadget
         */
        getCurrentView:getCurrentView
    };

})();

//Register the widget provider with Rave
rave.registerProvider(rave.opensocial);
