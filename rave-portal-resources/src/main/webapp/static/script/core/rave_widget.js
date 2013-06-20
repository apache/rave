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

/*
 Rave RegionWidget Interface

 Dependencies:
 rave.ui
 rave.api
 */


define(["./rave_api", "underscore"], function(api, _){
    /*
     rave widget constructor
     */
    var Widget = function (definition) {
        var provider = definition.type;

        _.extend(this, definition);

        this._provider = rave.getProvider(provider);

        if (!this._provider) {
            throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                'Provider ' + provider + ' is not registered.');
        }

        this._provider.initWidget(this);
    }

    Widget.defaultView = 'default';
    Widget.defaultWidth = 320;
    Widget.defaultHeight = 200;

    Widget.extend = function (mixin) {
        _.extend(this.prototype, mixin);
    }

    Widget.prototype.render = function (el, opts) {
        //if we receive only one argument, and the first arg is not a string or dom element, assume it is an opts object
        //and el should default to the widgets current render element
        if (!opts && !(_.isString(el) || (el instanceof HTMLElement))) {
            opts = el;
            el = this._el;
        }
        //if el is a string, go to rave's view system
        if (_.isString(el)) {
            //TODO: potential memory leak - rendering a widget into new views does not force cleanup of current view
            var view = rave.renderView(el, this);
            el = view.getWidgetSite();
            this._view = view;
        }
        //at this point el must be a valid dom element. if not, throw an error
        if (!(el instanceof HTMLElement)) {
            throw new Error('Cannot render widget. You must provide an el to render the view into');
        }
        this._el = el;
        this._provider.renderWidget(this, el, opts);
        return this;
    }

    Widget.prototype.renderError = function (el, errors) {
        el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
    }

    Widget.prototype.hide = function () {
        this.collapsed = true;

        api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.regionWidgetId,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.show = function () {
        this.collapsed = false;

        api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.regionWidgetId,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.close = function (opts) {
        this._provider.closeWidget(this, opts);
        if (this._view) {
            rave.destroyView(this._view);
        }

        api.rpc.removeWidget({
            regionWidgetId: this.regionWidgetId
        });
    }

    Widget.prototype.moveToPage = function (toPageId, cb) {
        api.rpc.moveWidgetToPage({
            toPageId: toPageId,
            regionWidgetId: this.regionWidgetId,
            successCallback: cb
        });
    }

    Widget.prototype.moveToRegion = function (fromRegionId, toRegionId, toIndex) {
        api.rpc.moveWidgetToRegion({
            regionWidgetId: this.regionWidgetId,
            fromRegionId: fromRegionId,
            toRegionId: toRegionId,
            toIndex: toIndex
        });
    }

    Widget.prototype.savePreference = function (name, val) {
        this.userPrefs[name] = val;
        api.rest.saveWidgetPreference({regionWidgetId: this.regionWidgetId, prefName: name, prefValue: val});
    }

    Widget.prototype.savePreferences = function (updatedPrefs) {
        this.userPrefs = updatedPrefs;
        api.rest.saveWidgetPreferences({regionWidgetId: this.regionWidgetId, userPrefs: updatedPrefs});
    }


    return Widget;
})
/*

rave = rave || {};

rave.RegionWidget = (function () {
    */
/*
     rave widget constructor
     *//*

    var Widget = function (definition) {
        var provider = definition.type;

        _.extend(this, definition);

        this._provider = rave.getProvider(provider);

        if (!this._provider) {
            throw new Error('Cannot render widget ' + definition.widgetUrl + '. ' +
                'Provider ' + provider + ' is not registered.');
        }

        this._provider.initWidget(this);
    }

    Widget.defaultView = 'default';
    Widget.defaultWidth = 320;
    Widget.defaultHeight = 200;

    Widget.extend = function (mixin) {
        _.extend(this.prototype, mixin);
    }

    Widget.prototype.render = function (el, opts) {
        //if we receive only one argument, and the first arg is not a string or dom element, assume it is an opts object
        //and el should default to the widgets current render element
        if (!opts && !(_.isString(el) || (el instanceof HTMLElement))) {
            opts = el;
            el = this._el;
        }
        //if el is a string, go to rave's view system
        if (_.isString(el)) {
            //TODO: potential memory leak - rendering a widget into new views does not force cleanup of current view
            var view = rave.renderView(el, this);
            el = view.getWidgetSite();
            this._view = view;
        }
        //at this point el must be a valid dom element. if not, throw an error
        if (!(el instanceof HTMLElement)) {
            throw new Error('Cannot render widget. You must provide an el to render the view into');
        }
        this._el = el;
        this._provider.renderWidget(this, el, opts);
        return this;
    }

    Widget.prototype.renderError = function (el, errors) {
        el.innerHTML = 'Error rendering widget.' + "<br /><br />" + errors;
    }

    Widget.prototype.hide = function () {
        this.collapsed = true;

        rave.api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.regionWidgetId,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.show = function () {
        this.collapsed = false;

        rave.api.rest.saveWidgetCollapsedState({
            regionWidgetId: this.regionWidgetId,
            collapsed: this.collapsed
        });
    }

    Widget.prototype.close = function (opts) {
        this._provider.closeWidget(this, opts);
        if (this._view) {
            rave.destroyView(this._view);
        }

        rave.api.rpc.removeWidget({
            regionWidgetId: this.regionWidgetId
        });
    }

    Widget.prototype.moveToPage = function (toPageId, cb) {
        rave.api.rpc.moveWidgetToPage({
            toPageId: toPageId,
            regionWidgetId: this.regionWidgetId,
            successCallback: cb
        });
    }

    Widget.prototype.moveToRegion = function (fromRegionId, toRegionId, toIndex) {
        rave.api.rpc.moveWidgetToRegion({
            regionWidgetId: this.regionWidgetId,
            fromRegionId: fromRegionId,
            toRegionId: toRegionId,
            toIndex: toIndex
        });
    }

    Widget.prototype.savePreference = function (name, val) {
        this.userPrefs[name] = val;
        rave.api.rest.saveWidgetPreference({regionWidgetId: this.regionWidgetId, prefName: name, prefValue: val});
    }

    Widget.prototype.savePreferences = function (updatedPrefs) {
        this.userPrefs = updatedPrefs;
        rave.api.rest.saveWidgetPreferences({regionWidgetId: this.regionWidgetId, userPrefs: updatedPrefs});
    }


    return Widget;

})();*/
