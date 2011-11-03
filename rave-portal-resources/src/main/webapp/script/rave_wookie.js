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
    var MIN_HEIGHT = 250;

    var container;
    
    function validateAndRenderWidget(widget){
    
        var widgetBodyElement = document.getElementById(["widget-", widget.regionWidgetId, "-body"].join(""));
        
        var widgetIframe = document.createElement("iframe");

        if (widget.height){
          widgetIframe.setAttribute("height",widget.height);
          widgetIframe.setAttribute("min-height",MIN_HEIGHT+"px");
        } else {
          widgetIframe.setAttribute("height",MIN_HEIGHT+"px");
          widgetIframe.setAttribute("min-height",MIN_HEIGHT+"px");             
        }
        if (widget.width) {
          widgetIframe.setAttribute("style","width: 100%; min-width: "+widget.width);
        } else {
          widgetIframe.setAttribute("style","width: 100%");        
        }
        widgetIframe.setAttribute("src",widget.widgetUrl);
        widgetIframe.setAttribute("scroll","no");
        widgetIframe.setAttribute("frameborder","0");
        widgetIframe.setAttribute("vspace","0");
        widgetIframe.setAttribute("hspace","0");
        widgetIframe.setAttribute("marginheight","0");
        widgetIframe.setAttribute("marginwidth","0");
        widgetBodyElement.appendChild(widgetIframe);
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