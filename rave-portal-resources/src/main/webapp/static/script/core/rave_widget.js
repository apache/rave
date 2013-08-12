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
define(['underscore', 'core/rave_api', 'core/rave_view_manager', 'core/rave_providers'],
    function (_, api, viewManager, providers) {

        function getProvider(name) {
            return providers[name.toLowerCase()];
        }

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

            this._provider = getProvider(provider);

            if (!this._provider) {
                throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                    'Provider ' + provider + ' is not registered.');
            }

            this._provider.initWidget(this);
        }

        /**
         * Workaround for IE8 (HTMLElement is undefined)
         * An item will have a nodeType of 1 if it is an HTMLElement
         * Defaults to use instanceof if HTMLElement is defined
         */
        isHTMLElement = (function() {
            if (window.HTMLElement) {
                return function(item) {
                    return item instanceof HTMLElement;
                }
            } else {
                return function(item) {
                    return item.nodeType && item.nodeType == 1;
                }
            }
        })();

        /**
         * Extends the RegionWidget's prototype. Convenience function for adding functionality
         * to the RegionWidget interface
         * @param mixin {object}
         */
        Widget.extend = function (mixin) {
            _.extend(this.prototype, mixin);
        }

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
        Widget.prototype.render = function (el, opts) {



            //if we receive only one argument, and the first arg is not a string or dom element, assume it is an opts object
            //and el should default to the widgets current render element
            if (!opts && !(_.isString(el) || (isHTMLElement(el)))) {
                opts = el;
                el = this._el;
            }
            //if el is a string, go to rave's view system
            if (_.isString(el)) {
                //TODO: potential memory leak - rendering a widget into new views does not force cleanup of current view
                var view = viewManager.renderView(el, this);
                el = view.getWidgetSite();
                this._view = view;
            }
            //at this point el must be a valid dom element. if not, throw an error
            if (!(isHTMLElement(el))) {
                throw new Error('Cannot render widget. You must provide an el to render the view into');
            }
            this._el = el;
            this._provider.renderWidget(this, el, opts);
            return this;
        }

        /**
         * Prints error messages into the given dom element.
         * @param el {HTMLElement}
         * @param errors {string}
         */
        Widget.prototype.renderError = function (el, errors) {
            el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
        }

        /**
         * Sets the collapsed property of the regionWidget object to true, and persists state to the server.
         * Does not cause any dom manipulation.
         */
        Widget.prototype.hide = function () {
            this.collapsed = true;

            api.rest.saveWidgetCollapsedState({
                regionWidgetId: this.regionWidgetId,
                collapsed: this.collapsed
            });
        }

        /**
         * Sets the collapsed property of the regionWidget object to false, and persists state to the server.
         * Does not cause any dom manipulation.
         */
        Widget.prototype.show = function () {
            this.collapsed = false;

            api.rest.saveWidgetCollapsedState({
                regionWidgetId: this.regionWidgetId,
                collapsed: this.collapsed
            });
        }

        /**
         * Unrenders the regionWidget from the page, and persists state to the server. If the regionWidget was
         * rendered into a view, also invokes module:rave_view_manager.destroyView method on that view.
         * @param [opts] {object} options object that is passed to the provider's close widget method.
         */
        Widget.prototype.close = function (opts) {
            this._provider.closeWidget(this, opts);
            if (this._view) {
                viewManager.destroyView(this._view);
            }

            api.rpc.removeWidget({
                regionWidgetId: this.regionWidgetId
            });
        }

        /**
         * A callback invoked after a successful call to the jsonRpc api.
         * @callback apiSuccessCallback
         * @param jsonRpcResult {object} - json rpc api result object.
         */

        /**
         * Makes api calls to persists move state to the server. Moves regionWidget object from one page to another.
         * Does not cause any dom manipulation.
         * @param toPageId {string}
         * @param [cb] {apiSuccessCallback} - callback function that will be invoked only if the state is successfully
         * saved to the server.
         */
        Widget.prototype.moveToPage = function (toPageId, cb) {
            api.rpc.moveWidgetToPage({
                toPageId: toPageId,
                regionWidgetId: this.regionWidgetId,
                successCallback: cb
            });
        }

        /**
         * Makes api calls to persist move state to the server. Moves the regionWidget object between regions on a page.
         * Does not cause any dom manipulation.
         * @param fromRegionId {string}
         * @param toRegionId {string}
         * @param toIndex {string}
         */
        Widget.prototype.moveToRegion = function (fromRegionId, toRegionId, toIndex) {
            api.rpc.moveWidgetToRegion({
                regionWidgetId: this.regionWidgetId,
                fromRegionId: fromRegionId,
                toRegionId: toRegionId,
                toIndex: toIndex
            });
        }

        /**
         * Regionwidget userPref object
         * @typedef Pref
         * @property name {string} Preference name
         * @property value {*} Preference value
         */

        /**
         * Updates the regionWidget's userPrefs object, updating a single preference, and persists state to server.
         * Does not cause any dom manipulation.
         * @param name {string}
         * @param val {*} new preference value
         */
        Widget.prototype.savePreference = function (name, val) {
            this.userPrefs[name] = val;
            api.rest.saveWidgetPreference({regionWidgetId: this.regionWidgetId, prefName: name, prefValue: val});
        }

        /**
         * Updates the regionWidget's userPrefs property, overwriting the entire object, and persists state to the
         * server. Does not cause any dom manipulation.
         * @param updatedPrefs {Array.<Pref>} Array of new preference objects
         */
        Widget.prototype.savePreferences = function (updatedPrefs) {
            this.userPrefs = updatedPrefs;
            api.rest.saveWidgetPreferences({regionWidgetId: this.regionWidgetId, userPrefs: updatedPrefs});
        }


        return Widget;
    })
