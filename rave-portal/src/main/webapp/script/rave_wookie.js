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

    function createWidgets(widgets){
        if(!widgets || widgets.length == 0) return;
        
        for(var i = 0; i < widgets.length; i++){
            var widgetBodyElement = document.getElementById(["widget-", widgets[i].regionWidgetId, "-body"].join(""));
            var widgetIframe = document.createElement("iframe");
            widgetIframe.setAttribute("height",250);
            widgetIframe.setAttribute("width",250);
            widgetIframe.setAttribute("src",widgets[i].widgetUrl);
            widgetBodyElement.appendChild(widgetIframe);
        }
    }

    /**
     * Exposed public API calls
     */
    return {
        TYPE : WIDGET_TYPE,
        
        /**
         * Renders the given Widgets list
         * @param a list of widgets to render
         */
        initWidgets: createWidgets

    };


})();