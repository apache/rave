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

describe('rave_widget', function(){

    beforeEach(function () {
        /**************
         Widget Examples
         ****************/
        validWidget = {
            "type": "OpenSocial",
            "regionWidgetId": "5",
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
                        "preferredHeight": 0, "quirks": true, "preferredWidth": 0}
                },
                "modulePrefs": {
                }
            },
            "userPrefs": { "headlineCount": "9", "summaryCount": "1"},
            "collapsed": false,
            "widgetId": "3"
        };

        invalidWidget = {
            "type": "OpenSocial",
            "regionWidgetId": "5",
            "widgetUrl": "http://widgets.nytimes.com/packages/html/igoogle/topstories.xml",
            "error": "error text"
        };

        /*************************
         Mock Module Dependencies
         ************************/
        mockApi = {
            rest: {
                saveWidgetCollapsedState: function(){},
                saveWidgetPreference: function(){},
                saveWidgetPreferences: function(){}
            },
            rpc: {
                removeWidget: function(){},
                moveWidgetToPage: function(){},
                moveWidgetToRegion: function(){}
            }
        }

        mockViewManager = {
            destroyView: function(){},
            renderView: function(){},
        }

        /****************
           Mock View Obj
         ****************/
        viewObject = {
            render: function () {
            },
            getWidgetSite: function () {
                return document.createElement('div');
            },
            destroy: function () {
            }
        }

        /****************
           Mock Provider
         ****************/
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

        /**************
              Spies
         ****************/
        _.each(mockApi.rest, function (fn, key) {
            spyOn(mockApi.rest, key);
        });
        _.each(mockApi.rpc, function (fn, key) {
            spyOn(mockApi.rpc, key);
        });
        spyOn(mockViewManager, 'destroyView');
        spyOn(mockViewManager, 'renderView').andReturn(viewObject);
        _.each(provider, function (fn, key) {
            spyOn(provider, key);
        });

        _.each(viewObject, function (fn, key) {
            spyOn(viewObject, key).andCallThrough();
        });

        RegionWidget = testr('core/rave_widget', {
            './rave_api': mockApi,
            './rave_view_manager': mockViewManager,
            './rave_providers': {
                opensocial: provider
            }
        })
    });

    describe('constructor', function () {
        it('returns a new object with all the properties of the definition', function () {
            var regionWidget = new RegionWidget(validWidget);
            _.each(validWidget, function (value, key) {
                expect(regionWidget[key]).toEqual(value);
            });
        });

        it('throws an error if the expected provider is not registered', function () {
            RegionWidgetNoProvider = testr('core/rave_widget', {
                './rave_api': mockApi,
                './rave_view_manager': mockViewManager,
                './rave_providers': {
                    opensocial: undefined
                }
            })

            expect(function () {
                new RegionWidgetNoProvider(validWidget);
            }).toThrow();
        });

        it('invokes the initWidget method of the provider', function () {
            expect(provider.initWidget).not.toHaveBeenCalled();
            var regionWidget = new RegionWidget(validWidget);
            expect(provider.initWidget).toHaveBeenCalled();
        });
    });

    describe('extend', function () {
        it('allows you to extend the RegionWidget prototype', function () {
            var mixin = {
                fn: function () {
                },
                prop: 7
            }

            spyOn(mixin, 'fn');

            var regionWidget1 = new RegionWidget(validWidget);
            expect(regionWidget1.fn).toBeUndefined();
            RegionWidget.extend(mixin);
            var regionWidget2 = new RegionWidget(validWidget);
            expect(regionWidget1.prop).toEqual(7);
            expect(regionWidget2.prop).toEqual(7);

            regionWidget1.fn();
            regionWidget2.fn();
            expect(mixin.fn.calls.length).toEqual(2);
        });
    });

    describe('prototype', function () {
        var regionWidget,
            errorRegionWidget;

        beforeEach(function () {
            regionWidget = new RegionWidget(validWidget);
            errorRegionWidget = new RegionWidget(invalidWidget);
        })

        describe('render', function () {
            it('throws an error if you do not provide a valid el to render into', function(){
                function doRenderWithNoArgs(){
                    regionWidget.render();
                }
                function doRenderWithOptsOnly(){
                    regionWidget.render({view: 'myView'});
                }
                function doRenderWithNonDomObject(){
                    regionWidget.render({}, {view: 'myView'});
                }

                expect(doRenderWithNoArgs).toThrow();
                expect(doRenderWithOptsOnly).toThrow();
                expect(doRenderWithNonDomObject).toThrow();
            });

            it('retrieves a registered view if el is a string', function () {
                regionWidget.render('asdf');

                expect(mockViewManager.renderView).toHaveBeenCalledWith('asdf', regionWidget);
                expect(viewObject.getWidgetSite).toHaveBeenCalled();
                expect(regionWidget._view).toBe(viewObject);
            });

            it("invokes the provider's renderWidget function", function () {
                var el =  document.createElement('div');
                var opts = {opts: true}
                regionWidget.render(el, opts);

                expect(provider.renderWidget).toHaveBeenCalledWith(regionWidget, el, opts);
            });

            it("defaults to the widget's current el if the widget has been rendered already", function(){
                var el =  document.createElement('div');
                var opts = {opts: true}

                expect(function(){
                    regionWidget.render();
                }).toThrow();

                regionWidget.render(el, opts);
                expect(provider.renderWidget).toHaveBeenCalledWith(regionWidget, el, opts);
                regionWidget.render(opts);
                expect(provider.renderWidget).toHaveBeenCalledWith(regionWidget, el, opts);
            });
        });

        describe('hide', function () {
            it('calls the rave api and updates the widget state', function () {
                regionWidget.collapsed = 'asdf';

                expect(regionWidget.collapsed).toEqual('asdf');
                regionWidget.hide();
                expect(regionWidget.collapsed).toEqual(true);
                expect(mockApi.rest.saveWidgetCollapsedState).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId,
                    collapsed: true
                });
            });
        });

        describe('show', function () {
            it('calls the rave api and updates the widget state', function () {
                regionWidget.collapsed = 'asdf';

                expect(regionWidget.collapsed).toEqual('asdf');
                regionWidget.show();
                expect(regionWidget.collapsed).toEqual(false);
                expect(mockApi.rest.saveWidgetCollapsedState).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId,
                    collapsed: false
                });
            });
        });

        describe('close', function () {
            it('invokes closeWidget on provider and calls the rave api', function () {
                var opts = {opts: true}
                regionWidget.close(opts);

                expect(mockViewManager.destroyView).not.toHaveBeenCalled();
                expect(provider.closeWidget).toHaveBeenCalledWith(regionWidget, opts);
                expect(mockApi.rpc.removeWidget).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId
                });
            });

            it('destroys the associated view', function () {
                regionWidget.render('asdf');

                expect(regionWidget._view).toBe(viewObject);

                regionWidget.close();
                expect(mockViewManager.destroyView).toHaveBeenCalledWith(viewObject);
            });
        });

        describe('moveToPage', function () {
            it('calls the rave api', function () {
                var cb = function () {
                }
                regionWidget.moveToPage(1, cb);

                expect(mockApi.rpc.moveWidgetToPage).toHaveBeenCalledWith({
                    toPageId: 1,
                    regionWidgetId: regionWidget.regionWidgetId,
                    successCallback: cb
                });
            })
        });

        describe('moveToRegion', function () {
            it('calls the rave api', function () {
                regionWidget.moveToRegion(1, 2, 3);

                expect(mockApi.rpc.moveWidgetToRegion).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId,
                    fromRegionId: 1,
                    toRegionId: 2,
                    toIndex: 3
                });
            });
        });

        describe('savePreference', function () {
            it('calls the rave api updates the widget state', function () {
                var name = 'color';
                var pref = 'blue';
                var prefs = {color: 'blue'}
                regionWidget.savePreference(name, pref);

                expect(mockApi.rest.saveWidgetPreference).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId,
                    prefName: name,
                    prefValue: pref
                });
            });
        });

        describe('savePreferences', function () {
            it('calls the rave api updates the widget state', function () {
                var prefs = {color: 'blue', number: 7}
                regionWidget.savePreferences(prefs);

                expect(regionWidget.userPrefs).toBe(prefs);
                expect(mockApi.rest.saveWidgetPreferences).toHaveBeenCalledWith({
                    regionWidgetId: regionWidget.regionWidgetId,
                    userPrefs: prefs
                });
            });
        });
    });
})
