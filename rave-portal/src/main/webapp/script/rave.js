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
var rave = rave || (function(){

    function mapWidgetsByType(widgets) {
        var map = {};
        for(var i=0; i < widgets.length; i++) {
            var widget = widgets[i];
            var type = widget.type;
            if(!type) {
                type = "Unknown";
            }
            if(!map[type]) {
                map[type] = [];
            }
            map[type].push(widget);
        }
        return map;
    }

    function extractRegionWidgetIdFromElementId(elementId) {
        var tokens = elementId.split("-");
        return tokens.length > 2 && tokens[0] == "widget" ? tokens[1] : null;
    }

    /**
     * Public API
     */
    return {
        /**
         * Creates a map of widgets by their type
         *
         * @param widgets list of widgets to map by type
         */
        createWidgetMap : mapWidgetsByType,

        /**
         * Parses the given string conforming to a widget's DOM element ID and return
         * the RegionWidget id
         *
         * NOTE: assumes convention of widget-RegionWidgetId-body|title|chrome|etc
         *
         * @param elementId the ID of the DOM element containing the widget
         */
        getRegionWidgetIdFromElementId : extractRegionWidgetIdFromElementId
    }

})();