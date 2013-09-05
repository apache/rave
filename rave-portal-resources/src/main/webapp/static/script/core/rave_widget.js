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
 * @module rave_widget
 * @requires rave_api
 * @requires rave_view_manager
 * @requires rave_providers
 */
define(['underscore', './eventemitter', 'core/rave_api', 'core/rave_view_manager', 'core/rave_state_manager', 'core/rave_providers'],
    function (_, EventEmitter, api, viewManager, stateManager, providers) {

        /**
         * Workaround for IE8 (HTMLElement is undefined)
         * An item will have a nodeType of 1 if it is an HTMLElement
         * Defaults to use instanceof if HTMLElement is defined
         */
        var isHTMLElement = (function () {
            if (window.HTMLElement) {
                return function (item) {
                    return item instanceof HTMLElement;
                }
            } else {
                return function (item) {
                    return item.nodeType && item.nodeType == 1;
                }
            }
        })();

        /**
         * Constructor for the rave RegionWidget object
         * @param definition {object} json object defining the regionWidget
         * @param definition.type {string} string corresponding to the regionWidget provider ('opensocial' | 'w3c').
         * Lookup of provider is case-insensitive.
         * @throws Will throw an error if definition.type does not match a registered provider, as defined in
         * rave_providers (case-insensitive).
         * @constructor
         * @alias module:rave_widget
         * @see module:rave_providers
         */
        var Widget = function (definition) {
            var provider = definition.type;

            _.extend(this, definition);

            this._provider = providers[provider.toLowerCase()];
            this._el = document.createElement('div');
            this._surface = stateManager.getDefaultView();
            this._opts = {view: stateManager.getDefaultView()};

            if (!this._provider) {
                throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                    'Provider ' + provider + ' is not registered.');
            }

            this._provider.initWidget(this);
        }

        _.extend(Widget.prototype, EventEmitter.prototype);

        /**
         * Extends the RegionWidget's prototype. Convenience function for adding functionality
         * to the RegionWidget interface. Extended functions will also trigger events.
         * @param key {string | object} If first argument is an object, it will iterate over the keys invoke extend
         * for each key / value pair.
         * @param fn {function}
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
            /**
             * Renders the regionWidget onto the page into the given dom element. This will delegate to
             * the regionWidget's provider.
             * @param [el] {(HTMLElement | string)} DOM element into which the widget iframe will be rendered. If the
             * argument is an HTMLElement, the regionWidget will be rendered directly into that element. If it is a
             * string, it will delegate to the rave_view_manager - rendering the registered view, and rendering the
             * regionWidget into the return value of that views getWidgetSite method.
             * A regionWidget keeps a handle on the dom element it was rendered into, so after the first render
             * if this argument is omitted, the region will re-render into that element.
             * @param [opts] {object} Render options which are passed to the provider's renderWidget method.
             * Supported options are therefore provider-specific.
             * @return this
             * @see module:rave_view_manager
             */
            'render': function (el) {
                if (el) {
                    if (!(isHTMLElement(el))) {
                        throw new Error('Cannot render widget. You must provide an el to render the view into');
                    }

                    el.appendChild(this._el);
                }
                this._provider.renderWidget(this, this._opts);

                return this;
            },

            'navigate': function (surface, opts) {
                opts = opts || {};

                this._opts = opts;
                //if the new view surface is supported, change the widget's surface
                if (viewManager.getView(surface)) {
                    this._surface = surface;
                }
                this.render();
            },


            'unrender': function () {
                this._provider.unrenderWidget(this);
                return this;
            },

            /**
             * Prints error messages into the given dom element.
             * @param errors {string}
             */
            'renderError': function (errors) {
                this._el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
            },

            /**
             * Sets the collapsed property of the regionWidget object to true, and persists state to the server.
             * Does not cause any dom manipulation.
             */
            'hide': function () {
                this.collapsed = true;
            },

            /**
             * Sets the collapsed property of the regionWidget object to false, and persists state to the server.
             * Does not cause any dom manipulation.
             */
            'show': function () {
                this.collapsed = false;
            }
        });

        return Widget;
    })
