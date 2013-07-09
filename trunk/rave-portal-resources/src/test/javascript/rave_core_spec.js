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

describe('rave', function () {
    var providerName = 'opensocial',
        viewName = 'modal',
        provider,
        widget1,
        widget2,
        viewObject,
        testScope = {};

    /*
     setup mock objects to be used in tests
     */
    beforeEach(function () {
        provider = {
            init: function () {
            },
            initWidget: function () {
            },
            renderWidget: function () {
            },
            closeWidget: function () {
            },
            setDefaultGadgetSize: function () {
            },
            setDefaultGadgetView: function () {
            }
        }
        _.each(provider, function (fn, key) {
            spyOn(provider, key);
        });

        viewObject = {
            render: function () {
            },
            getWidgetSite: function () {
            },
            destroy: function () {
            }
        }
        _.each(viewObject, function (fn, key) {
            spyOn(viewObject, key);
        });

        testScope.ViewConstructor = function () {
            this.render = function () {
            };
            this.getWidgetSite = function () {
            };
            this.destroy = function () {
            };

            spyOn(this, 'render');
            spyOn(this, 'getWidgetSite');
            spyOn(this, 'destroy');
        }
        spyOn(testScope, 'ViewConstructor').andCallThrough();

        spyOn(rave, 'RegionWidget').andCallFake(function (def) {
            return this;
        });

        widget1 = {
            "type": "OpenSocial",
            "regionWidgetId": "1",
            "widgetUrl": "http://widgets.nytimes.com/packages/html/igoogle/topstories.xml",
            "metadata": {
                "userPrefs": {
                },
                "hasPrefsToEdit": true,
                "views": {
                    "home": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0},
                    "default": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0},
                    "canvas": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0}},
                "modulePrefs": {
                }
            },
            "userPrefs": { "headlineCount": "9", "summaryCount": "1"},
            "collapsed": false,
            "widgetId": "3",
            "regionId": "1"
        };

        widget2 = {
            "type": "OpenSocial",
            "regionWidgetId": "2",
            "widgetUrl": "http://widgets.nytimes.com/packages/html/igoogle/topstories.xml",
            "metadata": {
                "userPrefs": {
                },
                "hasPrefsToEdit": true,
                "views": {
                    "home": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0},
                    "default": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0},
                    "canvas": {
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0}},
                "modulePrefs": {
                }
            },
            "userPrefs": { "headlineCount": "9", "summaryCount": "1"},
            "collapsed": false,
            "widgetId": "3",
            "regionId": "2"
        };
    });


    afterEach(function () {
        rave.reset();
    });

    describe('registerProvider', function () {

        it('does not fire init until rave.init is called', function () {
            rave.registerProvider(providerName, provider);
            expect(provider.init).not.toHaveBeenCalled();
            rave.init();
            expect(provider.init).toHaveBeenCalled();
        });

        it('automatically fires init on a provider registered after rave.init was called', function () {
            expect(provider.init).not.toHaveBeenCalled();
            rave.init();
            rave.registerProvider(providerName, provider);
            expect(provider.init).toHaveBeenCalled();
        });

    });

    describe('getProvider', function () {

        it('can retrieve a registered provider and is case insensitive', function () {
            expect(rave.getProvider(providerName)).toBeUndefined();
            rave.registerProvider(providerName, provider);
            expect(rave.getProvider(providerName)).toBe(provider);
            expect(rave.getProvider(providerName.toUpperCase())).toBe(provider);
        });

    });

    describe('registerWidget', function () {

        it('accepts one or two arguments', function(){
            var registeredWidget1 = rave.registerWidget(widget1);
            var registeredWidget2 = rave.registerWidget(2, widget2);

            expect(registeredWidget1).toEqual(widget1);
            expect(registeredWidget2).toEqual(widget2);
        });

        it('does not call RegionWidget constructor until rave.init is called', function () {
            rave.registerWidget(widget1);
            expect(rave.RegionWidget).not.toHaveBeenCalled();
            rave.init();
            expect(rave.RegionWidget).toHaveBeenCalled();
        });

        it('instatiates a new RegionWidget immediately if rave.init was already called', function () {
            expect(rave.RegionWidget).not.toHaveBeenCalled();
            rave.init();
            rave.registerWidget(widget1);
            expect(rave.RegionWidget).toHaveBeenCalled();
        });

    });

    describe('getWidget', function () {
        it('can retrieve a registed widget by regionWidgetId', function () {
            expect(rave.getWidget(widget1.regionWidgetId)).toBeUndefined();
            rave.registerWidget(widget1);
            expect(rave.getWidget(widget1.regionWidgetId)).toBe(widget1);
        });
    });

    describe('getWidgets', function () {
        it('returns the set of all registered widgets', function () {
            expect(rave.getWidgets()).toEqual([]);
            rave.registerWidget(widget1);
            expect(rave.getWidgets()).toEqual([widget1]);
        });

        it('filters widgets', function(){
            rave.registerWidget(widget1);
            rave.registerWidget(widget2);

            expect(rave.getWidgets({regionId: "1"})).toEqual([widget1]);
            expect(rave.getWidgets({widgetId: "3"})).toEqual([widget1, widget2]);
        });
    });

    describe('unregisterWidget', function () {
        it('unregisters a widget by regionWidgetId', function () {
            expect(rave.getWidget(widget1.regionWidgetId)).toBeUndefined();
            rave.registerWidget(widget1);
            expect(rave.getWidget(widget1.regionWidgetId)).toBe(widget1);
            rave.unregisterWidget(widget1.regionWidgetId);
            expect(rave.getWidget(widget1.regionWidgetId)).toBeUndefined();
        });

        it('unregisters a widget by object', function () {
            expect(rave.getWidget(widget1.regionWidgetId)).toBeUndefined();
            rave.registerWidget(widget1);
            expect(rave.getWidget(widget1.regionWidgetId)).toBe(widget1);
            rave.unregisterWidget(widget1);
            expect(rave.getWidget(widget1.regionWidgetId)).toBeUndefined();
        });
    });

    describe('registerView / getView', function () {
        it('registers and retrieves views and is case insensitive', function () {
            expect(rave.getView(viewName)).toBeUndefined();
            rave.registerView(viewName, viewObject);
            expect(rave.getView(viewName)).toBe(viewObject);
            expect(rave.getView(viewName.toUpperCase())).toBe(viewObject);
        });
    });

    describe('renderView', function () {
        it('throws an error if you render an unregistered view', function () {
            expect(function () {
                rave.renderView('asdf')
            }).toThrow();
        });

        it('invokes render on a view object', function () {
            rave.registerView(viewName, viewObject);
            rave.renderView(viewName);

            expect(viewObject.render).toHaveBeenCalled();
        });

        it('returns the view object', function () {
            rave.registerView(viewName, viewObject);
            expect(rave.renderView(viewName)).toBe(viewObject);
        });

        it('instantiates a view constructor and calls its render', function () {
            rave.registerView(viewName, testScope.ViewConstructor);
            var viewInstance = rave.renderView(viewName);

            expect(testScope.ViewConstructor).toHaveBeenCalled();
            expect(viewInstance.render).toHaveBeenCalled();
            expect(viewInstance.destroy).not.toHaveBeenCalled();
        });

        it('passes through any arbitrary arguments to the render function', function () {
            //with view object
            rave.registerView(viewName, viewObject);
            rave.renderView(viewName, 1, 'bird', {hello: 'world'});
            expect(viewObject.render).toHaveBeenCalledWith(1, 'bird', {hello: 'world'});

            //or view constructor
            rave.registerView(viewName, viewObject);
            var viewInstance = rave.renderView(viewName);
            expect(viewInstance.render).toHaveBeenCalledWith(1, 'bird', {hello: 'world'});
        });

        it('assigns a uid to a rendered view', function () {
            rave.registerView(viewName, viewObject);
            var viewInstance = rave.renderView(viewName);

            expect(viewInstance._uid).toBeDefined();
        });
    });

    describe('getRenderedView', function () {
        it('retrieves views that have been rendered', function () {
            rave.registerView(viewName, viewObject);
            var viewInstance1 = rave.renderView(viewName);
            var viewInstance2 = rave.renderView(viewName);

            expect(rave.getRenderedView(viewInstance1._uid)).toBe(viewInstance1);
            expect(rave.getRenderedView(viewInstance2._uid)).toBe(viewInstance2);
            expect(rave.getRenderedView('asdfasdf')).toBeUndefined();
        });
    });

    describe('destroyView', function () {
        it('invokes destroy on a view object', function () {
            rave.registerView(viewName, viewObject);
            rave.renderView(viewName);
            rave.destroyView(viewObject);

            expect(viewObject.destroy).toHaveBeenCalled();
        });

        it('invokes destroy on a view instance', function () {
            rave.registerView(viewName, viewObject);
            var viewInstance = rave.renderView(viewName);
            rave.destroyView(viewInstance);

            expect(viewInstance.destroy).toHaveBeenCalled();
        });

        it('invokes destroy on a view identified by uid', function () {
            rave.registerView(viewName, viewObject);
            var viewInstance = rave.renderView(viewName);
            rave.destroyView(viewInstance._uid);

            expect(viewInstance.destroy).toHaveBeenCalled();
        });

        it('removes view from renderedViews registry', function () {
            rave.registerView(viewName, viewObject);
            var viewInstance = rave.renderView(viewName);
            expect(rave.getRenderedView(viewInstance._uid)).toBe(viewInstance);

            rave.destroyView(viewInstance._uid);
            expect(rave.getRenderedView(viewInstance._uid)).toBeUndefined();
        });
    });

    describe('getManagedHub', function () {
        it('throws an error if OpenAjax is not defined', function () {
            OpenAjax = undefined;
            expect(rave.getManagedHub).toThrow();
        });

        it('creates a new hub if not currently defined', function () {
            OpenAjax = {
                hub: {
                    ManagedHub: function () {
                    }
                }
            }

            spyOn(OpenAjax.hub, 'ManagedHub').andCallThrough();

            var hub = rave.getManagedHub();
            expect(OpenAjax.hub.ManagedHub).toHaveBeenCalled();
            expect(rave.getManagedHub()).toBe(hub);
            expect(OpenAjax.hub.ManagedHub.calls.length).toEqual(1);
        });
    });

    describe('registerOnInitHandler', function () {
        it('throws an error if handler is not a function', function () {
            expect(function () {
                rave.registerOnInitHandler({})
            }).toThrow();
        });

        it('registers a handler that is fired on init in the order registered', function () {
            var arr = [];

            rave.registerOnInitHandler(function () {
                arr.push(1);
            });
            rave.registerOnInitHandler(function () {
                arr.push(2);
            });
            expect(arr).toEqual([]);
            rave.init();
            expect(arr).toEqual([1, 2]);
        });

        it('immediately invokes a handler if rave.init has already been called', function () {
            var scope = {
                handler: function () {
                }
            };

            spyOn(scope, 'handler');

            rave.init();
            expect(scope.handler).not.toHaveBeenCalled();
            rave.registerOnInitHandler(scope.handler);
            expect(scope.handler).toHaveBeenCalled();
        });
    });
});