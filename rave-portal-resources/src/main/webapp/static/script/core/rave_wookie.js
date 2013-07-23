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

/**
 * Implements wookie specific features of the rave_widget interface
 * @module rave_wookie
 * @requires rave_openajax_hub
 * @requires rave_state_manager
 */
define(['core/rave_openajax_hub', 'core/rave_state_manager'], function(managedHub, stateManager){
        var exports = {}

        exports.initWidget = function(widget){}
        exports.renderWidget = function(widget, el, opts){
            var widgetBodyElement = document.getElementById(["widget-", widget.regionWidgetId, "-body"].join(""));
            window["onWidget"+widget.regionWidgetId+"Load"] = function(){
                window.document.getElementById(widget.regionWidgetId).style.visibility="visible";
            };

            new OpenAjax.hub.IframeContainer(managedHub , ""+widget.regionWidgetId,
                {
                    Container: {
                        onSecurityAlert: onClientSecurityAlert,
                        onConnect:       onClientConnect,
                        onDisconnect:    onClientDisconnect
                    },
                    IframeContainer: {
                        parent:      el,
                        iframeAttrs: {
                            height: widget.height || stateManager.getDefaultHeight(),
                            width:  widget.width || stateManager.getDefaultWidth(),
                            frameborder: 0
                        },
                        uri: widget.widgetUrl,
                        onGadgetLoad: "onWidget"+widget.regionWidgetId+"Load"
                    }
                }
            );
        }

        function onClientSecurityAlert(source, alertType) {  /* Handle client-side security alerts */  }
        function onClientConnect(container) {        /* Called when client connects */   }
        function onClientDisconnect(container) {     /* Called when client disconnects */ }

        exports.closeWidget = function(widget){
            //TODO...
        }

        return exports;
})