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
 * The Base directive looks at the <base href=".."> tag on the angular.jsp page and parses it to establish the
 * hostedPath of the application. This is available on constants.hostedPath and should be injected to any
 * service, etc that needs to build a url (see all common resources for example).
 */
define([], function () {
    return [ 'constants',
        function (constants) {
            return {
                restrict: 'E',
                link: function link(scope, el, attrs) {
                    var href = attrs.href;

                    //TODO: this currently accounts for 'angular' in the root path. Once we bump it, needs to become splice(-3)
                    href = href.split('/')
                    href.splice(-4);
                    href = href.join('/');

                    constants.hostedPath = href;
                }
            }
        }
    ]
});