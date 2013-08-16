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

define(['underscore', 'rave'], function (_, rave) {
    return ['$resource', 'Pages', 'Regions', 'RegionWidgets', 'constants',
        function ($resource, Pages, Regions, RegionWidgets, constants) {
            var res = $resource(constants.hostedPath + '/api/rest/pages/render/:context/:identifier/:id', {},
                {
                    _query: { method: 'GET', isArray: true },
                    _get: {method: 'GET'}
                });

            /**
             * Overwriting the pagesForRender query / get methods to decompose the responses into their subordinate
             * $resource types. This guarantees that the PagesForRender resource gets us objects on the scope that
             * we can individually manipulate and update.
             */
            /*
            TODO: One issue with this approach is that currently on a save to a page, the entire page AND all sub-objects
            are getting posted to the server on $save() - same for regions
             */
            res.query = function (args, onSuccess, onError) {
                return res._query.call(null, args).$then(function (res) {
                    //TODO: check for error
                    var pages = res.data;

                    _.each(pages, function (page, k) {
                        page = pages[k] = new Pages(page);

                        decomposePage(page);
                    });

                    return onSuccess(pages);
                });
            }

            res.get = function (args, onSuccess, onError) {
                return res.get.call(null, args).$then(function (res) {
                    //TODO: check for error
                    var page = res.data;

                    decomposePage(page);

                    return onSuccess(page);
                });
            }

            function decomposePage(page) {
                _.each(page.regions, function (region, j) {
                    region = page.regions[j] = new Regions(region);

                    _.each(region.regionWidgets, function (regionWidget, i) {
                        regionWidget = rave.registerWidget(regionWidget.regionId, regionWidget);
                        region.regionWidgets[i] = new RegionWidgets(regionWidget);
                    });
                });
            }

            return res;
        }
    ];
})

