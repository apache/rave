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
 *                         registerView
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * The RenderRegionWidget directive takes a regionWidget to be rendered in the region-widget="" attribute
 * and renders that regionWidget at the location of the directive.
 */

define(['rave'], function (rave) {
    return ['$compile', '$rootScope',
        function ($compile, $rootScope) {
            var directive = {
                restrict: 'A',
                link: function (scope, el, attrs) {
                    var regionWidget = scope.$eval(attrs.renderRegionWidget);
                    regionWidget = rave.getWidget(regionWidget.id);

                    scope.$watch(function () {
                        return regionWidget._surface;
                    }, doRender)

                    regionWidget.on('navigate', function () {
                        //conditionally apply if not already in a digest cycle
                        if ($rootScope.$$phase != '$apply') {
                            scope.$apply();
                        }
                    });

                    function doRender() {
                        var template = rave.getView(regionWidget._surface);

                        el.html(template);
                        $compile(el.contents())(scope);
                    }

                }
            }

            return directive;
        }
    ]
})

