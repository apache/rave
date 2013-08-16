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

define(['angular', './CategoriesResource', './PagesResource', './PagesForRenderResource',
    './PeopleResource', './RegionsResource', './RegionWidgetsResource', './UsersResource',
    '../services/index', 'underscore', 'angularResource'],
    function (angular, categories, pages, pagesForRender, people, regions, regionWidgets, users, _) {

        var resources = angular.module('common.resources', ['ngResource', 'common.services'])

        /**
         * For all ajax requests, if the request returns json data and has a .data property, return that.
         * This lets us unwrap our api responses for $resources.
         */
        resources.config(['$httpProvider', function ($httpProvider) {
            $httpProvider.defaults.transformResponse.push(function (data, headers) {
                if (headers('CONTENT-TYPE') === 'application/json' && data.data) {
                    return data.data;
                }
                else {
                    return data;
                }
            });
        }]);

        resources.factory('Categories', categories);
        resources.factory('Pages', pages);
        resources.factory('People', people);
        resources.factory('Regions', regions);
        resources.factory('RegionWidgets', regionWidgets);
        resources.factory('Users', users);
        resources.factory('PagesForRender', pagesForRender);

        return resources;
    });
