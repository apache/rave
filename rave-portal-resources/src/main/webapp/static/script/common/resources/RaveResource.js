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
            /**
             * For all requests that return data, unwrap the json response
             */
            transformResponse = $http.defaults.transformResponse.concat([
                function (data) {
                    return data.data || data;
                }
            ]),
            /**
             * For all requests that save data, flatten the data object and do not include arrays or nested objects
             * in the request body
             */
            transformRequest = ([
                function (data) {
                    data = angular.copy(data);
                    _.each(data, function(val, key) {
                        if(_.isArray(val) || _.isObject(val)) {
                            data[key] = undefined;
                        }
                    });

                    return data;
                }
            ]).concat($http.defaults.transformRequest),
            defaultActions = {
                get: {method: 'GET', cache: true, transformResponse: transformResponse},
                save: {method: 'POST', transformRequest:transformRequest, transformResponse: transformResponse},
                update: {method: 'PUT', transformRequest:transformRequest, transformResponse: transformResponse},
                query: {method: 'GET', cache: true, isArray: true, transformResponse: transformResponse},
                remove: {method: 'DELETE', transformResponse: transformResponse},
                delete: {method: 'DELETE', transformResponse: transformResponse}
            };

        return function RaveResource(url, paramDefaults, actions) {
            var acts = angular.copy(defaultActions),
                trans = transformResponse;
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