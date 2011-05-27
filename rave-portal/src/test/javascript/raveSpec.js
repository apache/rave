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

 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
describe("Rave", function() {

    function getMockProvider(type) {
        return (function() {
            var called = false;
            var widgetsCalled = false;
            return {
                TYPE : type,

                init : function() {
                    called = true;
                },
                initWidgets: function(widgets) {
                    for (var i = 0; i < widgets.length; i++) {
                        expect(widgets[i].type).toEqual(type);
                    }
                    widgetsCalled = true;
                },
                initWidgetsWasCalled : function() {
                    return widgetsCalled;
                },
                initWasCalled : function() {
                    return called;
                }
            };
        })();
    }

    describe("initProviders", function() {

        it("initializes all providers", function() {
            var provider1 = getMockProvider("FOO");
            var provider2 = getMockProvider("BAR");
            rave.registerProvider(provider1);
            rave.registerProvider(provider2);
            rave.initProviders();
            expect(provider1.initWasCalled()).toBeTruthy();
            expect(provider2.initWasCalled()).toBeTruthy();
        });
    });

    describe("registerProvider", function() {
        it("throws error when invalid provider passed", function() {
            expect(
                    function() {
                        rave.registerProvider({badProp: function() {
                                } })
                    }).toThrow(new Error("Attempted to register invalid provider"));
        });
    });

    describe("initWidgets", function() {
        it("calls the appropriate providers", function() {
            var widgetMap = {
                "FOO" : [
                    {type:"FOO"},
                    {type:"FOO"}
                ],
                "BAR" : [
                    {type:"BAR"},
                    {type:"BAR"}
                ]
            };
            var provider1 = getMockProvider("FOO");
            var provider2 = getMockProvider("BAR");
            rave.registerProvider(provider1);
            rave.registerProvider(provider2);
            rave.initWidgets(widgetMap);
            expect(provider1.initWidgetsWasCalled()).toBeTruthy();
            expect(provider2.initWidgetsWasCalled()).toBeTruthy();
        });
    });

    describe("createWidgetMap", function() {

        it("builds a map keyed by type", function() {
            var widgets = [
                {regionWidgetId: 0, type: "OpenSocial"},
                {regionWidgetId: 1, type: "OpenSocial"},
                {regionWidgetId: 2, type: "W3C"},
                {regionWidgetId: 3, type: "W3C"}
            ];

            var map = rave.createWidgetMap(widgets);

            expect(map["OpenSocial"].length).toEqual(2);
            expect(map["W3C"].length).toEqual(2);
            expect(map["OpenSocial"]).toContain(widgets[1]);
            expect(map["W3C"]).toContain(widgets[3]);
        });

        it("builds a map that has widgets under an Unknown key", function() {
            var widgets = [
                {regionWidgetId: 0, type: "OpenSocial"},
                {regionWidgetId: 1, type: "OpenSocial"},
                {regionWidgetId: 2, type: "W3C"},
                {regionWidgetId: 3, type: "W3C"},
                {regionWidgetId: 4 },
                {regionWidgetId: 5, type: null }
            ];

            var map = rave.createWidgetMap(widgets);

            expect(map["OpenSocial"].length).toEqual(2);
            expect(map["W3C"].length).toEqual(2);
            expect(map["Unknown"].length).toEqual(2);
            expect(map["OpenSocial"]).toContain(widgets[1]);
            expect(map["W3C"]).toContain(widgets[3]);
            expect(map["Unknown"]).toContain(widgets[4]);
        });

        it("builds a map with no entries", function() {
            var widgets = [];
            var map = rave.createWidgetMap(widgets);
            var count = 0;
            for (i in map) {
                count++;
            }
            expect(count).toEqual(0);
        });

    });

    describe("initDragAndDrop", function() {
        function createMockJQuery() {
            console = {
                log : function(str) {

                }
            };
            var sortableArgs = null;
            $ = function(element) {
                return {
                    sortable : function(args) {
                        sortableArgs = args;
                        sortableArgs.selector = element;
                    },
                    getSortableArgs : function() {
                        return sortableArgs;
                    },
                    disableSelection: function() {
                    },
                    remove: function(e) {
                    },
                    removeClass: function(e) {
                    },
                    addClass: function(e) {
                    },
                    each: function(e) {
                    },
                    css: function(e) {
                    },
                    prepend: function(e) {
                    }
                }
            };
        }

        function getMockItem() {
            return  {
                item : {
                    parent : function() {
                        return {
                            get : function(index) {
                                if (index == 0) return { id : "region-35"};
                            }
                        }
                    },
                    children : function(selector) {
                        if (selector == '.widget') {
                            return {
                                get : function(index) {
                                    if (index == 0) return {id : 'widget-24-body'};
                                }
                            }
                        }
                    },
                    index : function() {
                        return 2
                    }
                }
            };
        }

        it("Initializes jQuery sortable when init is called", function() {
            createMockJQuery();
            rave.initDragAndDrop();
            var sortableArgs = $().getSortableArgs();
            expect(sortableArgs).toBeDefined();
            expect(sortableArgs.selector).toEqual(".region");
            expect(sortableArgs.connectWith).toEqual(".region");
            expect(sortableArgs.handle).toEqual(".widget-title-bar");
            expect(typeof(sortableArgs.start)).toEqual("function");
            expect(typeof(sortableArgs.stop)).toEqual("function");
        });

        it("Posts when dragging is stopped", function() {
            createMockJQuery();
            rave.initDragAndDrop();
            var sortableArgs = $().getSortableArgs();
            var mockItem = getMockItem();
            $.post = function(url, data, handler) {
                expect(url).toEqual("api/rpc/page/regionWidget/24/move");
                expect(data.toRegion).toEqual('35');
                expect(data.fromRegion).toEqual('35');
                expect(data.newPosition).toEqual(2);
                handler({error: false});
                return {
                    error: function(a, b, c) {}
                }
            };
            sortableArgs.start({}, mockItem);
            sortableArgs.stop({}, mockItem);
        });
        it("displays the appropriate alert when invalid parameters are passed", function() {
            createMockJQuery();
            rave.initDragAndDrop();
            var sortableArgs = $().getSortableArgs();
            var mockItem = getMockItem();
            $.post = function(url, data, handler) {
                handler({error: true, errorCode: "INVALID_PARAMS"});
                    return {
                        error: function(a, b, c) {}
                    }
            };
            alert = function(str) {
                expect(str).toEqual("Rave attempted to update the server with your recent changes, " +
                              " but the changes were rejected by the server as invalid.");
            };
            sortableArgs.start({}, mockItem);
            sortableArgs.stop({}, mockItem);
        });
        it("displays the appropriate alert when a server error occurs", function() {
            createMockJQuery();
            rave.initDragAndDrop();
            var sortableArgs = $().getSortableArgs();
            var mockItem = getMockItem();
            $.post = function(url, data, handler) {
                handler({error: true, errorCode: "INTERNAL_ERROR"});

                    return {
                        error: function(a, b, c) {}
                    }
            };
            alert = function(str) {
                expect(str).toEqual("Rave attempted to update the server with your recent changes, " +
                              " but the server encountered an internal error.");
            };
            sortableArgs.start({}, mockItem);
            sortableArgs.stop({}, mockItem);
        });
    });

    describe("getObjectIdFromDomId", function() {

        it("returns the regionwidgetId from the bodyElementId when the body Id is 3 digits", function() {

            var id = rave.getObjectIdFromDomId("widget-203-id");
            expect(id).toEqual('203');
        });
        it("returns the regionwidgetId from the ElementId when the  Id is 2 digits", function() {

            var id = rave.getObjectIdFromDomId("widget-20-id");
            expect(id).toEqual('20');
        });
        it("returns the regionwidgetId from the ElementId when the  Id is 1 digits", function() {

            var id = rave.getObjectIdFromDomId("widget-2-id");
            expect(id).toEqual('2');
        });
        it("returns the regionId from the ElementId when the  Id is 1 digits", function() {

            var id = rave.getObjectIdFromDomId("region-2-id");
            expect(id).toEqual('2');
        });

        it("returns null when the DOM element's id is invalid", function() {
            var id = rave.getObjectIdFromDomId("does-not-23");
            expect(id).toBeNull();
        });

    });
});