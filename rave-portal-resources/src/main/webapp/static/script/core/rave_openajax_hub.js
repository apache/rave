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
 * @requires rave_log
 */
define(['underscore', 'core/rave_log', 'osapi'], function (_, log) {

    if (_.isUndefined(OpenAjax)) {
        throw new Error("No implementation of OpenAjax found.  " +
            "Please ensure that an implementation has been included in the page.");
    }

    /**
     * Returns an OpenAjax.hub.ManagedHub instance.
     * @exports rave_openajax_hub
     * @see http://www.openajax.org/member/wiki/OpenAjax_Hub_2.0_Specification_Managed_Hub_APIs
     */
    var exports = new OpenAjax.hub.ManagedHub({
        onSubscribe: function (topic, container) {
            log((container == null ? "Container" : container.getClientID()) + " subscribes to this topic '" + topic + "'");
            return true;
        },
        onUnsubscribe: function (topic, container) {
            log((container == null ? "Container" : container.getClientID()) + " unsubscribes from this topic '" + topic + "'");
            return true;
        },
        onPublish: function (topic, data, pcont, scont) {
            log((pcont == null ? "Container" : pcont.getClientID()) + " publishes '" + data + "' to topic '" + topic + "' subscribed by " + (scont == null ? "Container" : scont.getClientID()));
            return true;
        }
    });

    return exports;
})