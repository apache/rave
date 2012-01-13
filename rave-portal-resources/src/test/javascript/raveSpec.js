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
            var callCount = 0;
            return {
                TYPE : type,

                init : function() {
                    called = true;
                },
                initWidget: function(widget) {
                    expect(widget.type).toEqual(type);
                    callCount++;
                },
                initWidgetsWasCalled : function(expected) {
                    return expected == callCount;
                },
                initWasCalled : function() {
                    return called;
                }
            };
        })();
    }

    describe("client message add and get", function() {
        it("adds a client message to the internal map based on a key and verifies it can be returned via getter", function() {
            var theKey = "myKey";
            var theMessage = "my message";
            expect(rave.getClientMessage(theKey)).toEqual(null);
            rave.addClientMessage(theKey, theMessage);
            expect(rave.getClientMessage(theKey)).toEqual(theMessage);
        });
    });

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
        //Creates a mock jquery object with the functions we need in this context
        function createMockJQuery() {
            var html;
            var expression;
            var classMap = [];
            $ = function(expr) {

                if (typeof expr != "undefined") {
                    expression = expr;
                }

                return {
                    expression : function () {
                        return expression;
                    },
                    html : function (txt) {
                        if (typeof txt  == "string") {
                            html = txt;
                            return $;
                        } else {
                            return html;
                        }
                    },
                    attr: function(a, b) {

                    },                    
                    hasClass : function (className) {
                        return classMap.indexOf(className) != -1;
                    },
                    addClass : function (className) {
                        classMap.push(className);
                    },
                    removeClass: function(className) {                        
                        var idx = classMap.indexOf(className); 
                        if (idx != -1) {
                            classMap.splice(idx, 1); 
                        }
                    }   
                }
            };            
        }

        it("calls the appropriate providers", function() {
            var HIDDEN_CLASS = "hidden";            
            createMockJQuery(); 
            $().addClass(HIDDEN_CLASS);
            expect($().hasClass(HIDDEN_CLASS)).toEqual(true);
            
            var widgetsByRegionIdMap = {};
            widgetsByRegionIdMap[1] = [
                    {type:"FOO"},
                    {type:"BAR"},
                    {type:"FOO"},
                    {type:"BAR"}
            ];
            var provider1 = getMockProvider("FOO");
            var provider2 = getMockProvider("BAR");
            rave.registerProvider(provider1);
            rave.registerProvider(provider2);
            rave.initWidgets(widgetsByRegionIdMap);
            expect(provider1.initWidgetsWasCalled(2)).toBeTruthy();
            expect(provider2.initWidgetsWasCalled(2)).toBeTruthy();   
            expect($().hasClass(HIDDEN_CLASS)).toEqual(true);
        });

        it("renders widgets in the appropriate order (first 'row', second 'row', third 'row', ...)", function() {
            createMockJQuery(); 
            
            var widgetsByRegionIdMap = {};
            widgetsByRegionIdMap[1] = [
                    {type:"FOO", renderOrder:1},
                    {type:"FOO", renderOrder:4},
                    {type:"FOO", renderOrder:7},
                    {type:"FOO", renderOrder:9}
            ];
            widgetsByRegionIdMap[2] = [
                    {type:"FOO", renderOrder:2},
                    {type:"FOO", renderOrder:5},
                    {type:"FOO", renderOrder:8},
                    {type:"FOO", renderOrder:10},
                    {type:"FOO", renderOrder:11},
                    {type:"FOO", renderOrder:12}
            ];
            widgetsByRegionIdMap[3] = [
                    {type:"FOO", renderOrder:3},
                    {type:"FOO", renderOrder:6}
            ];
            var widgets = [];
            var provider1 = getMockProvider("FOO");
            var originalInitWidgetFunction = provider1.initWidget;
            provider1.initWidget = function(widget) {
                originalInitWidgetFunction(widget);
                widgets.push(widget);
            };
            rave.registerProvider(provider1);
            rave.initWidgets(widgetsByRegionIdMap);
            expect(provider1.initWidgetsWasCalled(12)).toBeTruthy();

            for (var i = 0; i < 12; i++) {
                expect(widgets[i].renderOrder).toEqual(i+1);
            }
        });

        it("puts widgets in buckets keyed by regionIds", function() {
            createMockJQuery(); 
             
            var widgetsByRegionIdMap = {};
            var regionOneKey = 1;
            var regionTwoKey = 2;
            rave.registerWidget(widgetsByRegionIdMap, regionOneKey, {arbitrary:"value"});
            rave.registerWidget(widgetsByRegionIdMap, regionOneKey, {arbitrary:"value"});

            rave.registerWidget(widgetsByRegionIdMap, regionTwoKey, {arbitrary:"value"});

            rave.registerWidget(widgetsByRegionIdMap, regionOneKey, {arbitrary:"value"});
            rave.registerWidget(widgetsByRegionIdMap, regionOneKey, {arbitrary:"value"});

            rave.registerWidget(widgetsByRegionIdMap, regionTwoKey, {arbitrary:"value"});

            expect(widgetsByRegionIdMap[regionOneKey].length).toEqual(4);
            expect(widgetsByRegionIdMap[regionTwoKey].length).toEqual(2);
        });

        it("Renders an error gadget when invalid widget is provided", function(){
            createMockJQuery();  
             
            var widgetsByRegionIdMap = {};
            widgetsByRegionIdMap[1] = [
                    {type:"FOO",  regionWidgetId:20},
                    {type:"BAR",  regionWidgetId:21},
                    {type:"FOO",  regionWidgetId:22},
                    {type:"BAR",  regionWidgetId:23},
                    {type:"NONE", regionWidgetId:43}
            ];
            
            var provider1 = getMockProvider("FOO");
            var provider2 = getMockProvider("BAR");
            rave.registerProvider(provider1);
            rave.registerProvider(provider2);
            rave.addClientMessage("widget.provider.error", "This widget type is currently unsupported.  Check with your administrator and be sure the correct provider is registered.");
            rave.initWidgets(widgetsByRegionIdMap);
            expect($().expression()).toEqual("#widget-43-body");
            expect($().html()).toEqual("This widget type is currently unsupported.  Check with your administrator and be sure the correct provider is registered.");
            expect(provider1.initWidgetsWasCalled(2)).toBeTruthy();
            expect(provider2.initWidgetsWasCalled(2)).toBeTruthy();
        });

        it("Renders a disabled gadget when disabled flag is set", function(){
            createMockJQuery();

            var widgetsByRegionIdMap = {};
            widgetsByRegionIdMap[1] = [
                    {type:"DISABLED",  regionWidgetId:20, disabledMessage: "Widget disabled"}
            ];

            rave.initWidgets(widgetsByRegionIdMap);
            expect($().expression()).toEqual("#widget-20-body");
            expect($().html()).toEqual("Widget disabled");
        });
        
        it("Renders the empty page message when page has no widgets", function(){
            var HIDDEN_CLASS = "hidden";
            createMockJQuery();  
            $().addClass(HIDDEN_CLASS);
            expect($().hasClass(HIDDEN_CLASS)).toEqual(true);
            
            var widgetsByRegionIdMap = {};
                                   
            rave.initWidgets(widgetsByRegionIdMap);
            expect($().hasClass(HIDDEN_CLASS)).toEqual(false);           
        });        
    });

    describe("initUI", function() {
        function createMockJQuery() {
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
                    },
                    attr: function(a,b) {

                    }
                }
            };
        }

        it("Initializes jQuery sortable when init is called", function() {
            createMockJQuery();
            rave.initUI();
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
            rave.initUI();
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
            var errorText = "Rave attempted to update the server with your recent changes, but the changes were rejected by the server as invalid.";
            createMockJQuery();

            rave.addClientMessage("api.rpc.error.invalid_params", errorText);
            rave.initUI();
            var sortableArgs = $().getSortableArgs();
            var mockItem = getMockItem();
            $.post = function(url, data, handler) {
                handler({error: true, errorCode: "INVALID_PARAMS"});
                    return {
                        error: function(a, b, c) {}
                    }
            };
            alert = function(str) {
                expect(str).toEqual(errorText);
            };
            sortableArgs.start({}, mockItem);
            sortableArgs.stop({}, mockItem);
        });
        it("displays the appropriate alert when a server error occurs", function() {
            var errorText = "Rave attempted to update the server with your recent changes, but the server encountered an internal error.";
            createMockJQuery();

            rave.addClientMessage("api.rpc.error.internal", errorText);
            rave.initUI();
            var sortableArgs = $().getSortableArgs();
            var mockItem = getMockItem();
            $.post = function(url, data, handler) {
                handler({error: true, errorCode: "INTERNAL_ERROR"});

                    return {
                        error: function(a, b, c) {}
                    }
            };
            alert = function(str) {
                expect(str).toEqual(errorText);
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
    
    describe("isFunction", function() {

        it("returns true when the object is a function", function() {
            var obj = function() { };
            var result = rave.isFunction(obj);
            expect(result).toEqual(true);
        });
        it("returns false when the object is a number", function() {
            var obj = 1;
            var result = rave.isFunction(obj);
            expect(result).toEqual(false);
        });        
        it("returns false when the object is a string", function() {
            var obj = "hello";
            var result = rave.isFunction(obj);
            expect(result).toEqual(false);
        }); 
        it("returns false when the object is an object", function() {
            var obj = {"myattr" : "myvalue"};
            var result = rave.isFunction(obj);
            expect(result).toEqual(false);
        }); 
        it("returns false when the object is null", function() {
            var obj = null;
            var result = rave.isFunction(obj);
            expect(result).toEqual(false);
        }); 
        it("returns false when the object is undefined", function() {
            var obj;
            var result = rave.isFunction(obj);
            expect(result).toEqual(false);
        }); 
    });        
    
    describe("toggleCollapseWidgetIcon", function() {
        //Creates a simple mock jquery object that mocks the functions used in this suite
        function createMockJQuery() {
            var expression;
            var classMap = [];
            
            $ = function(expr) {

                if (typeof expr != "undefined") {
                    expression = expr;
                }

                return {
                    expression : function () {
                        return expression;
                    },
                    hasClass : function (className) {
                        return classMap.indexOf(className) != -1;
                    },
                    addClass : function (className) {
                        classMap.push(className);
                    },
                    removeClass: function(className) {                        
                        var idx = classMap.indexOf(className); 
                        if (idx != -1) {
                            classMap.splice(idx, 1); 
                        }
                    }
                }
            };
            
        }

        it("changes icon from normal to collapsed", function() {                                  
            createMockJQuery();
            // setup the state so the widget display is "normal"           
            $().addClass("ui-icon-triangle-1-e");
           
            var widgetId = 99;
           
            rave.toggleCollapseWidgetIcon(widgetId);
           
            expect($().expression()).toEqual("#widget-" + widgetId + "-collapse");            
            expect($().hasClass("ui-icon-triangle-1-e")).toEqual(false);
            expect($().hasClass("ui-icon-triangle-1-s")).toEqual(true);                                        
        });
        
        it("changes icon from collapsed to normal", function() {                                  
            createMockJQuery();
            // setup the state so the widget display is "normal"           
            $().addClass("ui-icon-triangle-1-s");
           
            var widgetId = 99;
           
            rave.toggleCollapseWidgetIcon(widgetId);
           
            expect($().expression()).toEqual("#widget-" + widgetId + "-collapse");            
            expect($().hasClass("ui-icon-triangle-1-s")).toEqual(false);
            expect($().hasClass("ui-icon-triangle-1-e")).toEqual(true);                                        
        });        
       
    });    
    
    describe("change widget view state", function(){
        //Creates a simple mock jquery object that mocks the functions used in this suite
        function createMockJQuery() {
            var expression;
            var classMap = [];                        
            var valuesMap = {};
            
            $ = function(expr) {

                if (typeof expr != "undefined") {
                    expression = expr;
                }

                return {
                    expression : function () {
                        return expression;
                    },
                    hasClass : function (className) {
                        return classMap.indexOf(className) != -1;
                    },
                    addClass : function (className) {
                        classMap.push(className);
                    },
                    removeClass: function(className) {                        
                        var idx = classMap.indexOf(className); 
                        if (idx != -1) {
                            classMap.splice(idx, 1); 
                        }
                        return this;
                    },
                    sortable: function(option, attrName, attrValue) {
                        valuesMap["sortableOption"] = option;
                        valuesMap["sortableAttrName"] = attrName;
                        valuesMap["sortableAttrValue"] = attrValue;
                    },
                    click: function(args, fn) {
                        valuesMap["clickArgs"] = args;
                        valuesMap["clickFn"] = fn;
                    },
                    button: function(option, attrName, attrArgs) {
                        valuesMap["buttonOption"] = option;
                        valuesMap["buttonAttrName"] = attrName;
                        valuesMap["buttonAttrArgs"] = attrArgs;
                    },
                    hide: function() {
                        valuesMap["hideWasCalled-" + expression] = true;
                    },
                    show: function() {
                        valuesMap["showWasCalled-" + expression] = true;
                    },                    
                    height: function() {
                        
                    },
                    width: function() {
                        
                    },
                    css: function() {
                        
                    },
                    prepend: function() {
                        
                    },
                    remove: function() {
                        valuesMap["removeWasCalled"] = true;
                    },
                    getValue: function(varName) {
                        return valuesMap[varName];
                    }                    
                }
            };            
        }
        
        it("successfully maximizes the widget", function() {                                  
            createMockJQuery();
            
            var mockWidget = {   
                maximizeWasCalled: false,                
                maximize: function() { this.maximizeWasCalled = true; }
            }
                                      
            var args = {};
            args.data = {};
            args.data.id = 99;
            spyOn(rave, "getRegionWidgetById").andReturn(mockWidget);
            
            rave.maximizeWidget(args);                      
           
            // verify the sortable parameters                        
            expect($().getValue("sortableOption")).toEqual("option");
            expect($().getValue("sortableAttrName")).toEqual("disabled");
            expect($().getValue("sortableAttrValue")).toEqual(true);            
            // verify the CSS styles
            expect($().hasClass("widget-wrapper-canvas")).toEqual(true);
            expect($().hasClass("widget-wrapper")).toEqual(false);           
            // verify widget menu hide was called
            expect($().getValue("hideWasCalled-#widget-" + args.data.id + "-widget-menu-wrapper")).toEqual(true);  
            // verify widget minimize show was called
            expect($().getValue("showWasCalled-#widget-" + args.data.id + "-min")).toEqual(true);        
            // verify getRegionWidgetById called
            expect(rave.getRegionWidgetById).toHaveBeenCalledWith(args.data.id); 
            // verify collapse/restore icon hide was called
            expect($().getValue("hideWasCalled-#widget-" + args.data.id + "-collapse")).toEqual(true);                 
            // verify widget.maximize was called
            expect(mockWidget.maximizeWasCalled).toEqual(true);
                   
        });        
        
        it("successfully minimizes the widget", function() {                                  
            createMockJQuery();
            
            var mockWidget = {   
                minimizeWasCalled: false,                
                minimize: function() { this.minimizeWasCalled = true; }
            }
                                      
            var args = {};
            args.data = {};
            args.data.id = 99;
            spyOn(rave, "getRegionWidgetById").andReturn(mockWidget);
            
            rave.minimizeWidget(args);                      
            
            // verify remove was called
            expect($().getValue("removeWasCalled")).toEqual(true);        
            // verify the sortable parameters
            expect($().getValue("sortableOption")).toEqual("option");
            expect($().getValue("sortableAttrName")).toEqual("disabled");
            expect($().getValue("sortableAttrValue")).toEqual(false);            
            // verify the CSS styles
            expect($().hasClass("widget-wrapper-canvas")).toEqual(false);
            expect($().hasClass("widget-wrapper")).toEqual(true);
            // verify widget minimize hide was called
            expect($().getValue("hideWasCalled-#widget-" + args.data.id + "-min")).toEqual(true);                            
            // verify widget menu show was called
            expect($().getValue("showWasCalled-#widget-" + args.data.id + "-widget-menu-wrapper")).toEqual(true);                                  
            // verify collapse/restore icon show was called
            expect($().getValue("showWasCalled-#widget-" + args.data.id + "-collapse")).toEqual(true);                 
            // verify getRegionWidgetById called
            expect(rave.getRegionWidgetById).toHaveBeenCalledWith(args.data.id);                   
            // verify widget.minimize was called
            expect(mockWidget.minimizeWasCalled).toEqual(true);             
        });                    
    });
});