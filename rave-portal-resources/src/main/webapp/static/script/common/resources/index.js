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

define(['angular', './RaveResource', './CategoriesResource', './PagesResource', './PagesForRenderResource',
    './PeopleResource', './RegionsResource', './RegionWidgetsResource', './UsersResource', './PageLayoutsResource',
    'underscore', '../services/index', 'angularResource'],
    function (angular, Resource, categories, pages, pagesForRender, people, regions, regionWidgets, users, pageLayouts, _) {

        var resources = angular.module('common.resources', ['ngResource', 'common.services'])

        /**
         The base $resource class for rave models.
         */
        resources.factory('RaveResource', Resource);

        /**
         The rave models.
         */
        resources.factory('Categories', categories);
        resources.factory('Pages', pages);
        resources.factory('People', people);
        resources.factory('Regions', regions);
        resources.factory('RegionWidgets', regionWidgets);
        resources.factory('Users', users);
        resources.factory('PagesForRender', pagesForRender);
        resources.factory('PageLayouts', pageLayouts);


        return resources;
    });
