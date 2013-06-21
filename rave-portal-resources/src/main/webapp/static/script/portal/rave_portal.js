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

define(["jquery", "underscore", "core/rave_api", "./rave_templates"], function($, _, api, raveTemplates){
    var clientMessages = {};
    var context="";

    var pageEditor = true;
    var pageViewer = {
        username: "Unknown",
        id: -1
    };
    var pageOwner = {
        username: "Unknown",
        id: -1
    };
    // JS debug mode is off by default
    var javaScriptDebugMode = 0;
    //Assigning a default value of 250 if unable to get value from the DB
    var defaultWidgetHeight = 250;
    var onWidgetsInitializedHandlers = [];
    var onProvidersInitializedHandlers = [];
    var onUIInitializedHandlers = [];
    var onPageInitializedHandlers = [];

    function getClientMessage(key) {
        return clientMessages[key];
    }

    function addClientMessage(key, message) {
        return clientMessages[key] = message;
    }

    function renderErrorWidget(id, message) {
        $("#widget-" + id + "-body").html(message);
    }


    function setJavaScriptDebugMode(debugMode) {
        javaScriptDebugMode = debugMode;
    }

    function getJavaScriptDebugMode() {
        return javaScriptDebugMode;
    }

    function setPageViewer(viewer) {
        pageViewer = viewer;
    }

    function getPageViewer() {
        return pageViewer;
    }

    function setPageOwner(owner) {
        pageOwner = owner;
    }

    function getPageOwner() {
        return pageOwner;
    }

    function viewPage(pageId) {
        var fragment = (pageId != null) ? ("/" + pageId) : "";
        window.location.href = getContext() + "page/view" + fragment;
    }

    function viewWidgetDetail(widgetId, referringPageId, jumpToId) {
        if (jumpToId) {
            jumpToId = '#' + jumpToId;
        }
        else {
            jumpToId = '';
        }
        window.location.href = getContext() + "store/widget/" + widgetId + "?referringPageId=" + referringPageId + jumpToId;
    }

    /**
     * Determines if a page is empty (has zero widgets)
     */
    function isPageEmpty() {
        return $.isEmptyObject(widgetByIdMap);
    }

    /**
     * Removes a regionWidgetId from the internal widget map
     * @param regionWidgetId the region widget id to remove
     */
    function removeWidgetFromMap(regionWidgetId) {
        delete widgetByIdMap[regionWidgetId];
    }

    function initPageEditorStatus(status) {
        if (status != "undefined") {
            pageEditor = status;
        }
    }

    function isPageEditor() {
        return pageEditor;
    }

    function registerOnWidgetsInitizalizedHandler(callback) {
        if (onWidgetsInitializedHandlers !== null) {
            onWidgetsInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnProvidersInitizalizedHandler(callback) {
        if (onProvidersInitializedHandlers !== null) {
            onProvidersInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnUIInitizalizedHandler(callback) {
        if (onUIInitializedHandlers !== null) {
            onUIInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };

    function registerOnPageInitizalizedHandler(callback) {
        if (onPageInitializedHandlers !== null) {
            onPageInitializedHandlers.push(callback);
        } else {
            callback();
        }
    };


    /**
     * Internal method should only be called from the page.jsp
     */
    function runOnPageInitializedHandlers() {
        if (onPageInitializedHandlers !== null && onPageInitializedHandlers.length > 0) {
            for (var i = 0, j = onPageInitializedHandlers.length; i < j; ++i) {
                try {
                    onPageInitializedHandlers[i]();
                } catch (ex) {
                    gadgets.warn("Could not fire onPageInitializedHandler " + ex.message);
                }
            }
        }
        onPageInitializedHandlers = null;  // No need to hold these references anymore.
    }

    function extractObjectIdFromElementId(elementId) {
        var tokens = elementId.split("-");
        return tokens.length > 2 && tokens[0] == "widget" || tokens[0] == "region" ? tokens[1] : null;
    }

    function showInfoMessage(message) {

        var markup = raveTemplates.templates['info-message']({
            message: message
        });

        $(markup).hide()
            .prependTo("body")
            //TODO: add a class for flash box and attach these styles
            .css({ position: 'fixed', top: 0, left: 0, width: 'auto', 'max-width': '60%', 'font-size': '1.25em', padding: '.6em 1em', 'z-index': 9999, 'border-radius': '0 0 4px 0'})
            .fadeIn('fast').delay(8000)
            .fadeOut(function () {
                $(this).remove();
            });
    }


    /**
     * Public API
     */
    return {

        showInfoMessage: showInfoMessage,

        getObjectIdFromDomId: extractObjectIdFromElementId,
        /**
         * Renders an error in place of the widget
         *
         * @param id the RegionWidgetId of the widget to render in error mode
         * @param message The message to display to the user
         */
        errorWidget: renderErrorWidget,

        /**
         * Gets the value of the JavaScriptDebugMode flag
         * 0 = off
         * 1 = on
         */
        getJavaScriptDebugMode: getJavaScriptDebugMode,

        /**
         * Sets the value of the JavaScriptDebugMode flag
         * 0 = off
         * 1 = on
         */
        setJavaScriptDebugMode: setJavaScriptDebugMode,

        /**
         * Sets the authenticated page viewer for the Rave web application
         *
         * @param viewer an object representing the authenticated user viewing the page {username:"bob", id:"1"}
         */
        setPageViewer: setPageViewer,

        /**
         * Gets the current viewer
         */
        getPageViewer: getPageViewer,

        /**
         * Sets the owner of the current page
         *
         * @param owner an object representing the owner of the page
         */
        setPageOwner: setPageOwner,

        /**
         * Gets the page owner
         */
        getPageOwner: getPageOwner,

        /**
         * View a page
         *
         * @param pageId the pageId to view, or if null, the user's default page
         */
        viewPage: viewPage,

        /**
         * View the widget detail page of a widget
         *
         * @param widgetId to widgetId to view
         * @param referringPageId the id of the page the call is coming from
         */
        viewWidgetDetail: viewWidgetDetail,

        /**
         * Determines if a page is empty (has zero widgets)
         * @param widgetByIdMap the map of widgets on the page
         */
        isPageEmpty: isPageEmpty,


        /**
         * Returns a language specific message based on the supplied key
         *
         * @param key the key of the message
         */
        getClientMessage: getClientMessage,

        /**
         * Adds a message to the internal client message map
         *
         * @param key
         * @param message
         */
        addClientMessage: addClientMessage,

        /**
         * Set if user of a page has editing permission
         * Used to stop sending UI events back to the server, rather
         * than actually implementing any permission rules
         * (which are set on the server)
         */
        initPageEditorStatus: initPageEditorStatus,

        /**
         * Returns a boolean indicating if the user
         * should be treated as an page editor or not
         */
        isPageEditor: isPageEditor,

        /**
         * Registration methods for initialization events
         */
        registerOnWidgetsInitizalizedHandler: registerOnWidgetsInitizalizedHandler,
        registerOnProvidersInitizalizedHandler: registerOnProvidersInitizalizedHandler,
        registerOnUIInitizalizedHandler: registerOnUIInitizalizedHandler,
        registerOnPageInitizalizedHandler: registerOnPageInitizalizedHandler,
        runOnPageInitializedHandlers: runOnPageInitializedHandlers
    }
})
