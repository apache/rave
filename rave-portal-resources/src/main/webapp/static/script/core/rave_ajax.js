/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

define(['jquery'], function ($) {
    /**
     * Wraps the rave client's ajax functionality.
     * By default rave uses jquery as its ajax library. If you want to use another ajax library overlay this file
     * and return a function that conforms with the api of jquery's ajax() function.
     * @exports rave_ajax
     * @see {@link http://api.jquery.com/jQuery.ajax/ $.ajax}
     */
    var ajax = $.ajax;

    return ajax;
})
