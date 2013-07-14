/* * Licensed to the Apache Software Foundation (ASF) under one
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


describe('rave_widget_manager', function () {
    beforeEach(function () {
        /**************
         Widget Examples
         ****************/
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
            "regionId": "1",
            "render": function(){}
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
            "regionId": "2",
            "render":function(){}
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
            reset: function(){
                registeredViews = {};
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

        mockRegionWidget = {}

        mockRegionWidget.RegionWidget = testr('core/rave_widget', {
            './rave_api': mockApi,
            './rave_view_manager': mockViewManager,
            './rave_providers': {
                opensocial: provider
            }
        })

        spyOn(mockRegionWidget, 'RegionWidget').andCallFake(function(def){return def})

        WidgetManager = testr('core/rave_widget_manager', {
            './rave_widget': mockRegionWidget.RegionWidget
        })

        eventManager = testr('core/rave_event_manager')
    });

    describe('registerWidget', function () {

        it('accepts one or two arguments', function(){
            var registeredWidget1 = WidgetManager.registerWidget(widget1);
            var registeredWidget2 = WidgetManager.registerWidget(2, widget2);

            expect(registeredWidget1).toEqual(widget1);
            expect(registeredWidget2).toEqual(widget2);
        });
    });

    describe('unregisterWidget', function () {
        it('unregisters a widget by regionWidgetId', function () {
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBeUndefined();
            WidgetManager.registerWidget(widget1);
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBe(widget1);
            WidgetManager.unregisterWidget(widget1.regionWidgetId);
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBeUndefined();
        });

        it('unregisters a widget by object', function () {
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBeUndefined();
            WidgetManager.registerWidget(widget1);
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBe(widget1);
            WidgetManager.unregisterWidget(widget1);
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBeUndefined();
        });
    });

    describe('getWidget', function () {
        it('can retrieve a registered widget by regionWidgetId', function () {
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBeUndefined();
            WidgetManager.registerWidget(widget1);
            expect(WidgetManager.getWidget(widget1.regionWidgetId)).toBe(widget1);
        });
    });

    describe('getWidgets', function () {
        it('returns the set of all registered widgets', function () {
            expect(WidgetManager.getWidgets()).toEqual([]);
            WidgetManager.registerWidget(widget1);
            expect(WidgetManager.getWidgets()).toEqual([widget1]);
        });

        it('filters widgets', function(){
            WidgetManager.registerWidget(widget1);
            WidgetManager.registerWidget(widget2);

            expect(WidgetManager.getWidgets({regionId: "1"})).toEqual([widget1]);
            expect(WidgetManager.getWidgets({widgetId: "3"})).toEqual([widget1, widget2]);
        });
    });

    describe('renderWidgets', function(){
        it('renders the widget correctly', function(){
            WidgetManager.registerWidget(widget1);
            var widget = WidgetManager.getWidget(widget1.regionWidgetId);

            spyOn(widget, 'render');

            expect(widget.render).not.toHaveBeenCalled();
            WidgetManager.renderWidgets('el', 'opts');
            expect(widget.render).toHaveBeenCalled();
        })
    })
});
