/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

function () {
    var widgetMap = {};
    var self = this;

    var mapPage = function (page) {
        if (page.regions) {
            page.regions.forEach(function (region) {
                if (region.regionWidgets) {
                    region.regionWidgets.forEach(function (regionWidget) {
                        if (!widgetMap[regionWidget.widgetId]) {
                            widgetMap[regionWidget.widgetId] = true;
                            var userMap = {};
                            userMap[self.ownerId] = 1;
                            emit(regionWidget.widgetId, userMap);
                        }
                    })
                }
            })
        }
    };

    mapPage(this);
    if (this.subPages) {
        this.subPages.forEach(function (p) {
            mapPage(p)
        })
    }
}
