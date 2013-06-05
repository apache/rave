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
define(['underscore',  './eventemitter', './rave_api', './rave_providers'], function (_, EventEmitter, api, providers) {

    var Widget = function (definition) {
        var provider = definition.type;

        _.extend(this, definition);

        this._provider = providers[provider.toLowerCase()];
        this._el = document.createElement('div');
        this._surface = Widget.defaultView;

        if (!this._provider) {
            throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                'Provider ' + provider + ' is not registered.');
        }

        this._provider.initWidget(this);
    }

    /*
     Static properties
     */
    Widget.defaultView = 'default';
    Widget.defaultWidth = 320;
    Widget.defaultHeight = 200;

    /*
     Set up widget as an event emitter
     */
    _.extend(Widget.prototype, EventEmitter.prototype);

    /*
     Convenience function for adding functionality to Widget prototype with events
     */
    Widget.extend = function (key, fn) {
        var self = this;
        if (_.isObject(key)) {
            return _.each(key, function (f, k) {
                self.extend(k, f);
            });
        }
        this.prototype[key] = function () {
            var args = _.toArray(arguments);
            fn.apply(this, args);
            this.emitEvent(key, args);
        }
    }

    Widget.extend({

        'render': function (el) {
            if (el) {
                el.appendChild(this._el);
            }

            this._provider.renderWidget(this, this._opts);
            return this;
        },

        'navigate': function (opts) {
            opts = opts || {};

            var viewSurface = opts.view || Widget.defaultView;
            viewSurface = viewSurface.split('.')[0];

            this._opts = opts;
            this._surface = viewSurface;
            this.render();
        },

        'unrender': function () {
            this._provider.unrenderWidget(this);
            return this;
        },

        'renderError': function (errors) {
            this._el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
        },

        'hide': function () {
            this.collapsed = true;

            api.rest.saveWidgetCollapsedState({
                regionWidgetId: this.id,
                collapsed: this.collapsed
            });
        },

        'show': function () {
            this.collapsed = false;

            api.rest.saveWidgetCollapsedState({
                regionWidgetId: this.id,
                collapsed: this.collapsed
            });
        },

        'close': function (opts) {
            this.unrender();

            api.rpc.removeWidget({
                regionWidgetId: this.id
            });
        },

        'moveToPage': function (toPageId, cb) {
            api.rpc.moveWidgetToPage({
                toPageId: toPageId,
                regionWidgetId: this.id,
                successCallback: cb
            });
        },

        'moveToRegion': function (fromRegionId, toRegionId, toIndex) {
            api.rpc.moveWidgetToRegion({
                regionWidgetId: this.id,
                fromRegionId: fromRegionId,
                toRegionId: toRegionId,
                toIndex: toIndex
            });
        },

        'getPrefs': function () {
            var self = this;
            var combined = [];
            //TODO: I think this is opensocial specific - need to investigate wookie and possibly delegate to providers
            _.each(self.metadata.userPrefs, function (data) {
                var value = self.userPrefs[data.name];
                data = _.clone(data);
                data.value = value || data.defaultValue;
                data.displayName = data.displayName || data.name;
                combined.push(data);
            });
            return _.isEmpty(combined) ? undefined : combined;
        },

        'setPrefs': function (name, val) {
            if (_.isObject(name)) {
                var updatedPrefs = name;
                this.userPrefs = _.object(_.pluck(updatedPrefs, 'name'), _.pluck(updatedPrefs, 'value'));
                api.rest.saveWidgetPreferences({regionWidgetId: this.id, userPrefs: this.userPrefs});
            } else {
                this.userPrefs[name] = val;
                api.rest.saveWidgetPreference({regionWidgetId: this.id, prefName: name, prefValue: val});
            }
        }
    })


    return Widget;
});
/*
 Rave RegionWidget Interface

 Dependencies:
 api
 */