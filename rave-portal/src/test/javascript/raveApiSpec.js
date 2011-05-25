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

    describe("rpc", function() {

        $ = {};
        console = {log : function(msg) {}};

        describe("addWidgetToPage", function() {
            it("posts the correct values to RPC service for adding a widget to the page", function() {

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/1");
                    expect(data.widgetId).toEqual(2);
                    expect(data.operation).toEqual("ADD_COMPONENT");
                    expect(typeof(callback)).toEqual("function");
                };
                rave.api.rpc.addWidgetToPage({pageId: 1, widgetId: 2});
            });
        });
        describe("moveWidget", function() {
            it("posts the correct values to RPC service for adding a widget to the page", function() {

                $.post = function(url, data, callback) {
                    expect(url).toEqual("api/rpc/page/regionWidget/3");
                    expect(data.operation).toEqual("MOVE");
                    expect(data.to_region).toEqual("1");
                    expect(data.from_region).toEqual("2");
                    expect(data.new_position).toEqual(3);
                    expect(typeof(callback)).toEqual("function");
                };
                rave.api.rpc.moveWidget({targetRegion: {id:"region-1"}, currentRegion: {id:"region-2"}, targetIndex: 3, widget:{id:"widget-3-body"}});
            });
        });
        describe("Error handling", function() {
            it("displays the appropriate alert when invalid parameters are passed", function() {

                $.post = function(url, data, handler) {
                    handler({error: true, errorCode: "INVALID_PARAMS"});
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
                };
                alert = function(str) {
                    expect(str).toEqual("Rave attempted to update the server with your recent changes, " +
                            " but the server encountered an internal error.");
                };
                rave.api.rpc.moveWidget({targetRegion: {id:"region-1"}, currentRegion: {id:"region-2"}, targetIndex: 3, widget:{id:"widget-3-body"}});
            });
        });
    })


});

