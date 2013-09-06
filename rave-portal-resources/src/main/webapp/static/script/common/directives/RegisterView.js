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

define(['rave'], function (rave) {
    return ['$http',
        function ($http) {
            var directive = {
                restrict: 'AE',
                scope: {},
                terminal: true,
                priority: -1,
                controller: ['$scope', '$element',
                    function ($scope, $element) {
                        this.customRender = function () {
                        };
                        this.customDestroy = function () {
                        };
                    }
                ],
                link: function (scope, el, attrs, controller) {

                    var viewName = attrs.registerView || attrs.view;
                    //Because this directive has an isolate scope, need to eval expressions with the parent scope
                    var template = scope.$parent.$eval(attrs.template);

                    if (template) {
                        $http.get(template).success(function (data) {
                            template = data;
                            rave.registerView(viewName, template);
                        });
                    } else {
                        template = el.html();
                        rave.registerView(viewName, template);

                    }
                    el.remove();


                }
            }

            return directive;
        }
    ]
})