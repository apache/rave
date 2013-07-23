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
define(["underscore", "jquery", "rave", "portal/rave_templates", "clientMessages"],
    function (_, $, rave, raveTemplates, clientMessages) {

        // JS debug mode is off by default
        var javaScriptDebugMode = 0;

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

        function viewPage(pageId) {
            var fragment = (pageId != null) ? ("/" + pageId) : "";
            window.location.href = rave.getContext() + "page/view" + fragment;
        }

        function viewWidgetDetail(widgetId, referringPageId, jumpToId) {
            if (jumpToId) {
                jumpToId = '#' + jumpToId;
            }
            else {
                jumpToId = '';
            }
            window.location.href = rave.getContext() + "store/widget/" + widgetId + "?referringPageId=" + referringPageId + jumpToId;
        }

        /**
         * Determines if a page is empty (has zero widgets)
         */
        function isPageEmpty() {
            return _.isEmpty(rave.getWidgets());
        }

        function isPageEditor() {
            return rave.getViewer().editor;
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
             * Returns a boolean indicating if the user
             * should be treated as an page editor or not
             */
            isPageEditor: isPageEditor
        }
    })
