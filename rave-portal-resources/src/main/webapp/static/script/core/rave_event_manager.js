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
 * Manages the registering and execution of initialization handlers
 * @module rave_event_manager
 */
define(['underscore'], function(_){

    var INITIALIZED = false,
        initHandlers = [];

    var exports = {}

    /**
     * Registers a callback function to be executed at the time that rave is initialized.
     * @param {function} handler
     */
    exports.registerOnInitHandler = function (handler) {
        if (!(_.isFunction(handler))) {
            throw new Error('Init event handler must be a function');
        }
        if (INITIALIZED) {
            return handler();
        }
        initHandlers.push(handler);
    }

    /**
     * Executes all registered init handlers in the order they were registered.
     */
    exports.init = function () {
        INITIALIZED = true;
        _.each(initHandlers, function (fn) {
            fn();
        });
    }

    return exports;

})