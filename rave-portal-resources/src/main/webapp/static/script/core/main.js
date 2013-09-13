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

/**
 * The rave core module. This file assembles the dependency tree onto one namespace and presents a consolidated api.
 * In virtually all cases, you will not need to require the individual modules. Instead, you can require 'rave' to
 * access the rave api object. The rave api is extended by the api of each of the mixin modules. In addition it attaches
 * rave_api to rave.api, rave_widget onto rave.RegionWidget, and rave_log onto rave.log.
 *
 * @module rave
 * @mixes module:rave_event_manager
 * @mixes module:rave_widget_manager
 * @mixes module:rave_state_manager
 * @mixes module:rave_view_manager
 * @example
 *
 * require(['rave'], function(rave){
 *
 *     rave.registerWidget('1', {id: 9});
 *
 *     rave.registerOnInitHandler(function(){
 *          rave.api.rpc.getFriends({successCallback: rave.log});
 *     });
 *
 * });
 */
define(['underscore', 'core/rave_widget_manager', 'core/rave_api', 'core/rave_widget', 'core/rave_log',
    'core/rave_event_manager', 'core/rave_view_manager', 'core/rave_state_manager', 'core/rave_openajax_hub', 'core/rave_action_manager'],
    function (_, widgetManager, api, RegionWidget, log, eventManager, viewManager, stateManager, managedHub, actionManager) {

        var exports = {};

        /**
         * @see module:rave_api
         */
        exports.api = api;

        /**
         * @see module:rave_widget
         */
        exports.RegionWidget = RegionWidget;

        /**
         * @see module:rave_log
         */
        exports.log = log;

        /**
         * @see module:rave_openajax_hub
         */
        exports.getManagedHub = function(){return managedHub};

        _.extend(exports, eventManager);
        _.extend(exports, viewManager);
        _.extend(exports, widgetManager);
        _.extend(exports, stateManager);
        _.extend(exports, actionManager);

        return exports;
    }
)
