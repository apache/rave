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

define(['angular', '../common/resources/index'], function (angular) {
    var router = angular.module('portal.routes', ['common.resources']);

    router.config(['$routeProvider', '$locationProvider', '$httpProvider',
        function ($routeProvider, $locationProvider, $httpProvider) {

            var getPages =  ['PagesForRender', '$q', '$rootScope', function (PagesForRender, $q, $rootScope) {
                var deferred = $q.defer();

                if($rootScope.pages) {
                    deferred.resolve($rootScope.pages);
                } else {
                    PagesForRender.query({
                        context: 'portal',
                        identifier: '@self'
                    }, function(data){
                        deferred.resolve(data);
                    });
                }

                return deferred.promise;
            }]

            $routeProvider
                .when('/', {
                    controller: 'MainCtrl',
                    resolve: {
                        pages: getPages
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .when('/:tabId', {
                    controller: 'TabsController',
                    resolve: {
                        pages: getPages
                    },
                    templateUrl: "/portal/static/html/portal/tabs.html"
                })
                .otherwise({ templateUrl: '/portal/static/html/portal/404.html'});

            $locationProvider.html5Mode(true);
        }
    ]);

    router.run(['$route', '$rootScope', function($route, $rootScope) {
        $rootScope.$on('$routeChangeSuccess', function(evt, curr, prev){
            $rootScope.pages = curr.locals.pages;
            $rootScope.currentPageId = curr.params.tabId || $rootScope.pages[0].id;
        });
    }]);

    return router;
});