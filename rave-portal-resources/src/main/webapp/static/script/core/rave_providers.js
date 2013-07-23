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
 * Exports the providers that the rave client will support. If your implementation does
 * not need to support both opensocial and wookie providers, overlay this file to
 * export only the providers you will support. This reduces the amount of script that will
 * be downloaded and parsed by the client.
 * @module rave_providers
 * @requires rave_wookie
 * @requires rave_opensocial
 */
define(['core/rave_wookie', 'core/rave_opensocial'], function(wookie, os){
    var exports = {};

    /**
     * Rave wookie provider module
     * @see module:rave_wookie
     */
    exports.w3c = wookie;

    /**
     * Rave opensocial provider module.
     * @see module:rave_opensocial
     */
    exports.opensocial = os;

    return exports;
})
