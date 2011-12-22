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
describe("Rave API", function() {
    //create a mock jquery object so we can hang mock functions off of it as needed
    $ = {};

    describe("rest", function() {
        describe("saveWidgetPreferences", function() {
            it("PUTs correct values to the REST service for saving widget preferences", function() {
                //add a mock for the jquery ajax function to the mock jquery object
                //this mock function will end up getting called when the saveWidgetPreferences we're testing makes its
                //ajax call by executing $.ajax({ ... }); - and when that happens it will end up running this implementation
                //of $.ajax() which in turn sets up a bunch of expectations for jasmine to verify at the end of the test
                $.ajax = function(args) {
                    expect(args.url).toEqual("api/rest/regionWidgets/1/preferences");
                    var userPrefs = JSON.parse(args.data).preferences;
                    expect(userPrefs.length).toEqual(2);
                    var foundColorPref = false;
                    var foundSpeedPref = false;
                    for (var i = 0; i < userPrefs.length; i++) {
                        var userPref = userPrefs[i];
                        if (userPref.name == "color" && userPref.value == "blue") {
                            foundColorPref = true;
                        } else if (userPref.name == "speed" && userPref.value == "fast") {
                            foundSpeedPref = true;
                        }
                    }
                    expect(foundColorPref).toBeTruthy();
                    expect(foundSpeedPref).toBeTruthy();

                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };

                var callbackCalled = false;
                var callback = function() {
                    callbackCalled = true
                };
                rave.api.rest.saveWidgetPreferences({regionWidgetId:1, userPrefs:{"color":"blue", "speed":"fast"},
                    successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });
        
        describe("saveWidgetPreference", function() {
            it("PUTs correct values to the REST service for saving one widget preference", function() {
                $.ajax = function(args) {
                    expect(args.url).toEqual("api/rest/regionWidgets/1/preferences/firstname");
                    var userPref = JSON.parse(args.data);
                    expect(userPref.name).toEqual("firstname");
                    expect(userPref.value).toEqual("Tony");                    
                    
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };

                var callbackCalled = false;
                var callback = function() {
                    callbackCalled = true
                };
                             
                rave.api.rest.saveWidgetPreference({regionWidgetId: 1, userPref: {prefName: "firstname", prefValue: "Tony"},
                                                   successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });
        
        describe("saveWidgetCollapsedState", function() {
            it("PUTs correct values to the REST service for saving the collapsed state of a widget", function() {               
                
                $.ajax = function(args) {
                    expect(args.url).toEqual("api/rest/regionWidgets/7/collapsed");                    
                    expect(JSON.parse(args.data)).toEqual(true);                                   
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };

                var callbackCalled = false;
                var callback = function() {
                    callbackCalled = true
                };
                             
                rave.api.rest.saveWidgetCollapsedState({regionWidgetId: 7, 
                                                        collapsed: true,
                                                        successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });        
                
        describe("deletePage", function() {
            it("DELETEs the correct Page using the REST service", function() {
                $.ajax = function(args) {
                    expect(args.url).toEqual("api/rest/page/9");                                                  
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };

                var callbackCalled = false;
                var callback = function() {
                    callbackCalled = true
                };
                             
                rave.api.rest.deletePage({pageId: 9, successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });

        describe("getUsersForWidget", function() {
            it("GETs the widget users using the REST service", function() {
                $.ajax = function(args) {
                    expect(args.url).toEqual("api/rest/widgets/23/users");
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };

                var callbackCalled = false;
                var callback = function() {
                    callbackCalled = true
                };

                rave.api.rest.getUsersForWidget({widgetId: 23, successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });
    });

    describe("rpc", function() {
        describe("addWidgetToPage", function() {
            it("posts the correct values to RPC service for adding a widget to the page", function() {

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/1/widget/add");
                    expect(data.widgetId).toEqual(2);
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.addWidgetToPage({pageId: 1, widgetId: 2});
            });
        });

        describe("moveWidget", function() {
            it("posts the correct values to RPC service for adding a widget to the page", function() {

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/regionWidget/3/move");
                    expect(data.toRegion).toEqual("1");
                    expect(data.fromRegion).toEqual("2");
                    expect(data.newPosition).toEqual(3);
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.moveWidget({targetRegion: {id:"region-1"}, currentRegion: {id:"region-2"}, targetIndex: 3, widget:{id:"widget-3-body"}});
            });
        });

        describe("deleteWidget", function() {
            it("posts correct values to the RPC service for deleting a widget from the page", function() {
                var callbackCalled = false;
                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/regionWidget/1/delete");
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                var callback = function() {
                    callbackCalled = true
                };
                rave.api.rpc.removeWidget({regionWidgetId : 1, successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });

        });
        
        describe("addPage", function() {
            it("posts the correct values to RPC service for adding a new page", function() {

                var newPageName = "my new page";
                var newPageLayoutCode = "layout1";

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/add");
                    expect(data.pageName).toEqual(newPageName);
                    expect(data.pageLayoutCode).toEqual(newPageLayoutCode);
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.addPage({pageName: newPageName, pageLayoutCode: newPageLayoutCode});
            });
        });
        
        describe("movePage", function() {
            it("posts the correct values to RPC service for moving a page", function() {
                var pageId = 7;
                var moveAfterPageId = 29;

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/" + pageId + "/move");                   
                    expect(data.moveAfterPageId).toEqual(moveAfterPageId);
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.movePage({pageId: pageId, moveAfterPageId: moveAfterPageId});
            });
        });        

        describe("moveWidgetToPage", function() {
            it("posts the correct values to RPC service for moving a widget to a different page", function() {
                var currentPageId = 7;
                var toPageId = 8;
                var regionWidgetId = 10;

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/" + toPageId + "/moveWidget");                   
                    expect(data.toPageId).toEqual(toPageId);
                    expect(data.regionWidgetId).toEqual(regionWidgetId);
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.moveWidgetToPage({toPageId: toPageId, regionWidgetId:regionWidgetId});
            });
        });

        describe("getPagePrefs", function() {
            it("gets the correct metadata for a page", function() {
                var callbackCalled = false;
                var pageId = 73;
                $.get = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/get?pageId=" + pageId);                                       
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                var callback = function() {
                    callbackCalled = true;
                };
                rave.api.rpc.getPagePrefs({pageId: pageId, successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });

        describe("getWidgetMetadata", function() {
            it("gets the metadata metadata for a url provided", function() {
                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/widget/metadata/get");
                    expect(data.url).toEqual("http://www.gstatic.com/ig/modules/tabnews/tabnews.xml");
                    expect(data.type).toEqual("OpenSocial");
                    expect(typeof(callback)).toEqual("function");
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                rave.api.rpc.getWidgetMetadata({url: "http://www.gstatic.com/ig/modules/tabnews/tabnews.xml", type: "OpenSocial"});
            });
        });
        
        describe("updatePagePrefs", function() {
            it("posts the correct values to RPC service for updating page metadata", function() {
                var pageId = 7;                
                var newTitle = "New Title";
                var newLayout = "1_column";
                var callbackCalled = false;

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/" + pageId + "/update");                   
                    expect(data.name).toEqual(newTitle);
                    expect(data.layout).toEqual(newLayout);
                    expect(typeof(callback)).toEqual("function");
                    callback({error:false});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                var callback = function() {
                    callbackCalled = true;
                };
                rave.api.rpc.updatePagePrefs({pageId: pageId, title: newTitle, layout: newLayout, successCallback: callback});
                expect(callbackCalled).toBeTruthy();
            });
        });                

        describe("Error handling", function() {
            it("displays the appropriate alert when invalid parameters are passed", function() {

                $.post = function(url, data, handler) {
                    handler({error: true, errorCode: "INVALID_PARAMS"});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                alert = function(str) {
                    expect(str).toEqual("Rave attempted to update the server with your recent changes, " +
                        " but the changes were rejected by the server as invalid.");
                };
                rave.api.rpc.moveWidget({targetRegion: {id:"region-1"}, currentRegion: {id:"region-2"}, targetIndex: 3, widget:{id:"widget-3-body"}});

            });

            it("displays the appropriate alert when a server error occurs", function() {

                $.post = function(url, data, handler) {
                    handler({error: true, errorCode: "INTERNAL_ERROR"});
                    return {
                        error: function(a, b, c) {
                        }
                    }
                };
                alert = function(str) {
                    expect(str).toEqual("Rave attempted to update the server with your recent changes, " +
                        " but the server encountered an internal error.");
                };
                rave.api.rpc.moveWidget({targetRegion: {id:"region-1"}, currentRegion: {id:"region-2"}, targetIndex: 3, widget:{id:"widget-3-body"}});
            });
        });
    });

});
