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

define(['angular', 'common/directives/index'], function (angular) {
    var services = angular.module('common.services', ['common.directives']);

    /**
     * Establishes the constants.hostedPath that can be injected into any service. Note that constants was made an object
     * with a hostedPath property so that it is mutable. To contrast, services.constant('hostedPath', '') could never
     * have its value set.
     */
    services.constant('constants', {
        hostedPath: ''
    });

    return services;
});