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
var rave = rave || {};
rave.wookie = rave.wookie || (function() {
    var WIDGET_TYPE = "W3C";
    var OFFSET = 10;
    // keep this value so we can show the widget in the maximize view even when its collapsed
    var userCollapsed;
    var container;
    
    
    function onClientSecurityAlert(source, alertType) {  /* Handle client-side security alerts */  }
    function onClientConnect(container) {        /* Called when client connects */   }
    function onClientDisconnect(container) {     /* Called when client disconnects */ }

    function validateAndRenderWidget(widget){
    	userCollapsed = widget.collapsed;
        var widgetBodyElement = document.getElementById(["widget-", widget.regionWidgetId, "-body"].join(""));
        
        var height = rave.getDefaultWidgetHeight();
        if (widget.height) height = widget.height;
        
        //
        // Create a global onload callback handler for making the widget
        // visible after its container is ready
        //
        window["onWidget"+widget.regionWidgetId+"Load"] = function(){
           window.document.getElementById(widget.regionWidgetId).style.visibility="visible";
        };

        //
        // Create OpenAjax IFrame container
        //
        var ooacontainer = new OpenAjax.hub.IframeContainer(rave.getManagedHub() , ""+widget.regionWidgetId,
        {
          Container: {
            onSecurityAlert: onClientSecurityAlert,
            onConnect:       onClientConnect,
            onDisconnect:    onClientDisconnect
          },
          IframeContainer: {
            parent:      widgetBodyElement, 
            iframeAttrs: { 
                style: { width:"100%"},
                vspace: 0,
                hspace: 0,
                marginheight: 0,
                marginwidth: 0,
                scroll: "no",
                frameborder: 0,
                height: height,
                "min-height": ""+rave.getDefaultWidgetHeight()+"px"
            },
            uri: widget.widgetUrl,
            onGadgetLoad: "onWidget"+widget.regionWidgetId+"Load"
          }
        }
        );
        
        // collapse/restore functions
        widget.collapse = function() {
            $(ooacontainer.getIframe()).hide();
        };
        widget.restore = function() {
            $(ooacontainer.getIframe()).show();
        };
        widget.maximize = function() {
            // always display the widget in canvas view even if it currently collapsed
            if (widget.collapsed){
                userCollapsed = true;
                $(ooacontainer.getIframe()).show();
            }
        };
        widget.minimize = function() {
            if (widget.collapsed){
                userCollapsed = false;
                $(ooacontainer.getIframe()).hide();
            }
        };

        // if the widget is on a top level page, or an active sub page tab, render it
        if (!rave.layout.isWidgetOnHiddenTab(widget)) {
            // if in the collapsed state, hide the layer
            if (widget.collapsed){
                $(ooacontainer.getIframe()).hide();
            }
        }
    }

    /**
     * Exposed public API calls
     */
    return {
        TYPE : WIDGET_TYPE,

        /**
         * Init function required by Rave's initialization infrastructure
         */
        init : function() {},
        /**
         * Instantiates and renders the given widget
         * @param a widget to render
         */
        initWidget: validateAndRenderWidget

    };


})();

//Register the widget provider with Rave
rave.registerProvider(rave.wookie);
