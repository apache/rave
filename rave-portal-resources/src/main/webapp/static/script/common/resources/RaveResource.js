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

define(['underscore', 'angular'], function (_, angular) {
    return ['$resource', '$http', 'constants', function ($resource, $http, constants) {
        var baseUrl = constants.hostedPath + '/api/rest/',
            transform = $http.defaults.transformResponse.concat([
                function (data) {
                    return data.data || data;
                }
            ]),
            defaultActions = {
                get: {method: 'GET', cache: true, transformResponse: transform},
                save: {method: 'POST', transformResponse: transform},
                update: {method: 'PUT', transformResponse: transform},
                query: {method: 'GET', cache: true, isArray: true, transformResponse: transform},
                remove: {method: 'DELETE', transformResponse: transform},
                delete: {method: 'DELETE', transformResponse: transform}
            };

        return function RaveResource(url, paramDefaults, actions) {
            var acts = angular.copy(defaultActions),
                trans = transform;
            url = baseUrl + url;

            _.each(actions, function (action, key) {
                if (action.transformResponse) {
                    if (_.isArray(action.transformResponse)) {
                        trans = trans.concat(action.transformResponse);
                    } else {
                        trans = trans.concat([action.transformResponse]);
                    }
                }

                action.transformResponse = trans;
                acts[key] = _.extend(acts[key] || {}, action);
            });


            return $resource(url, paramDefaults, acts)
        }
    }];
});