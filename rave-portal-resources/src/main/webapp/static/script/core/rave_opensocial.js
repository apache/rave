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

//TODO: Remove dependency on rave_ui when moving to angular
define(["./rave_core", "./rave_view_manager", "./rave_widget", "./rave_api", "portal/rave_ui"],
    function(core, viewManager, regionWidget, api, raveUi){
    var exports = {};

    var container;

    var containerConfig = {};
    containerConfig[osapi.container.ServiceConfig.API_PATH] = "/rpc";
    //TODO: Handle moving out javaScriptDebugMode
    //containerConfig[osapi.container.ContainerConfig.RENDER_DEBUG] = ravePortal.getJavaScriptDebugMode();
    container = new osapi.container.Container(containerConfig);

    gadgets.pubsub2router.init({
        hub:core.getManagedHub()
    });

    rpcRegister();
    implementViews();


    function rpcRegister() {
        container.rpcRegister('requestNavigateTo', requestNavigateTo);
        container.rpcRegister('set_pref', setPref);
        container.rpcRegister('set_title', setTitle);
        container.rpcRegister('hideWidget', hideWidget);
        container.rpcRegister('showWidget', showWidget);
    }

    function requestNavigateTo(args, viewName, opt_params, opt_ownerId) {
        var widget = args.gs._widget,
            viewSurface = viewName.split('.')[0],
            renderInto = viewManager.getView(viewSurface) ? viewSurface : widget._el;

        widget.render(renderInto, {view: viewName, view_params: opt_params, ownerId: opt_ownerId});
    }

    function setPref(args, editToken, prefName, prefValue) {
        var widget = args.gs._widget;
        widget.savePreference(prefName, prefValue);
    }

    /*
     TODO: these rely on a gadget's view implementing a method
     */
    function setTitle(args) {
        var widget = args.gs._widget;
        if (widget._view && widget._view.setTitle) {
            var title = _.isArray(args.a) ? args.a[0] : args.a;
            widget._view.setTitle(title);
        }
    }

    function hideWidget(args, viewName, opt_params, opt_ownerId) {
        var widget = args.gs._widget;
        if (widget._view && widget._view.collapse) {
            widget._view.collapse();
        }
    }

    function showWidget(args, viewName, opt_params, opt_ownerId) {
        var widget = args.gs._widget;
        if (widget._view && widget._view.expand) {
            widget._view.expand();
        }
    }

    function implementViews() {
        container.views.createElementForGadget = function (metadata, rel, opt_view, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
            if (opt_viewTarget) {
                var prefs = (metadata && metadata.views && metadata.views[opt_view])
                var view = viewManager.renderView(opt_viewTarget, prefs);
                var el = view.getWidgetSite();
                el.setAttribute('data-rave-view', view._uid);
                return el;
            }
        };

        container.views.createElementForEmbeddedExperience = function (rel, opt_gadgetInfo, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
            var widgetUrl = opt_gadgetInfo.url;

            api.rest.getSecurityToken({
                "url": widgetUrl,
                //TODO: need to remove reference to rave.ui once we understand need for current pageid
                //violates core dependency
                "pageid": raveUi.getCurrentPageId(),
                "successCallback": renderEE
            });

            function renderEE(data) {
                if (data.error) {
                    return core.log(data.error.message)
                }
                var gadget = {
                        "widgetUrl": widgetUrl,
                        "securityToken": data.securityToken,
                        "metadata": opt_gadgetInfo
                    },
                    height = gadget.metadata.modulePrefs.height || regionWidget.defaultHeight,
                    width = gadget.metadata.modulePrefs.width || regionWidget.defaultWidth;

                preloadMetadata(gadget);

                if (opt_viewTarget) {
                    var view = viewManager.renderView(opt_viewTarget, {"preferredHeight": height, preferredWidth: width});
                    var el = view.getWidgetSite();
                    el.setAttribute('data-rave-view', view._uid);
                    opt_callback(el);
                }
            }
        };

        container.views.createElementForUrl = function (rel, opt_viewTarget, opt_coordinates, parentSite, opt_callback) {
            if (opt_viewTarget) {
                var view = viewManager.renderView(opt_viewTarget);
                var el = view.getWidgetSite();
                el.setAttribute('data-rave-view', view._uid);
                opt_callback(el);
            }
        };

        container.views.destroyElement = function (site) {
            var el = site.el_;
            container.closeGadget(site);
            var _uid = el.getAttribute('data-rave-view');
            viewManager.destroyView(_uid);
        };
    }

    exports.initWidget = function (widget) {
        widget.error = getMetadataErrors(widget.metadata);
        if (!widget.error) {
            preloadMetadata(widget);
        }
    }

    /*
     Opts -
     full spectrum of allowed render options!
     */
    exports.renderWidget = function (widget, el, opts) {
        if(widget.error) {
            widget.renderError(el, widget.error.message);
            return;
        }
        opts = opts || {};
        var site = container.newGadgetSite(el);
        site._widget = widget;
        widget._site = site;

        var renderParams = {};
        renderParams[osapi.container.RenderParam.VIEW] = opts.view || regionWidget.defaultView;
        renderParams[osapi.container.RenderParam.ALLOW_DEFAULT_VIEW ] = opts.allowDefaultView;
        renderParams[osapi.container.RenderParam.DEBUG ] = opts.debug;
        renderParams[osapi.container.RenderParam.HEIGHT ] = opts.height || regionWidget.defaultHeight;
        renderParams[osapi.container.RenderParam.NO_CACHE ] = opts.noCache;
        renderParams[osapi.container.RenderParam.TEST_MODE] = opts.testMode;
        renderParams[osapi.container.RenderParam.WIDTH ] = opts.width || regionWidget.defaultWidth;
        renderParams[osapi.container.RenderParam.USER_PREFS] = getCompleteUserPrefSet(widget.userPrefs, widget.metadata.userPrefs);
        container.navigateGadget(site, widget.widgetUrl, opts.view_params, renderParams, opts.callback);
    }

    /**
     * Combines the default user pref list from the metadata with those set by the user
     * @param setPrefs preferences already set by the user
     * @param metadataPrefs list of all available metadata objects
     */
    function getCompleteUserPrefSet(setPrefs, metadataPrefs) {
        var combined = {};
        _.each(metadataPrefs, function (metaPref) {
            var userPref = setPrefs[metaPref.name];
            combined[metaPref.name] = _.isUndefined(userPref) ? metaPref.defaultValue : userPref;
        });
        return combined;
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

    function getMetadataErrors(metadata) {
        return metadata.error;
    }

    exports.closeWidget = function (widget) {
        if (widget._site) {
            container.closeGadget(widget._site);
        }
    }

    exports.getContainer = function() {
        return container;
    }

    return exports;
})