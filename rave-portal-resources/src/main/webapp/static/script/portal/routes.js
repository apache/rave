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

define(['angular'], function () {
    var router = angular.module('portal.routes', []);

    router.config(['$routeProvider', '$locationProvider', '$httpProvider',
        function ($routeProvider, $locationProvider, $httpProvider) {

            $routeProvider
                .when('/', {
                    controller: 'TabsController',
                    resolve: {
                        pages: ['PagesService', function (PagesService) {
                            return PagesService.get('portal', '@self');
                        }]
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .when('/:tabId', {
                    controller: 'TabsController',
                    resolve: {
                        pages: ['PagesService', function (PagesService) {
                            return PagesService.get('portal', '@self');
                        }]
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .otherwise({ templateUrl: '/portal/static/html/portal/404.html'});

            $locationProvider.html5Mode(true);


        }
    ]);

    return router;
});