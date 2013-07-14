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

describe('rave_event_manager', function(){
    beforeEach(function () {
        eventManager = testr('core/rave_event_manager');

    });

    describe('regsiterOnInitHandler', function(){

        it('throws an error if handler is not a function', function () {
            expect(function () {
                eventManager.registerOnInitHandler({})
            }).toThrow();
        });

        it('registers a handler that is fired on init in the order registered', function () {
            var arr = [];

            eventManager.registerOnInitHandler(function () {
                arr.push(1);
            });
            eventManager.registerOnInitHandler(function () {
                arr.push(2);
            });
            expect(arr).toEqual([]);
            eventManager.init();
            expect(arr).toEqual([1, 2]);
        });

        it('immediately invokes a handler if rave.init has already been called', function () {
            var scope = {
                handler: function () {
                }
            };

            spyOn(scope, 'handler');

            eventManager.init();
            expect(scope.handler).not.toHaveBeenCalled();
            eventManager.registerOnInitHandler(scope.handler);
            expect(scope.handler).toHaveBeenCalled();
        });
    })
})
